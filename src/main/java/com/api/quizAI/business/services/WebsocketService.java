package com.api.quizAI.business.services;

import com.api.quizAI.web.dto.UserScoreboardResponse;
import com.api.quizAI.web.payload.ScoreboardBroadcastResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebsocketService
{
    private final SimpMessagingTemplate messageBroker;

    public void broadcastScoreboardUpdate(UUID roomId, ScoreboardBroadcastResponse updateScoreboardResponse)
    {
        log.info("start scoreboard broadcast on room {} of user {}", roomId, updateScoreboardResponse.player().getId());

        messageBroker.convertAndSend("/topic/rooms/" + roomId + "/update-score", updateScoreboardResponse);

        log.info("successfully broadcast scoreboard on room {} of user {}", roomId, updateScoreboardResponse.player().getId());
    }

    public void broadcastPlayerJoinRoom(UUID roomId, UserScoreboardResponse playerScoreResponse)
    {
        log.info("start user {} join room {} broadcast", roomId, playerScoreResponse.player().getId());

        messageBroker.convertAndSend("/topic/rooms/" + roomId + "/join", playerScoreResponse);

        log.info("successfully broadcast player {} join on room {}", playerScoreResponse.player().getId(), roomId);

    }
}
