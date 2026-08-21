package com.example.bankcards.repository;

import com.example.bankcards.entity.Transfer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface TransferRepository extends JpaRepository<Transfer, Long> {

    @Query("""
        SELECT t
        FROM Transfer t
        WHERE (t.fromCard.id = :cardId OR t.toCard.id = :cardId)
        AND t.createdAt >= :from
        AND t.createdAt < :to
        """)
    Page<Transfer> findStatement(@Param("cardId") Long cardId, @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to, Pageable pageable);
}