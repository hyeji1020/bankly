package com.project.bankassetor.filter;

import com.project.bankassetor.filter.response.LocationResponse;
import com.project.bankassetor.model.entity.AccessLog;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.RabbitTemplateConfigurer;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static com.project.bankassetor.utils.Utils.toJson;

@Profile("api")
@Slf4j
@Component
@RequiredArgsConstructor
public class AccessLogFilter implements Filter {

    private final RabbitTemplate rabbitTemplate;

    // RabbitMQ로 보낼 Exchange 이름과 Routing Key를 주입
    @Value("${mq.exchange}")
    private String exchange;

    @Value("${mq.routing-key}")
    private String routingKey;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // HttpServletRequest와 HttpServletResponse를 감싸는 Wrapper로 변환
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper((HttpServletResponse) response);

        // AccessLog 객체 생성 및 기본 정보 설정
        AccessLog accessLog = createAccessLog(requestWrapper);

        try {
            // 요청 데이터 설정
            setRequestData(requestWrapper, accessLog);

            // 다음 필터 또는 서블릿 호출
            chain.doFilter(requestWrapper, responseWrapper);
        } finally {
            // 응답 데이터 설정 및 로그 기록
            setResponseData(responseWrapper, accessLog);
            finalizeAccessLog(responseWrapper, accessLog);
        }

            sendAccessLogToExternalServer(accessLog);

    }

    /**
     * AccessLog 객체 생성 및 기본 정보 설정
     */
    private AccessLog createAccessLog(ContentCachingRequestWrapper requestWrapper) {
        AccessLog accessLog = new AccessLog();
        accessLog.setHost(requestWrapper.getRemoteHost());

        // 클라이언트 IP 설정
        String clientIp = AccessLogUtil.getClientIp(requestWrapper);
        accessLog.setClientIp(clientIp);

        // User-Agent 파싱하여 관련 필드 설정
        String userAgent = requestWrapper.getHeader("User-Agent");
        accessLog.setUserAgent(userAgent);
        AccessLogUtil.getUserAgent(userAgent, accessLog);

        // requestId UUID 설정
        String requestId = AccessLogUtil.generateRequestId();
        accessLog.setRequestId(requestId);
        requestWrapper.setAttribute("requestId", requestId);

        accessLog.setUri(requestWrapper.getRequestURI());
        accessLog.setMethod(requestWrapper.getMethod());
        accessLog.setRequestAt(LocalDateTime.now());
        accessLog.setReferer(requestWrapper.getHeader("Referer"));

        // 위치 정보 (IP 기반) 설정
        LocationResponse location = AccessLogUtil.getLocationInfoByIp(clientIp);
        accessLog.setCountry(location.getCountry());
        accessLog.setCity(location.getCity());

        return accessLog;
    }

    /**
     * 요청 데이터를 설정하는 메서드.
     *
     * ContentCachingRequestWrapper를 사용하여 요청 본문 또는 쿼리 스트링을 읽고,
     * AccessLog 객체의 `request` 필드에 JSON 형식으로 저장합니다.
     *
     * @param requestWrapper ContentCachingRequestWrapper로 감싸진 HttpServletRequest 객체
     * @param accessLog      요청 데이터를 저장할 AccessLog 객체
     */
    private void setRequestData(ContentCachingRequestWrapper requestWrapper, AccessLog accessLog) {
        // GET 요청의 경우 쿼리 스트링을 JSON 형식으로 저장
        if ("GET".equalsIgnoreCase(requestWrapper.getMethod())) {
            accessLog.setRequest(toJson(requestWrapper.getParameterMap())); // 쿼리 스트링을 JSON으로 변환
        }
        // POST 요청의 경우 요청 본문(Body)을 JSON 형식으로 저장
        else if ("POST".equalsIgnoreCase(requestWrapper.getMethod())) {

            byte[] content = requestWrapper.getContentAsByteArray();
            if (content.length > 0) {
                String requestBody = new String(content, StandardCharsets.UTF_8);
                accessLog.setRequest(requestBody); // 요청 본문 설정
            }
        }
    }

    /**
     * 응답 데이터를 ContentCachingResponseWrapper로 읽어와서 AccessLog에 저장하는 메서드
     *
     * @param responseWrapper ContentCachingResponseWrapper 객체
     * @param accessLog       AccessLog 엔티티
     */
    private void setResponseData(ContentCachingResponseWrapper responseWrapper, AccessLog accessLog) {
        // 응답 본문(Body)을 캐싱한 후 바이트 배열로 가져옴
        byte[] content = responseWrapper.getContentAsByteArray();

        // 응답 본문이 존재할 경우에만 처리
        if (content.length > 0) {

            // 바이트 배열을 문자열로 변환
            String responseBody = new String(content, StandardCharsets.UTF_8);

            // 응답 본문 설정
            accessLog.setResponse(responseBody);
        }
    }

    /**
     * 응답 후 AccessLog 설정 및 로그 기록
     */
    private void finalizeAccessLog(ContentCachingResponseWrapper responseWrapper, AccessLog accessLog) {
        // 요청 처리에 소요된 시간 계산 및 AccessLog 설정
        long elapsedTime = System.currentTimeMillis() - accessLog.getRequestAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        accessLog.setElapsed(elapsedTime);
        accessLog.setResponseAt(LocalDateTime.now());
        accessLog.setStatus(String.valueOf(responseWrapper.getStatus()));

        // 로그 출력
        log.info("AccessLog ==> {}", toJson(accessLog));

        // 응답 본문을 클라이언트로 다시 전달
        try {
            responseWrapper.copyBodyToResponse();
        } catch (IOException e) {
            log.error("Failed to copy response body to HttpServletResponse", e);
        }
    }

    private void sendAccessLogToExternalServer(AccessLog accessLog) {
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, toJson(accessLog));
            log.info("AccessLog가 MQ로 성공적으로 전송되었습니다.");
        } catch (Exception e) {
            log.error("AccessLog를 MQ로 보내지 못했습니다", e);
        }
    }
}