package com.project.bankassetor.filter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.project.bankassetor.filter.response.LocationResponse;
import com.project.bankassetor.model.entity.AccessLog;
import com.project.bankassetor.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

public class AccessLogUtil {

    // User-Agent 파서 초기화
    private static final UserAgentAnalyzer userAgentAnalyzer = UserAgentAnalyzer
            .newBuilder()
            .hideMatcherLoadStats() // 로딩 중 통계 정보 숨기기
            .withCache(50_000)      // 캐시 크기 설정
            .withField(UserAgent.AGENT_NAME)
            .withField(UserAgent.OPERATING_SYSTEM_NAME)
            .withField(UserAgent.OPERATING_SYSTEM_CLASS)
            .withField(UserAgent.DEVICE_CLASS)
            .build();

    /**
     * User-Agent 문자열로부터 정보를 파싱하여 AccessLog의 필드를 설정합니다.
     *
     * @param userAgentString User-Agent 문자열
     * @param accessLog       AccessLog 객체
     */
    public static void getUserAgent(String userAgentString, AccessLog accessLog) {
        UserAgent userAgent = userAgentAnalyzer.parse(userAgentString);

        // User-Agent로부터 파싱한 정보 설정
        accessLog.setAgentName(userAgent.getValue(UserAgent.AGENT_NAME));
        accessLog.setOsName(userAgent.getValue(UserAgent.OPERATING_SYSTEM_NAME));
        accessLog.setDeviceClass(userAgent.getValue(UserAgent.DEVICE_CLASS));
        accessLog.setAgentClass(userAgent.getValue(UserAgent.OPERATING_SYSTEM_CLASS));
        accessLog.setOsVersion(userAgent.getValue(UserAgent.OPERATING_SYSTEM_VERSION));
        accessLog.setDeviceName(userAgent.getValue(UserAgent.DEVICE_NAME));
    }

    /**
     * 에러 ID를 생성하여 반환하는 메서드
     *
     * @return UUID 기반의 에러 ID 문자열
     */
    public static String generateErrorId() {
        return UUID.randomUUID().toString();
    }

    /**
     * 클라이언트의 IP 주소를 가져오는 메서드
     *
     * @param request HttpServletRequest 객체
     * @return 클라이언트 IP 주소
     */
    public static String getClientIp(HttpServletRequest request) {
        String[] headers = {"X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_X_FORWARDED_FOR", "X-Real-IP"};
        String ip = null;

        // 1. IP 주소 확인 (여러 헤더에서 확인)
        for (String header : headers) {
            ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                ip = ip.split(",")[0].trim();  // 여러 IP가 있을 경우 첫 번째 IP 사용
                break;
            }
        }

        // 2. 헤더에 없을 경우 기본 IP 가져오기
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

    /**
     * IP 주소를 기반으로 국가 및 도시 정보를 가져오는 메서드
     *
     * @param ip IP 주소
     * @return 국가 및 도시 정보를 포함하는 LocationResponse 객체
     */
    public static LocationResponse getLocationInfoByIp(String ip) {
        String apiUrl = "http://ip-api.com/json/" + ip;

        try {
            // 1. URL 객체 생성 및 연결 설정
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // 2. HTTP 응답 코드 확인
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) { // 응답 코드가 200(성공)일 경우
                // 3. 응답 데이터 읽어오기
                String responseJson = new String(connection.getInputStream().readAllBytes());

                // 4. JSON 문자열을 Map으로 변환
                Map<String, Object> responseMap = Utils.toObject(responseJson, new TypeReference<Map<String, Object>>() {
                });

                // 5. country와 city 정보를 Map에서 추출하여 LocationResponse 객체로 변환
                String country = responseMap.getOrDefault("country", "Unknown").toString();
                String city = responseMap.getOrDefault("city", "Unknown").toString();

                return new LocationResponse(country, city); // LocationResponse 객체 반환
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 예외 발생 시 기본값으로 LocationResponse 반환
        return new LocationResponse("Unknown", "Unknown");
    }
}

