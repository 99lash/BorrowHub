package com.example.borrowhub.repository;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.borrowhub.data.remote.api.ApiService;
import com.example.borrowhub.data.remote.dto.DashboardStatsDTO;
import com.example.borrowhub.data.remote.dto.RecentTransactionDTO;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DashboardRepositoryTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private ApiService apiService;

    @Mock
    private Call<DashboardStatsDTO> statsCall;

    @Mock
    private Call<List<RecentTransactionDTO>> transactionsCall;

    @Mock
    private Observer<DashboardStatsDTO> statsObserver;

    @Mock
    private Observer<List<RecentTransactionDTO>> transactionsObserver;

    private DashboardRepository repository;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        repository = new DashboardRepository(apiService);
    }

    @Test
    public void testGetDashboardStats_Success() {
        DashboardStatsDTO mockStats = new DashboardStatsDTO();
        mockStats.setTotalBorrowed(10);

        when(apiService.getDashboardStats(anyString())).thenReturn(statsCall);

        doAnswer(invocation -> {
            Callback<DashboardStatsDTO> callback = invocation.getArgument(0);
            callback.onResponse(statsCall, Response.success(mockStats));
            return null;
        }).when(statsCall).enqueue(any(Callback.class));

        LiveData<DashboardStatsDTO> liveData = repository.getDashboardStats("test_token");
        liveData.observeForever(statsObserver);

        verify(statsObserver).onChanged(mockStats);
        assertEquals(10, liveData.getValue().getTotalBorrowed());
    }

    @Test
    public void testGetDashboardStats_Failure() {
        when(apiService.getDashboardStats(anyString())).thenReturn(statsCall);

        doAnswer(invocation -> {
            Callback<DashboardStatsDTO> callback = invocation.getArgument(0);
            callback.onFailure(statsCall, new Throwable("Network Error"));
            return null;
        }).when(statsCall).enqueue(any(Callback.class));

        LiveData<DashboardStatsDTO> liveData = repository.getDashboardStats("test_token");
        liveData.observeForever(statsObserver);

        verify(statsObserver).onChanged(null);
        assertNull(liveData.getValue());
    }

    @Test
    public void testGetRecentTransactions_Success() {
        List<RecentTransactionDTO> mockTransactions = new ArrayList<>();
        RecentTransactionDTO transaction = new RecentTransactionDTO();
        transaction.setItemName("Test Item");
        mockTransactions.add(transaction);

        when(apiService.getRecentTransactions(anyString())).thenReturn(transactionsCall);

        doAnswer(invocation -> {
            Callback<List<RecentTransactionDTO>> callback = invocation.getArgument(0);
            callback.onResponse(transactionsCall, Response.success(mockTransactions));
            return null;
        }).when(transactionsCall).enqueue(any(Callback.class));

        LiveData<List<RecentTransactionDTO>> liveData = repository.getRecentTransactions("test_token");
        liveData.observeForever(transactionsObserver);

        verify(transactionsObserver).onChanged(mockTransactions);
        assertEquals(1, liveData.getValue().size());
        assertEquals("Test Item", liveData.getValue().get(0).getItemName());
    }

    @Test
    public void testGetRecentTransactions_Failure() {
        when(apiService.getRecentTransactions(anyString())).thenReturn(transactionsCall);

        doAnswer(invocation -> {
            Callback<List<RecentTransactionDTO>> callback = invocation.getArgument(0);
            callback.onFailure(transactionsCall, new Throwable("Network Error"));
            return null;
        }).when(transactionsCall).enqueue(any(Callback.class));

        LiveData<List<RecentTransactionDTO>> liveData = repository.getRecentTransactions("test_token");
        liveData.observeForever(transactionsObserver);

        verify(transactionsObserver).onChanged(null);
        assertNull(liveData.getValue());
    }
}
