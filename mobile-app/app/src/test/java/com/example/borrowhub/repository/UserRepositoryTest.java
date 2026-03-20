package com.example.borrowhub.repository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.borrowhub.data.local.SessionManager;
import com.example.borrowhub.data.local.dao.UserDao;
import com.example.borrowhub.data.local.entity.User;
import com.example.borrowhub.data.remote.api.ApiService;
import com.example.borrowhub.data.remote.dto.ApiResponseDTO;
import com.example.borrowhub.data.remote.dto.ChangePasswordRequestDTO;
import com.example.borrowhub.data.remote.dto.LoginRequestDTO;
import com.example.borrowhub.data.remote.dto.LoginResponseDTO;
import com.example.borrowhub.data.remote.dto.UpdateProfileRequestDTO;
import com.example.borrowhub.data.remote.dto.UserDTO;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepositoryTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private ApiService mockApiService;
    @Mock
    private SessionManager mockSessionManager;
    @Mock
    private UserDao mockUserDao;
    @Mock
    private Call<LoginResponseDTO> mockCall;
    @Mock
    private Observer<Boolean> mockObserver;
    @Mock
    private Call<Void> mockLogoutCall;
    @Mock
    private Call<ApiResponseDTO<UserDTO>> mockUpdateProfileCall;
    @Mock
    private Call<ApiResponseDTO<Void>> mockChangePasswordCall;

    private UserRepository userRepository;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        userRepository = new UserRepository(mockApiService, mockSessionManager, mockUserDao);
    }

    @Test
    public void login_success_savesTokenAndUser() {
        // Arrange
        LoginResponseDTO mockResponse = new LoginResponseDTO();
        LoginResponseDTO.Data mockData = new LoginResponseDTO.Data();
        mockData.setToken("fake_token");
        UserDTO userDto = new UserDTO();
        userDto.setId(1);
        userDto.setName("Test User");
        userDto.setUsername("testuser");
        userDto.setRole("student");
        mockData.setUser(userDto);
        mockResponse.setData(mockData);

        when(mockApiService.login(any(LoginRequestDTO.class))).thenReturn(mockCall);

        // Act
        LiveData<Boolean> result = userRepository.login("testuser", "password");
        result.observeForever(mockObserver);

        // Capture callback
        ArgumentCaptor<Callback<LoginResponseDTO>> captor = ArgumentCaptor.forClass(Callback.class);
        verify(mockCall).enqueue(captor.capture());

        // Simulate success response
        captor.getValue().onResponse(mockCall, Response.success(mockResponse));

        // Assert
        verify(mockSessionManager).saveAuthToken("Bearer fake_token");
        verify(mockUserDao, timeout(500)).insertUser(any(User.class));
        verify(mockObserver).onChanged(true);
    }

    @Test
    public void logout_clearsSessionAndDeletesUser() {
        // Arrange
        when(mockSessionManager.getAuthToken()).thenReturn("Bearer fake_token");
        when(mockApiService.logout("Bearer fake_token")).thenReturn(mockLogoutCall);

        // Act
        LiveData<Boolean> result = userRepository.logout();
        result.observeForever(mockObserver);

        ArgumentCaptor<Callback<Void>> captor = ArgumentCaptor.forClass(Callback.class);
        verify(mockLogoutCall).enqueue(captor.capture());
        captor.getValue().onResponse(mockLogoutCall, Response.success(null));

        // Assert
        verify(mockSessionManager).clearSession();
        verify(mockUserDao, timeout(500)).deleteAll();
        verify(mockApiService).logout("Bearer fake_token");
        verify(mockObserver).onChanged(true);
        result.removeObserver(mockObserver);
    }

    @Test
    public void updateProfile_success_updatesLocalUser() {
        ApiResponseDTO<UserDTO> apiResponse = new ApiResponseDTO<>();
        apiResponse.setStatus("success");
        UserDTO userDto = new UserDTO();
        userDto.setId(9);
        userDto.setName("Updated Name");
        userDto.setUsername("updated");
        userDto.setRole("staff");
        apiResponse.setData(userDto);

        when(mockSessionManager.getAuthToken()).thenReturn("Bearer fake_token");
        when(mockApiService.updateProfile(anyString(), any(UpdateProfileRequestDTO.class))).thenReturn(mockUpdateProfileCall);

        LiveData<Boolean> result = userRepository.updateProfile("Updated Name", "updated");
        result.observeForever(mockObserver);

        ArgumentCaptor<Callback<ApiResponseDTO<UserDTO>>> captor = ArgumentCaptor.forClass(Callback.class);
        verify(mockUpdateProfileCall).enqueue(captor.capture());
        captor.getValue().onResponse(mockUpdateProfileCall, Response.success(apiResponse));
        verify(mockUserDao, timeout(500)).insertUser(any(User.class));
        verify(mockObserver).onChanged(true);
        result.removeObserver(mockObserver);
    }

    @Test
    public void changePassword_success_returnsTrue() {
        ApiResponseDTO<Void> apiResponse = new ApiResponseDTO<>();
        apiResponse.setStatus("success");
        when(mockSessionManager.getAuthToken()).thenReturn("Bearer fake_token");
        when(mockApiService.changePassword(anyString(), any(ChangePasswordRequestDTO.class))).thenReturn(mockChangePasswordCall);

        LiveData<Boolean> result = userRepository.changePassword("old", "new", "new");
        result.observeForever(mockObserver);

        ArgumentCaptor<Callback<ApiResponseDTO<Void>>> captor = ArgumentCaptor.forClass(Callback.class);
        verify(mockChangePasswordCall).enqueue(captor.capture());
        captor.getValue().onResponse(mockChangePasswordCall, Response.success(apiResponse));

        verify(mockObserver).onChanged(true);
        result.removeObserver(mockObserver);
    }
}
