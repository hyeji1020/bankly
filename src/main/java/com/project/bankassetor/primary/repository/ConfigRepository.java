package com.project.bankassetor.primary.repository;

import com.project.bankassetor.primary.model.entity.Config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//@Repository
public interface ConfigRepository extends JpaRepository<Config, Long> {

    Optional<Config> findByCode(String code);
}
