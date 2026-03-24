package com.example.borrowhub.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.borrowhub.data.local.AppDatabase;
import com.example.borrowhub.data.local.SessionManager;
import com.example.borrowhub.data.local.dao.CourseDao;
import com.example.borrowhub.data.local.dao.StudentDao;
import com.example.borrowhub.data.local.entity.CourseEntity;
import com.example.borrowhub.data.remote.api.ApiService;
import com.example.borrowhub.data.remote.dto.ApiResponseDTO;
import com.example.borrowhub.data.remote.dto.CourseDTO;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseIntegrationTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private StudentDao studentDao;
    @Mock
    private CourseDao courseDao;
    @Mock
    private AppDatabase database;
    @Mock
    private ApiService apiService;
    @Mock
    private SessionManager sessionManager;

    private StudentRepository repository;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(sessionManager.getAuthToken()).thenReturn("test_token");
        doAnswer(invocation -> {
            Runnable runnable = invocation.getArgument(0);
            runnable.run();
            return null;
        }).when(database).runInTransaction(any(Runnable.class));
        repository = new StudentRepository(studentDao, courseDao, database, apiService, sessionManager);
    }

    @Test
    public void refreshCoursesFromApi_success_syncsAndReturnsCourseNames() throws Exception {
        Call<ApiResponseDTO<List<CourseDTO>>> mockCall = mock(Call.class);
        when(apiService.getCourses(anyString())).thenReturn(mockCall);

        ApiResponseDTO<List<CourseDTO>> body = new ApiResponseDTO<>();
        body.setStatus("success");
        List<CourseDTO> data = new ArrayList<>();

        CourseDTO c1 = new CourseDTO();
        c1.setId(1L);
        c1.setName("BSCS");
        data.add(c1);

        CourseDTO c2 = new CourseDTO();
        c2.setId(2L);
        c2.setName("BSIT");
        data.add(c2);

        body.setData(data);

        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<List<String>> callbackResult = new AtomicReference<>(new ArrayList<>());

        repository.refreshCoursesFromApi(courseNames -> {
            callbackResult.set(courseNames);
            latch.countDown();
        });

        ArgumentCaptor<Callback<ApiResponseDTO<List<CourseDTO>>>> captor = ArgumentCaptor.forClass(Callback.class);
        verify(mockCall).enqueue(captor.capture());
        captor.getValue().onResponse(mockCall, Response.success(body));

        assertTrue(latch.await(2, TimeUnit.SECONDS));

        verify(database).runInTransaction(any(Runnable.class));
        verify(courseDao).deleteAll();

        ArgumentCaptor<List<CourseEntity>> entitiesCaptor = ArgumentCaptor.forClass(List.class);
        verify(courseDao).insertAll(entitiesCaptor.capture());
        assertEquals(2, entitiesCaptor.getValue().size());

        assertEquals(2, callbackResult.get().size());
        assertEquals("BSCS", callbackResult.get().get(0));
        assertEquals("BSIT", callbackResult.get().get(1));
    }
}
