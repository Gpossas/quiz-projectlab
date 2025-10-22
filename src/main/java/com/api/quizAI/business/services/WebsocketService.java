package com.api.quizAI.business.services;

import com.api.quizAI.web.payload.ScoreboardResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
public class WebsocketService
{
    private final SimpMessagingTemplate messageBroker;

    public void broadcastScoreboardUpdate(UUID roomId, ScoreboardResponse updateScoreboardResponse)
    {
        log.info("start scoreboard broadcast on room {} of user {}", roomId, updateScoreboardResponse.player().getId());

        messageBroker.convertAndSend("/topic/rooms/" + roomId + "/update-score", updateScoreboardResponse);

        log.info("successfully broadcast scoreboard on room {} of user {}", roomId, updateScoreboardResponse.player().getId());
    }
}
