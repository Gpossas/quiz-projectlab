package com.api.quizAI.infra.config;

import com.api.quizAI.core.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleInvalidMethodArgument(MethodArgumentNotValidException exception)
    {
        List<HashMap<String, String>> errorMessages = new ArrayList<>();
        exception.getFieldErrors().forEach(error -> {
            HashMap<String, String> error_map = new HashMap<>();
            error_map.put("message", error.getDefaultMessage());
            errorMessages.add(error_map);
        });

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setDetail("Parâmetros com valores inválidos");
        problemDetail.setTitle("Parâmetro inválido");
        problemDetail.setProperty("errors", errorMessages);

        return problemDetail;
    }

    @ExceptionHandler(QuizNotFound.class)
    public ProblemDetail handleQuizNotFoundException(QuizNotFound exception)
    {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        problemDetail.setTitle("Quiz não encontrado");
        return problemDetail;
    }

    @ExceptionHandler(UnableToConvertJsonToObject.class)
    public ProblemDetail handleUnableToConvertJsonToObjectException(UnableToConvertJsonToObject exception)
    {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, exception.getMessage());
        problemDetail.setTitle("Erro ao receber JSON do Quiz");
        return problemDetail;
    }

    @ExceptionHandler(RoomNotFound.class)
    public ProblemDetail handleRoomNotFoundException(RoomNotFound exception)
    {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        problemDetail.setTitle("Sala não encontrada");
        return problemDetail;
    }

    @ExceptionHandler(UserNotFound.class)
    public ProblemDetail handleUserNotFoundException(UserNotFound exception)
    {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        problemDetail.setTitle("Usuário não encontrado");
        return problemDetail;
    }

    @ExceptionHandler(UserIsNotRoomOwner.class)
    public ProblemDetail handleUserIsNotRoomOwnerException(UserIsNotRoomOwner exception)
    {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, exception.getMessage());
        problemDetail.setTitle("Usuário não autorizado a realizar essa ação na sala");
        return problemDetail;
    }

    @ExceptionHandler(AnswerNotFound.class)
    public ProblemDetail handleAnswerNotFoundException(AnswerNotFound exception)
    {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        problemDetail.setTitle("Alternativa não encontrada");
        return problemDetail;
    }

    @ExceptionHandler(QuestionNotFound.class)
    public ProblemDetail handleQuestionNotFoundException(QuestionNotFound exception)
    {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        problemDetail.setTitle("Questão não encontrada");
        return problemDetail;
    }

    @ExceptionHandler(NoneCorrectAnswerFound.class)
    public ProblemDetail handleNoneCorrectAnswerFoundException(NoneCorrectAnswerFound exception)
    {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FAILED_DEPENDENCY, exception.getMessage());
        problemDetail.setTitle("Alternativa correta não encontrada");
        return problemDetail;
    }

    @ExceptionHandler(PlayerAlreadyInOtherRoom.class)
    public ProblemDetail handlePlayerAlreadyInOtherRoomException(PlayerAlreadyInOtherRoom exception)
    {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());
        problemDetail.setTitle("Jogador já está em outra sala");
        return problemDetail;
    }

    @ExceptionHandler(RoomIsFull.class)
    public ProblemDetail handleRoomIsFullException(RoomIsFull exception)
    {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());
        problemDetail.setTitle("A sala está lotada");
        return problemDetail;
    }
}
