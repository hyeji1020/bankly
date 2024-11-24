package com.project.bankassetor.filter;

import com.project.bankassetor.primary.model.entity.Config;
import com.project.bankassetor.secondary.model.entity.AccessLog;
import com.project.bankassetor.service.perist.ConfigService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;

import static com.project.bankassetor.utils.Utils.toJson;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccessLogFilter implements Filter {

    private final RabbitTemplate rabbitTemplate;
    private final ConfigService configService;
    private final Environment env;

    // RabbitMQ로 보낼 Exchange 이름과 Routing Key를 주입
    @Value("${mq.exchange}")
    private String exchange;

    @Value("${mq.routing-key}")
    private String routingKey;

    /**
     * AccessLog 필터링 및 로깅 처리를 담당하는 메서드.
     * "access" 프로파일이 활성화된 경우 필터를 건너뛰며,
     * `access-log.enabled` 설정에 따라 로깅 여부를 결정한다.
     *
     * @param request  클라이언트의 요청 객체
     * @param response 서버의 응답 객체
     * @param chain    다음 필터 또는 서블릿으로 요청을 전달하기 위한 필터 체인
     * @throws IOException   입출력 예외 발생 시
     * @throws ServletException 서블릿 예외 발생 시
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // 1. "access" 프로파일 활성화 여부를 확인하여 필터를 건너뛸지 결정한다.
        if (Arrays.asList(env.getActiveProfiles()).contains("access")) {
            // 다음 필터로 요청을 넘기고 현재 필터를 종료
            chain.doFilter(request, response);
            return;
        }

        // 2. runtime 시 설정된 `access-log.enabled` 값이 "off"인 경우 로깅을 비활성화한다.
        Config config = configService.getConfigInCache("access-log.enabled");
        if(config.getVal().equalsIgnoreCase("off")) {
            chain.doFilter(request, response);
            return;
        }

        // HTTP 요청의 URI를 확인하여 특정 경로로 시작하는 요청을 필터링
        final HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        if(httpServletRequest.getRequestURI().startsWith("/assets") || httpServletRequest.getRequestURI().startsWith("/custom") || httpServletRequest.getRequestURI().startsWith("/images")) {
            chain.doFilter(request, response);
            return;
        }

        // 3. HttpServletRequest와 HttpServletResponse를 감싸는 Wrapper로 변환하여 로그 데이터를 수집한다.
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper((HttpServletResponse) response);

        // 4. AccessLog 객체 생성 후, 요청 데이터를 설정하여 필터 체인을 호출한다.
        AccessLog accessLog = createAccessLog(requestWrapper);

        try {
            // 요청 데이터 설정
            setRequestData(requestWrapper, accessLog);
            chain.doFilter(requestWrapper, responseWrapper);
        } finally {
            // 응답 데이터 설정 및 로그 기록
            setResponseData(responseWrapper, accessLog);
            finalizeAccessLog(responseWrapper, accessLog);
        }

        // 5. 응답 완료 후 응답 데이터를 AccessLog 객체에 추가하고, RabbitMQ로 전송하여 로그를 저장한다.
        sendAccessLogToMQ(accessLog);

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

        return accessLog;
    }

    /**
     * 요청 데이터를 설정하는 메서드.
     * <p>
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

            // 응답 본문 설정, 5000 자까지만 들어가도록 자르기.
            accessLog.setResponse(truncateString(responseBody, 5000));
        }
    }

    /**
     * 주어진 문자열을 지정된 최대 길이로 잘라 반환하는 메서드입니다.
     *
     * @param input     자를 대상이 되는 입력 문자열 (null 가능)
     * @param maxLength 반환할 문자열의 최대 길이
     * @return 입력 문자열이 maxLength를 초과할 경우 잘린 문자열, 최대 길이 이내일 경우 원래 문자열,
     */
    public String truncateString(String input, int maxLength) {
        if (input == null) return null;
        return input.length() <= maxLength ? input : input.substring(0, maxLength);
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

    private void sendAccessLogToMQ(AccessLog accessLog) {
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, accessLog);
            log.info("AccessLog가 MQ로 성공적으로 전송되었습니다.");
        } catch (Exception e) {
            log.error("AccessLog를 MQ로 보내지 못했습니다", e);
        }
    }
}