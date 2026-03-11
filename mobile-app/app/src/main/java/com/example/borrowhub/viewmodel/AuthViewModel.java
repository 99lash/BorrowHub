package com.example.borrowhub.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.borrowhub.repository.UserRepository;

public class AuthViewModel extends AndroidViewModel {

    private final UserRepository userRepository;

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public AuthViewModel(Application application) {
        super(application);
        this.userRepository = new UserRepository(application);
    }

    public AuthViewModel(Application application, UserRepository userRepository) {
        super(application);
        this.userRepository = userRepository;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> login(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            errorMessage.setValue("Username and password cannot be empty");
            MutableLiveData<Boolean> errorResult = new MutableLiveData<>(false);
            return errorResult;
        }

        isLoading.setValue(true);
        LiveData<Boolean> repoResult = userRepository.login(username, password);

        // We wrap the repository result to handle loading state
        MutableLiveData<Boolean> finalResult = new MutableLiveData<>();
        
        // This is a simple way to observe the repoResult once and update states
        repoResult.observeForever(success -> {
            isLoading.postValue(false);
            if (!success) {
                errorMessage.postValue("Invalid Credentials: Username or password is incorrect.");
            }
            finalResult.postValue(success);
        });

        return finalResult;
    }
}
