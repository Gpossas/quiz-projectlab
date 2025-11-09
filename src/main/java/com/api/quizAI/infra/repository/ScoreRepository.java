package com.api.quizAI.infra.repository;

import com.api.quizAI.core.domain.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface ScoreRepository extends JpaRepository<Score, UUID>
{
    Set<Score> findByRoomId(UUID roomId);

    Boolean existsByUserId(UUID userId);

    int countByRoomId(UUID roomId);
}
