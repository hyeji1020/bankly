package com.project.bankassetor.primary.repository;

import com.project.bankassetor.primary.model.entity.Member;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findMemberByEmail(String email);

    boolean existsMemberByEmail(String email);

    @Query(value = "SELECT m.* FROM member m JOIN checking_account ca ON m.id = ca.memberId WHERE ca.accountId = :accountId ", nativeQuery = true)
    Optional<Member> findCheckByAccountId(@Param("accountId") long accountId);

    @Query(value = "SELECT m.* FROM member m JOIN saving_account sa ON m.id = sa.memberId WHERE sa.accountId = :accountId ", nativeQuery = true)
    Optional<Member> findSaveByAccountId(@Param("accountId") long accountId);
}
