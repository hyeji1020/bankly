package com.project.bankassetor.secondary.repository;

import com.project.bankassetor.secondary.model.entity.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {

    @Query(
            value = """
        SELECT 
          DATE(requestAt) AS log_date,
          uri,
          COUNT(*) AS total_requests,
          SUM(CASE WHEN userAgent LIKE '%bot%' OR userAgent LIKE '%crawl%' OR userAgent LIKE '%spider%' OR userAgent LIKE '%python-requests%' OR userAgent LIKE '%curl%' THEN 1 ELSE 0 END) AS crawler_requests,
          SUM(CASE WHEN userAgent LIKE '%bot%' OR userAgent LIKE '%crawl%' OR userAgent LIKE '%spider%' OR userAgent LIKE '%python-requests%' OR userAgent LIKE '%curl%' THEN 0 ELSE 1 END) AS user_requests
        FROM access_log
        GROUP BY DATE(requestAt), uri
        ORDER BY log_date DESC, total_requests DESC
        """,
            nativeQuery = true)
    List<Object[]> getAccessLogSummaryRaw();

    @Query(
            value = """
    SELECT 
      uri,
      AVG(elapsed) AS avg_response_time,
      MAX(elapsed) AS max_response_time
    FROM access_log
    GROUP BY uri
    ORDER BY avg_response_time DESC
    """,
            nativeQuery = true)
    List<Object[]> getResponseTimeSummaryRaw();

    @Query(
            value = """
    SELECT 
      HOUR(STR_TO_DATE(SUBSTRING_INDEX(requestAt, '.', 1), '%Y-%m-%d %H:%i:%s')) AS hour,
      COUNT(*) AS count
    FROM access_log
    GROUP BY hour
    ORDER BY hour
    """,
            nativeQuery = true)
    List<Object[]> getTrafficTrendRaw();

}
