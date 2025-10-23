package com.api.quizAI.web.dto;

import com.api.quizAI.core.domain.Room;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record PublicRoomsResponseDTO(
        UUID id,
        String roomCode,
        Integer maxNumberOfPlayers,
        String quizTopic,
        String ownerName
) {
    public static Set<PublicRoomsResponseDTO> fromDomainToDTO(Set<Room> rooms)
    {
        return rooms.stream().map(room -> new PublicRoomsResponseDTO(
                room.getId(),
                room.getRoomCode(),
                room.getMaxNumberOfPlayers(),
                (room.getQuiz() != null) ? room.getQuiz().getTopic() : "Tópico Não Definido",
                room.getOwner().getUsername()
        )).collect(Collectors.toSet());
    }
}
