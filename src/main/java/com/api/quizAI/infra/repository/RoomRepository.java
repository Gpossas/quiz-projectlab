package com.api.quizAI.infra.repository;

import com.api.quizAI.core.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface RoomRepository extends JpaRepository<Room, UUID>
{
    Optional<Room> findByRoomCode(String code);

    Set<Room> findByIsPublicTrue();

    Boolean existsByOwnerId(UUID userId);
}
