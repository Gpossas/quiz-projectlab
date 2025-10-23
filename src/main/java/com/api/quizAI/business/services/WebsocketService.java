package com.api.quizAI.business.services;

import com.api.quizAI.core.domain.Question;
import com.api.quizAI.core.domain.Quiz;
import com.api.quizAI.core.domain.Room;
import com.api.quizAI.web.dto.QuestionDTO;
import com.api.quizAI.web.payload.CountdownResponseDTO;
import com.api.quizAI.web.payload.ScoreboardBroadcastResponse;
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

    public void broadcastScoreboardUpdate(UUID roomId, ScoreboardBroadcastResponse updateScoreboardResponse)
    {
        log.info("start scoreboard broadcast on room {} of user {}", roomId, updateScoreboardResponse.player().getId());

        messageBroker.convertAndSend("/topic/rooms/" + roomId + "/update-score", updateScoreboardResponse);

        log.info("successfully broadcast scoreboard on room {} of user {}", roomId, updateScoreboardResponse.player().getId());
    }

    public void startMatch(Room room)
    {
        log.info("start match request initiated for room {}", room.getId());

        Quiz quiz = roomService.findById(room.getId()).getQuiz();

        log.info("quiz of room {} found, starting match countdown", room.getId());

        broadcastCountdownTo("/start-match-countdown", 5, room.getId());

        log.info("match started!");
        for (Question question: quiz.getQuestions())
        {
            questionService.setTimeSentToMatch(question);
            messageBroker.convertAndSend("/topic/rooms/" + room.getId() + "/question", QuestionDTO.domainToDTO(question));
            log.info("question {} sent to users in room {}", question.getId(), room.getId());

            broadcastCountdownTo("/question-countdown", room.getWaitTimeBetweenQuestions(), room.getId());
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
