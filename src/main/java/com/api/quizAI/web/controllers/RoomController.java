package com.api.quizAI.web.controllers;

import com.api.quizAI.business.services.RoomService;
import com.api.quizAI.business.services.ScoreService;
import com.api.quizAI.core.domain.Room;
import com.api.quizAI.core.domain.Score;
import com.api.quizAI.web.dto.*;
import com.api.quizAI.web.payload.UserScoreboardResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping(value = "/rooms", produces = "application/json")
@RequiredArgsConstructor
@Slf4j
public class RoomController
{
    private final RoomService roomService;
    private final ScoreService scoreService;

    @Operation(summary = "Create room", description = "Player create a room to play the quiz")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(implementation = RoomCreationResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ProblemDetailExample.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = BadRequestExample.class))),
    })
    @PostMapping(consumes = "application/json")
    @Transactional
    public ResponseEntity<RoomCreationResponseDTO> createRoom(@Valid @RequestBody RoomRequestDTO roomRequestDTO)
    {
        log.info("starting room creation");

        Room room = roomService.save(Room.builder()
                .isPublic(roomRequestDTO.isPublic())
                .maxNumberOfPlayers(roomRequestDTO.maxNumberOfPlayersInRoom()).build(),
                roomRequestDTO.ownerId());

        Score score = scoreService.save(new CreateScoreRequestDTO(roomRequestDTO.ownerId(), room.getId()));

        log.info("successfully created room {}", room.getRoomCode());

        return new ResponseEntity<>(new RoomCreationResponseDTO(
                        room.getId(),
                        room.getRoomCode(),
                        room.getIsPublic(),
                        roomRequestDTO.maxNumberOfPlayersInRoom(),
                        room.getOwner(),
                        new PlayerScoreDTO(score.getId(), 0)),
                HttpStatus.CREATED);
    }

    @Operation(summary = "Join room", description = "join a player to a room")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(implementation = JoinRoomResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ProblemDetailExample.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = BadRequestExample.class))),
    })
    @PostMapping(value = "/join/{roomCode}", consumes = "application/json")
    public ResponseEntity<JoinRoomResponseDTO> joinRoom(
            @PathVariable(value = "roomCode") String roomCode,
            @Valid @RequestBody JoinRoomRequestDTO joinRoomRequest)
    {
        log.info("starting request to join player {} in room {}", joinRoomRequest.userId(), roomCode);

        Room room = roomService.findByCode(roomCode);
        //todo: check room capacity before allowing user join room
        List<Score> playersScoreOrdered = scoreService.findUsersScoreboardOrderedByScore(room.getId());
        Score score = scoreService.save(new CreateScoreRequestDTO(joinRoomRequest.userId(), room.getId()));

        log.info("successfully joined player {} in room {}", joinRoomRequest.userId(), roomCode);

        return new ResponseEntity<>(new JoinRoomResponseDTO(
                room.getId(),
                roomCode,
                room.getIsPublic(),
                room.getMaxNumberOfPlayers(),
                room.getOwner(),
                new PlayerScoreDTO(score.getId(), 0),
                playersScoreOrdered.stream().map(playerScoreboard -> new UserScoreboardResponse(playerScoreboard.getId(), playerScoreboard.getScore(), playerScoreboard.getUser())).toList()),
                HttpStatus.CREATED);
    }


    @Operation(summary = "Exit room", description = "Exit a player of a room by deleting its scoreboard")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ProblemDetailExample.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = BadRequestExample.class))),
    })
    @DeleteMapping(value = "/exit/{scoreId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void exitRoom(@PathVariable("scoreId") UUID scoreId)
    {
        log.info("starting player exit room request {}", scoreId);

        scoreService.delete(scoreId);

        log.info("successfully exit room request {}", scoreId);
    }


    @Operation(summary = "Delete room")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ProblemDetailExample.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = BadRequestExample.class))),
    })
    @DeleteMapping(value = "/{id}", consumes = "application/json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRoom(@PathVariable("id") UUID roomId, @Valid @RequestBody UserIdRequestDTO userIdRequest)
    {
        log.info("starting room delete request {}", roomId);

        scoreService.deleteAllScoresInRoom(roomId);
        roomService.delete(roomId, userIdRequest.userId());

        log.info("successfully deleted room {}", roomId);
    }


    @Operation(summary = "Update room")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ProblemDetailExample.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = BadRequestExample.class))),
    })
    @PatchMapping(value = "/{id}", consumes = "application/json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateRoom(
            @PathVariable("id") UUID roomId,
            @Valid @RequestBody UpdateRoomDTO updateRoomDTO)
    {
        log.info("starting room update request {}", roomId);

        roomService.update(updateRoomDTO, roomId);

        log.info("successfully updated room {}", roomId);
    }

    @Operation(summary = "Find public rooms")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = @Content(array = @ArraySchema(schema = @Schema(implementation = PublicRoomsResponseDTO.class)))),
    })
    @GetMapping
    public ResponseEntity<Set<PublicRoomsResponseDTO>> findPublicRooms()
    {
        log.info("starting find public rooms request");

        Set<Room> publicRooms = roomService.findRooms();

        log.info("successfully found {} rooms", publicRooms.size());

        return new ResponseEntity<>(PublicRoomsResponseDTO.fromDomainToDTO(publicRooms), HttpStatus.OK);
    }
}
