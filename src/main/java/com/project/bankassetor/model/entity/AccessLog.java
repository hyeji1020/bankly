package com.project.bankassetor.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "access_log")
public class AccessLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    private String memberId;
    private String email;
    private String threadId;
    private String host;
    private String authorization;
    private String method;
    private String uri;
    private String service;
    private String os;
    private String deviceClass;
    private String agentName;
    private String agentClass;
    private String clientIp;
    private String country;
    private String city;
    private long elapsed;
    private String request;
    private String response;
    private String status;
    private String deviceName;
    private String osName;
    private String osVersion;
    private String userAgent;
    private String referer;
    private String errorId;
    private LocalDateTime requestAt;
    private LocalDateTime responseAt;
    private String requestId;

    // 새로운 생성자 (테스트에서 사용)
    public AccessLog(String memberId, String status, String uri) {
        this.memberId = memberId;
        this.status = status;
        this.uri = uri;
        this.requestAt = LocalDateTime.now(); // 요청 시각을 현재 시각으로 설정
    }
}
