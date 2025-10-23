package com.api.quizAI.business.services;

import com.api.quizAI.business.authorization.RoomAuthorization;
import com.api.quizAI.core.domain.Quiz;
import com.api.quizAI.core.domain.Room;
import com.api.quizAI.core.domain.User;
import com.api.quizAI.core.exceptions.PlayerAlreadyInOtherRoom;
import com.api.quizAI.core.exceptions.RoomNotFound;
import com.api.quizAI.infra.repository.RoomRepository;
import com.api.quizAI.web.dto.UpdateRoomDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class RoomService
{
    private final RoomRepository roomRepository;
    private final QuizService quizService;
    private final UserService userService;
    private final RoomAuthorization roomAuthorization;

    public Room save(Room room, UUID userId)
    {
        log.info("Saving room to database {}", room.getId());

        checkUserInOtherRoom(userId);
        User user = userService.findById(userId);
        room.setOwner(user);
        room = roomRepository.save(room);

        log.info("persisted room {} in database", room.getId());

        return room;
    }

    public Room findByCode(String roomCode)
    {
        log.info("searching for room {}", roomCode);

        Room room = roomRepository.findByRoomCode(roomCode).orElseThrow(() -> new RoomNotFound(roomCode));

        log.info("found room {}", roomCode);

        return room;
    }

    public Room findById(UUID roomId)
    {
        log.info("searching for room {}", roomId);

        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RoomNotFound(roomId.toString()));

        log.info("found room {}", roomId);

        return room;
    }

    public Set<Room> findRooms()
    {
        log.info("searching for rooms");

        Set<Room> rooms = roomRepository.findByIsPublicTrue();

        log.info("returning rooms");

        return rooms;
    }

    public void delete(UUID roomId, UUID userId)
    {
        log.info("deleting room {}", roomId);

        Room room = findById(roomId);
        User user = userService.findById(userId);
        roomAuthorization.verifyRoomOwner(room, user);

        roomRepository.delete(room);

        log.info("deleted room {}", roomId);
    }

    public Room update(UpdateRoomDTO updateRoomDTO, UUID roomId)
    {
        Room room = findById(roomId);

        User user = userService.findById(updateRoomDTO.ownerId());
        roomAuthorization.verifyRoomOwner(room, user);

        if (updateRoomDTO.isPublic() != null)
        {
            room.setIsPublic(updateRoomDTO.isPublic());
        }

        if (updateRoomDTO.maxNumberOfPlayers() != null)
        {
            room.setMaxNumberOfPlayers(updateRoomDTO.maxNumberOfPlayers());
        }

        if (updateRoomDTO.quizId() != null)
        {
            Quiz quiz = quizService.findById(updateRoomDTO.quizId());
            room.setQuiz(quiz);
        }

        room = roomRepository.save(room);
        return room;
    }

    private void checkUserInOtherRoom(UUID userId)
    {
        if (roomRepository.existsByOwnerId(userId))
        {
            throw new PlayerAlreadyInOtherRoom();
        }
    }
}
