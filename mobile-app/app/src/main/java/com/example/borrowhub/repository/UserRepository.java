package com.example.borrowhub.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.borrowhub.data.local.AppDatabase;
import com.example.borrowhub.data.local.SessionManager;
import com.example.borrowhub.data.local.dao.UserDao;
import com.example.borrowhub.data.local.entity.User;
import com.example.borrowhub.data.remote.ApiClient;
import com.example.borrowhub.data.remote.api.ApiService;
import com.example.borrowhub.data.remote.dto.ApiResponseDTO;
import com.example.borrowhub.data.remote.dto.ChangePasswordRequestDTO;
import com.example.borrowhub.data.remote.dto.LoginRequestDTO;
import com.example.borrowhub.data.remote.dto.LoginResponseDTO;
import com.example.borrowhub.data.remote.dto.UpdateProfileRequestDTO;
import com.example.borrowhub.data.remote.dto.UserDTO;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private static final String TAG = "UserRepository";
    private final ApiService apiService;
    private final SessionManager sessionManager;
    private final UserDao userDao;
    private final ExecutorService executorService;

    public UserRepository(Application application) {
        this.sessionManager = new SessionManager(application);
        this.apiService = ApiClient.getInstance(this.sessionManager).getApiService();
        this.userDao = AppDatabase.getInstance(application).userDao();
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public UserRepository(ApiService apiService, SessionManager sessionManager, UserDao userDao) {
        this.apiService = apiService;
        this.sessionManager = sessionManager;
        this.userDao = userDao;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<User> getUser() {
        return userDao.getUser();
    }

    public LiveData<Boolean> login(String username, String password) {
        MutableLiveData<Boolean> loginResult = new MutableLiveData<>();
        
        LoginRequestDTO request = new LoginRequestDTO(username, password);
        apiService.login(request).enqueue(new Callback<LoginResponseDTO>() {
            @Override
            public void onResponse(Call<LoginResponseDTO> call, Response<LoginResponseDTO> response) {
                Log.d(TAG, "Login Response Code: " + response.code());
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    LoginResponseDTO.Data data = response.body().getData();
                    sessionManager.saveAuthToken("Bearer " + data.getToken());
                    
                    UserDTO userDto = data.getUser();
                    if (userDto != null) {
                        User user = new User(userDto.getId(), userDto.getName(), userDto.getUsername(), userDto.getRole());
                        executorService.execute(() -> userDao.insertUser(user));
                    }
                    loginResult.postValue(true);
                } else {
                    loginResult.postValue(false);
                }
            }

            @Override
            public void onFailure(Call<LoginResponseDTO> call, Throwable t) {
                Log.e(TAG, "Login Failure: " + t.getMessage(), t);
                loginResult.postValue(false);
            }
        });

        return loginResult;
    }

    public LiveData<Boolean> logout() {
        MutableLiveData<Boolean> logoutResult = new MutableLiveData<>();

        String token = sessionManager.getAuthToken();
        if (isValidToken(token)) {
            apiService.logout(token).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    clearLocalSession(logoutResult);
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    clearLocalSession(logoutResult);
                }
            });
        } else {
            clearLocalSession(logoutResult);
        }

        return logoutResult;
    }

    private void clearLocalSession(MutableLiveData<Boolean> logoutResult) {
        try {
            sessionManager.clearSession();
        } catch (RuntimeException e) {
            Log.e(TAG, "Failed to clear auth session", e);
            logoutResult.postValue(false);
            return;
        }

        executorService.execute(() -> {
            try {
                userDao.deleteAll();
                logoutResult.postValue(true);
            } catch (RuntimeException e) {
                Log.e(TAG, "Failed to clear local session", e);
                logoutResult.postValue(false);
            }
        });
    }

    public boolean hasActiveSession() {
        String token = sessionManager.getAuthToken();
        return isValidToken(token);
    }

    public LiveData<Boolean> updateProfile(String fullName, String username) {
        MutableLiveData<Boolean> updateResult = new MutableLiveData<>();
        String token = sessionManager.getAuthToken();
        if (!isValidToken(token)) {
            updateResult.postValue(false);
            return updateResult;
        }

        UpdateProfileRequestDTO request = new UpdateProfileRequestDTO(fullName, username);
        apiService.updateProfile(token, request).enqueue(new Callback<ApiResponseDTO<UserDTO>>() {
            @Override
            public void onResponse(Call<ApiResponseDTO<UserDTO>> call, Response<ApiResponseDTO<UserDTO>> response) {
                if (response.isSuccessful()
                        && response.body() != null
                        && response.body().isSuccess()
                        && response.body().getData() != null) {
                    UserDTO userDto = response.body().getData();
                    executorService.execute(() -> {
                        userDao.insertUser(new User(
                                userDto.getId(),
                                userDto.getName(),
                                userDto.getUsername(),
                                userDto.getRole()
                        ));
                        updateResult.postValue(true);
                    });
                } else {
                    updateResult.postValue(false);
                }
            }

            @Override
            public void onFailure(Call<ApiResponseDTO<UserDTO>> call, Throwable t) {
                Log.e(TAG, "Profile update failed: " + t.getMessage(), t);
                updateResult.postValue(false);
            }
        });

        return updateResult;
    }

    public LiveData<Boolean> changePassword(String currentPassword, String newPassword, String confirmPassword) {
        MutableLiveData<Boolean> changePasswordResult = new MutableLiveData<>();
        String token = sessionManager.getAuthToken();
        if (!isValidToken(token)) {
            changePasswordResult.postValue(false);
            return changePasswordResult;
        }

        ChangePasswordRequestDTO request = new ChangePasswordRequestDTO(
                currentPassword, newPassword, confirmPassword
        );
        apiService.changePassword(token, request).enqueue(new Callback<ApiResponseDTO<Void>>() {
            @Override
            public void onResponse(Call<ApiResponseDTO<Void>> call, Response<ApiResponseDTO<Void>> response) {
                boolean success = response.isSuccessful()
                        && response.body() != null
                        && response.body().isSuccess();
                changePasswordResult.postValue(success);
            }

            @Override
            public void onFailure(Call<ApiResponseDTO<Void>> call, Throwable t) {
                Log.e(TAG, "Change password failed: " + t.getMessage(), t);
                changePasswordResult.postValue(false);
            }
        });

        return changePasswordResult;
    }

    private boolean isValidToken(String token) {
        return token != null && !token.trim().isEmpty();
    }
}
