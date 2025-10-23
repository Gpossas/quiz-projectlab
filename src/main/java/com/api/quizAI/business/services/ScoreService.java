package com.api.quizAI.business.services;

import com.api.quizAI.business.factories.ScoreFactory;
import com.api.quizAI.core.domain.Answer;
import com.api.quizAI.core.domain.Question;
import com.api.quizAI.core.domain.Score;
import com.api.quizAI.core.domain.User;
import com.api.quizAI.core.exceptions.ScoreNotFound;
import com.api.quizAI.infra.repository.ScoreRepository;
import com.api.quizAI.web.dto.AnswerRequestDTO;
import com.api.quizAI.web.dto.CalculatePlayerScoreDTO;
import com.api.quizAI.web.dto.CreateScoreRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScoreService
{
    private final ScoreRepository scoreRepository;
    private final UserService userService;
    private final ScoreFactory scoreFactory;

    public Score save(CreateScoreRequestDTO scoreboardRequest)
    {
        User user = userService.findById(scoreboardRequest.userId());
        Score score = new Score(0, user, scoreboardRequest.roomId());

        log.info("saving score to database {} of user {}", score.getId(), user.getId());

        score = scoreRepository.save(score);

        log.info("persisted score {} in database of user {}", score.getId(), user.getId());

        return score;
    }

    public Score findById(UUID scoreId)
    {
        log.info("searching for score {}", scoreId);

        Score score = scoreRepository.findById(scoreId).orElseThrow(() -> new ScoreNotFound(scoreId));

        log.info("found score {}", scoreId);

        return score;
    }

    public void delete(UUID scoreId)
    {
        log.info("deleting score {}", scoreId);

        Score score = findById(scoreId);
        scoreRepository.delete(score);

        log.info("deleted score {}", scoreId);
    }

    public Integer calculatePlayerScore(CalculatePlayerScoreDTO dto)
    {
        int pointsEarned = dto.isCorrectAnswer() ? scoreFactory.getBonus(dto.questionWasSentAt(), dto.totalTimeToAnswerQuestionInSeconds()) : -2;

        Score score = findById(dto.scoreId());
        score.addScore(pointsEarned);
        scoreRepository.save(score);

        return pointsEarned;
    }

    public List<Score> findUsersScoreboardOrderedByScore(UUID roomId)
    {
        log.info("searching all scores in room {}", roomId);

        Set<Score> scores = scoreRepository.findByRoomId(roomId);

        List<Score> scoresOrdered = new ArrayList<>(scores.stream().toList());
        scoresOrdered.sort(Comparator.comparingInt(Score::getScore).reversed());

        log.info("got all scores in room ordered {}", roomId);

        return scoresOrdered;
    }

}
