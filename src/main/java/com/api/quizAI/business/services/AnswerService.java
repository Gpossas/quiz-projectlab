package com.api.quizAI.business.services;

import com.api.quizAI.core.domain.Answer;
import com.api.quizAI.core.domain.Question;
import com.api.quizAI.core.domain.Room;
import com.api.quizAI.core.exceptions.AnswerNotFound;
import com.api.quizAI.core.exceptions.QuestionWasNotSentYet;
import com.api.quizAI.infra.repository.AnswerRepository;
import com.api.quizAI.web.dto.AnswerRequestDTO;
import com.api.quizAI.web.dto.CalculatePlayerScoreDTO;
import com.api.quizAI.web.dto.PlayerAnswerResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnswerService
{
    private final AnswerRepository answerRepository;
    private final QuestionService questionService;
    private final ScoreService scoreService;
    private final RoomService roomService;

    public Answer findById(UUID answerId)
    {
        log.info("searching for answer {}", answerId);

        Answer answer = answerRepository.findById(answerId).orElseThrow(() -> new AnswerNotFound(answerId));

        log.info("found answer {}", answerId);

        return answer;
    }

    public PlayerAnswerResponseDTO playerResponse(AnswerRequestDTO answerRequest)
    {
        Room room = roomService.findById(answerRequest.roomId());
        Question question = questionService.findById(answerRequest.questionId());
        checkQuestionWasSentByServer(question);
        Answer answer = findById(answerRequest.answerId());

        log.info("calculating player {} score in room {}", answerRequest.playerId(), answerRequest.roomId());
        Integer pointsEarned = scoreService.calculatePlayerScore(new CalculatePlayerScoreDTO(
                answerRequest.scoreId(),
                answerRequest.answerId(),
                room.getWaitTimeBetweenQuestions(),
                question.getSentAt(),
                answer.isCorrectAnswer()
        ));

        UUID correctAnswerId = pointsEarned > 0 ? answerRequest.answerId() : questionService.findCorrectAnswer(question);

        return new PlayerAnswerResponseDTO(
                pointsEarned,
                correctAnswerId,
                answerRequest.answerId()
        );
    }

    private void checkQuestionWasSentByServer(Question question)
    {
        if (question.getSentAt() == null)
        {
            log.error("player tried to answer a question not sent by the server {}", question.getId());
            throw new QuestionWasNotSentYet();
        }
    }
}
