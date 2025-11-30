package com.api.quizAI.business.services;

import com.api.quizAI.core.domain.Answer;
import com.api.quizAI.core.domain.Question;
import com.api.quizAI.core.exceptions.NoneCorrectAnswerFound;
import com.api.quizAI.core.exceptions.QuestionNotFound;
import com.api.quizAI.infra.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionService
{
    private final QuestionRepository questionRepository;

    private Question save(Question question)
    {
        log.info("Saving question to database {}", question.getId());

        question = questionRepository.save(question);

        log.info("persisted question to database {}", question.getId());

        return question;
    }

    public Question findById(UUID questionId)
    {
        log.info("searching for question {}", questionId);

        Question question = questionRepository.findById(questionId).orElseThrow(() -> new QuestionNotFound(questionId));

        log.info("found question {}", questionId);

        return question;
    }

    public UUID findCorrectAnswer(Question question)
    {
        log.info("checking correct answer for question {}", question.getId());

        for (Answer answer: question.getAnswers())
        {
            if (answer.isCorrectAnswer())
            {
                log.info("found correct answer for question {}", question.getId());
                return answer.getId();
            }
        }

        throw new NoneCorrectAnswerFound();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void setTimeSentToMatch(UUID questionId)
    {
        log.info("setting time sent for question {}", questionId);

        Question question = findById(questionId);
        question.setSentAt(OffsetDateTime.now());
        save(question);

        log.info("finished time sent for question");
    }
}
