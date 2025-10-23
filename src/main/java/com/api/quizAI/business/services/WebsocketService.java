package com.api.quizAI.business.services;

import com.api.quizAI.business.authorization.RoomAuthorization;
import com.api.quizAI.core.domain.Question;
import com.api.quizAI.core.domain.Room;
import com.api.quizAI.core.domain.User;
import com.api.quizAI.core.exceptions.MatchCanNotStartWithoutQuiz;
import com.api.quizAI.web.dto.QuestionDTO;
import com.api.quizAI.web.payload.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebsocketService
{
    private final SimpMessagingTemplate messageBroker;
    private final RoomService roomService;
    private final QuestionService questionService;
    private final UserService userService;
    private final RoomAuthorization roomAuthorization;

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

    public void broadcastPlayerLeftRoom(UUID roomId, PlayerLeftResponse playerLeftResponse)
    {
        log.info("start user {} left room {} broadcast", roomId, playerLeftResponse.player().getId());

        messageBroker.convertAndSend("/topic/rooms/" + roomId + "/exit", playerLeftResponse);

        log.info("successfully broadcast player {} left on room {}", playerLeftResponse.player().getId(), roomId);
    }

    public void startMatch(StartMatchRequest matchRequest)
    {
        log.info("start match request initiated for room {}", matchRequest.roomId());

        Room room = roomService.findById(matchRequest.roomId());
        User user = userService.findById(matchRequest.playerId());
        roomAuthorization.verifyRoomOwner(room, user);

        if (room.getQuiz() == null)
        {
            throw new MatchCanNotStartWithoutQuiz();
        }

        log.info("quiz of room {} found, starting match countdown", matchRequest.roomId());

        broadcastCountdownTo("/start-match-countdown", 5, matchRequest.roomId());

        log.info("match started!");
        for (Question question: room.getQuiz().getQuestions())
        {
            questionService.setTimeSentToMatch(question);
            messageBroker.convertAndSend("/topic/rooms/" + matchRequest.roomId() + "/question", QuestionDTO.domainToDTO(question));
            log.info("question {} sent to users in room {}", question.getId(), matchRequest.roomId());

            broadcastCountdownTo("/question-countdown", room.getWaitTimeBetweenQuestions(), matchRequest.roomId());
        }

        log.info("match finished");
    }

    private void broadcastCountdownTo(String uri, int startCount, UUID roomId)
    {
        for (int seconds = startCount; seconds > 0; seconds--)
        {
            messageBroker.convertAndSend("/topic/rooms/" + roomId + uri, new CountdownResponseDTO(seconds));
            log.info("room {} countdown {}...", roomId, seconds);
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException ignored) {}
        }
    }
}
