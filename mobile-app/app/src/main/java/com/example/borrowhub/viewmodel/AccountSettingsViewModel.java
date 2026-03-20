package com.example.borrowhub.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.borrowhub.R;
import com.example.borrowhub.data.local.entity.User;
import com.example.borrowhub.repository.UserRepository;

public class AccountSettingsViewModel extends AndroidViewModel {
    private final UserRepository userRepository;
    private final LiveData<User> user;
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> operationError = new MutableLiveData<>();
    private final MutableLiveData<String> operationSuccess = new MutableLiveData<>();

    public AccountSettingsViewModel(@NonNull Application application) {
        super(application);
        this.userRepository = new UserRepository(application);
        this.user = userRepository.getUser();
    }

    public AccountSettingsViewModel(@NonNull Application application, UserRepository userRepository) {
        super(application);
        this.userRepository = userRepository;
        this.user = userRepository.getUser();
    }

    public LiveData<User> getUser() {
        return user;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getOperationError() {
        return operationError;
    }

    public LiveData<String> getOperationSuccess() {
        return operationSuccess;
    }

    public void clearOperationStates() {
        operationError.setValue(null);
        operationSuccess.setValue(null);
    }

    public void updateProfile(String fullName, String username) {
        if (isBlank(fullName) || isBlank(username)) {
            operationError.setValue(getApplication().getString(R.string.account_settings_error_fields_required));
            return;
        }

        isLoading.setValue(true);
        LiveData<Boolean> repositoryResult = userRepository.updateProfile(fullName.trim(), username.trim());
        Observer<Boolean> observer = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean success) {
                isLoading.setValue(false);
                if (Boolean.TRUE.equals(success)) {
                    operationSuccess.setValue(getApplication().getString(R.string.account_settings_profile_updated));
                } else {
                    operationError.setValue(getApplication().getString(R.string.account_settings_profile_update_failed));
                }
                repositoryResult.removeObserver(this);
            }
        };
        repositoryResult.observeForever(observer);
    }

    public void changePassword(String currentPassword, String newPassword, String confirmNewPassword) {
        if (isBlank(currentPassword) || isBlank(newPassword) || isBlank(confirmNewPassword)) {
            operationError.setValue(getApplication().getString(R.string.account_settings_error_fields_required));
            return;
        }
        if (!newPassword.equals(confirmNewPassword)) {
            operationError.setValue(getApplication().getString(R.string.account_settings_password_mismatch));
            return;
        }

        isLoading.setValue(true);
        LiveData<Boolean> repositoryResult = userRepository.changePassword(
                currentPassword,
                newPassword,
                confirmNewPassword
        );
        Observer<Boolean> observer = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean success) {
                isLoading.setValue(false);
                if (Boolean.TRUE.equals(success)) {
                    operationSuccess.setValue(getApplication().getString(R.string.account_settings_password_updated));
                } else {
                    operationError.setValue(getApplication().getString(R.string.account_settings_password_update_failed));
                }
                repositoryResult.removeObserver(this);
            }
        };
        repositoryResult.observeForever(observer);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
