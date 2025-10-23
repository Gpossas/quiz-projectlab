package com.api.quizAI.web.controllers;

import com.api.quizAI.business.services.ScoreService;
import com.api.quizAI.business.services.WebsocketService;
import com.api.quizAI.core.domain.Score;
import com.api.quizAI.web.payload.*;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebsocketsController
{
    private final ScoreService scoreService;
    private final WebsocketService websocketService;

    @Operation(summary = "broadcast player scoreboard to others in room")
    @MessageMapping("/sendPlayerScore/{roomId}")
    public void broadcastPlayerScoreboard(@DestinationVariable UUID roomId, @Valid @Payload ScoreBroadcastRequestDTO playerRequest)
    {
        log.info("received request from client to broadcast player scoreboard in room {}", roomId);

        Score score = scoreService.findById(playerRequest.scoreId());

        log.info("retrieved score for user {} in room {}", score.getUser().getUsername(), roomId);

        websocketService.broadcastScoreboardUpdate(roomId, new ScoreboardBroadcastResponse(
                score.getId(),
                score.getUser(),
                playerRequest.pointsEarned()));
    }

    @Operation(summary = "broadcast player join to others in room")
    @MessageMapping("/sendPlayerJoin/{roomId}")
    public void broadcastPlayerJoin(@DestinationVariable UUID roomId, @Valid @Payload PlayerJoinRequest playerRequest)
    {
        log.info("received request from client to broadcast player join in room {}", roomId);

        Score score = scoreService.findById(playerRequest.scoreId());

        log.info("retrieved score for player {} in room {}", score.getUser().getUsername(), roomId);

        websocketService.broadcastPlayerJoinRoom(roomId, new UserScoreboardResponse(
                score.getId(),
                0,
                score.getUser()));
    }

    @Operation(summary = "broadcast player left to others in room")
    @MessageMapping("/sendPlayerExit/{roomId}")
    public void broadcastPlayerLeft(@DestinationVariable UUID roomId, @Valid @Payload PlayerLeftRequest playerRequest)
    {
        log.info("received request from client to broadcast player exit in room {}", roomId);

        Score score = scoreService.findById(playerRequest.scoreId());

        websocketService.broadcastPlayerLeftRoom(roomId, new PlayerLeftResponse(
                score.getId(),
                score.getUser()));
    }
}
