package com.example.borrowhub.data.remote.api;

import com.example.borrowhub.data.remote.dto.LoginRequestDTO;
import com.example.borrowhub.data.remote.dto.LoginResponseDTO;

import com.example.borrowhub.data.remote.dto.DashboardStatsDTO;
import com.example.borrowhub.data.remote.dto.RecentTransactionDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Header;

/**
 * Interface defining the API endpoints for the BorrowHub backend.
 */
public interface ApiService {

    @POST("api/v1/login")
    Call<LoginResponseDTO> login(@Body LoginRequestDTO request);

    @POST("api/v1/logout")
    Call<Void> logout(@Header("Authorization") String token);

    @GET("api/v1/dashboard/stats")
    Call<DashboardStatsDTO> getDashboardStats(@Header("Authorization") String token);

    @GET("api/v1/dashboard/recent-transactions")
    Call<List<RecentTransactionDTO>> getRecentTransactions(@Header("Authorization") String token);

    // TODO: Add more endpoints (items, borrow requests, etc.)
}
