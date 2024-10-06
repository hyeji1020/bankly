package com.project.bankassetor.filter;

import com.project.bankassetor.filter.response.LocationResponse;
import com.project.bankassetor.model.entity.AccessLog;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

import static com.project.bankassetor.utils.Utils.toJson;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccessLogFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        long startTime = System.currentTimeMillis(); // 시작시간
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        AccessLog accessLog = new AccessLog();
        accessLog.setHost(httpRequest.getRemoteHost());

        // 클라이언트 IP 설정
        String clientIp = AccessLogUtil.getClientIp(httpRequest);
        accessLog.setClientIp(clientIp);

        // User-Agent 파싱하여 관련 필드 설정
        String userAgent = httpRequest.getHeader("User-Agent");
        accessLog.setUserAgent(userAgent);
        AccessLogUtil.getUserAgent(userAgent, accessLog);

        accessLog.setUri(httpRequest.getRequestURI());
        accessLog.setMethod(httpRequest.getMethod());
        accessLog.setRequestAt(LocalDateTime.now());
        accessLog.setReferer(httpRequest.getHeader("Referer"));

        // 위치 정보 (IP 기반) 설정
        LocationResponse location = AccessLogUtil.getLocationInfoByIp(clientIp);
        accessLog.setCountry(location.getCountry());
        accessLog.setCity(location.getCity());

        /**
         * 사용자 request 처리 과정
         * 1. AccessLogFilter 실행
         * 2. 필터 실행 (chain.doFilter(request, response))
         * 3. 컨트롤러에서 응답 (JSON, HTML 등 반환)
         */

        chain.doFilter(request, response);

        /**
         * 사용자 response 처리 과정
         * 1. AccessLogFilter에서 후처리
         * 2. AccessLog 정보 업데이트
         * 3. AccessLog 정보를 로그에 기록
         */
        long timeGap = System.currentTimeMillis() - startTime;  // 요청 처리에 소요된 시간 계산
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        accessLog.setElapsed(timeGap);
        accessLog.setResponseAt(LocalDateTime.now());
        accessLog.setStatus(String.valueOf(httpServletResponse.getStatus()));

        log.info("AccessLog ==> {}", toJson(accessLog));

    }
}

