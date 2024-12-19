package com.leedae.mockaroo.repository;

import com.leedae.mockaroo.doamin.MockData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MockFieldRepository extends JpaRepository<MockData, Long> {
    Page<MockData> findByUserId(Long userId, Pageable pageable);
    List<MockData> findByUserIdOrderByCreatedAtDesc(Long userId);
    Optional<MockData> findByIdAndUserId(Long id, Long userId);
}