package com.example.borrowhub.data.local;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class SessionManagerTest {

    @Mock
    private Context mockContext;
    @Mock
    private SharedPreferences mockPreferences;
    @Mock
    private SharedPreferences.Editor mockEditor;

    private SessionManager sessionManager;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(mockContext.getSharedPreferences("BorrowHubPrefs", Context.MODE_PRIVATE)).thenReturn(mockPreferences);
        when(mockPreferences.edit()).thenReturn(mockEditor);
        when(mockEditor.putString("auth_token", "Bearer token")).thenReturn(mockEditor);
        when(mockEditor.remove("auth_token")).thenReturn(mockEditor);
        when(mockEditor.putInt("theme_mode", 2)).thenReturn(mockEditor);
        when(mockEditor.putInt("theme_mode", 1)).thenReturn(mockEditor);
        sessionManager = new SessionManager(mockContext);
    }

    @Test
    public void saveAndGetAuthToken_usesSharedPreferences() {
        sessionManager.saveAuthToken("Bearer token");
        verify(mockEditor).putString("auth_token", "Bearer token");
        verify(mockEditor).apply();

        when(mockPreferences.getString("auth_token", null)).thenReturn("Bearer token");
        assertEquals("Bearer token", sessionManager.getAuthToken());
    }

    @Test
    public void clearSession_removesAuthToken() {
        sessionManager.clearSession();
        verify(mockEditor).remove("auth_token");
        verify(mockEditor).apply();
    }

    @Test
    public void setAndGetThemeMode_persistsMode() {
        sessionManager.setThemeMode(2);
        verify(mockEditor).putInt("theme_mode", 2);
        verify(mockEditor).apply();

        when(mockPreferences.getInt("theme_mode", -1)).thenReturn(2);
        assertEquals(2, sessionManager.getThemeMode());
    }

    @Test
    public void getThemeMode_defaultIsSystem() {
        when(mockPreferences.getInt("theme_mode", -1)).thenReturn(-1);
        assertEquals(-1, sessionManager.getThemeMode());
    }
}
