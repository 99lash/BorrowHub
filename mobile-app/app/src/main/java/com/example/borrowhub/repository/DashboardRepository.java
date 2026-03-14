package com.example.borrowhub.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.borrowhub.data.local.dao.DashboardStatsDao;
import com.example.borrowhub.data.local.dao.RecentTransactionDao;
import com.example.borrowhub.data.local.entity.DashboardStatsEntity;
import com.example.borrowhub.data.local.entity.RecentTransactionEntity;
import com.example.borrowhub.data.remote.api.ApiService;
import com.example.borrowhub.data.remote.dto.DashboardStatsDTO;
import com.example.borrowhub.data.remote.dto.RecentTransactionDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardRepository {

    private final ApiService apiService;
    private final DashboardStatsDao dashboardStatsDao;
    private final RecentTransactionDao recentTransactionDao;
    private final ExecutorService executorService;

    public DashboardRepository(ApiService apiService, DashboardStatsDao dashboardStatsDao, RecentTransactionDao recentTransactionDao) {
        this.apiService = apiService;
        this.dashboardStatsDao = dashboardStatsDao;
        this.recentTransactionDao = recentTransactionDao;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<DashboardStatsEntity> getDashboardStats(String token) {
        // Fetch from remote and update local cache
        apiService.getDashboardStats("Bearer " + token).enqueue(new Callback<DashboardStatsDTO>() {
            @Override
            public void onResponse(Call<DashboardStatsDTO> call, Response<DashboardStatsDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DashboardStatsDTO dto = response.body();
                    executorService.execute(() -> {
                        DashboardStatsEntity entity = new DashboardStatsEntity(dto.getTotalBorrowed(), dto.getActiveRequests(), dto.getOverdueItems());
                        dashboardStatsDao.deleteAll();
                        dashboardStatsDao.insert(entity);
                    });
                }
            }

            @Override
            public void onFailure(Call<DashboardStatsDTO> call, Throwable t) {
                // Log failure, UI will still show cached data via LiveData from DAO
            }
        });

        // Always return the local cache
        return dashboardStatsDao.getDashboardStats();
    }

    public LiveData<List<RecentTransactionEntity>> getRecentTransactions(String token) {
        // Fetch from remote and update local cache
        apiService.getRecentTransactions("Bearer " + token).enqueue(new Callback<List<RecentTransactionDTO>>() {
            @Override
            public void onResponse(Call<List<RecentTransactionDTO>> call, Response<List<RecentTransactionDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<RecentTransactionDTO> dtoList = response.body();
                    executorService.execute(() -> {
                        List<RecentTransactionEntity> entityList = new ArrayList<>();
                        for (RecentTransactionDTO dto : dtoList) {
                            entityList.add(new RecentTransactionEntity(dto.getId(), dto.getItemName(), dto.getStatus(), dto.getDate()));
                        }
                        recentTransactionDao.deleteAll();
                        recentTransactionDao.insertAll(entityList);
                    });
                }
            }

            @Override
            public void onFailure(Call<List<RecentTransactionDTO>> call, Throwable t) {
                 // Log failure, UI will still show cached data via LiveData from DAO
            }
        });

        // Always return the local cache
        return recentTransactionDao.getRecentTransactions();
    }
}
