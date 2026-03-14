package com.example.borrowhub.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.borrowhub.data.remote.api.ApiService;
import com.example.borrowhub.data.remote.dto.DashboardStatsDTO;
import com.example.borrowhub.data.remote.dto.RecentTransactionDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardRepository {

    private final ApiService apiService;

    public DashboardRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<DashboardStatsDTO> getDashboardStats(String token) {
        MutableLiveData<DashboardStatsDTO> statsData = new MutableLiveData<>();

        apiService.getDashboardStats("Bearer " + token).enqueue(new Callback<DashboardStatsDTO>() {
            @Override
            public void onResponse(Call<DashboardStatsDTO> call, Response<DashboardStatsDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    statsData.postValue(response.body());
                } else {
                    statsData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<DashboardStatsDTO> call, Throwable t) {
                statsData.postValue(null);
            }
        });

        return statsData;
    }

    public LiveData<List<RecentTransactionDTO>> getRecentTransactions(String token) {
        MutableLiveData<List<RecentTransactionDTO>> transactionsData = new MutableLiveData<>();

        apiService.getRecentTransactions("Bearer " + token).enqueue(new Callback<List<RecentTransactionDTO>>() {
            @Override
            public void onResponse(Call<List<RecentTransactionDTO>> call, Response<List<RecentTransactionDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    transactionsData.postValue(response.body());
                } else {
                    transactionsData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<RecentTransactionDTO>> call, Throwable t) {
                transactionsData.postValue(null);
            }
        });

        return transactionsData;
    }
}
