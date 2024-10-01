package com.project.bankassetor.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.mockito.Mockito.*;
class AccessLogFilterTest {

    @InjectMocks
    private AccessLogFilter accessLogFilter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("엑세스 로그 필터 성공")
    void doFilter_shouldLogAccessInformation() throws IOException, ServletException {
        // Given
        when(request.getRemoteHost()).thenReturn("localhost");
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
        when(request.getHeader("User-Agent")).thenReturn("Mozilla/5.0");
        when(request.getRequestURI()).thenReturn("/test-uri");
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Referer")).thenReturn("http://referer.com");
        when(response.getStatus()).thenReturn(200);

        // When
        accessLogFilter.doFilter(request, response, filterChain);

        // Then
        verify(filterChain, times(1)).doFilter(request, response); // 필터 체인 실행 여부 확인
    }
}