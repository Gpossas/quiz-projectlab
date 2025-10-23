package com.api.quizAI.web.controllers;

import com.api.quizAI.business.services.ScoreService;
import com.api.quizAI.web.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/scores", produces = "application/json")
@RequiredArgsConstructor
@Slf4j
public class ScoreController
{
    private final ScoreService scoreService;


    @Operation(summary = "Calculate user answer points", description = "Return the points earned after answer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = ScoreResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ProblemDetailExample.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = BadRequestExample.class))),
    })
    @PatchMapping(value = "/{id}", consumes = "application/json")
    public ResponseEntity<ScoreResponseDTO> calculateAnswerScore(
            @PathVariable("id") UUID scoreId,
            @Valid @RequestBody AnswerRequestDTO answerRequest)
    {
        log.info("starting answer points calculation {}", scoreId);

        Integer pointsEarned = scoreService.calculatePlayerScore(scoreId, answerRequest);

        log.info("successfully calculate points {}", scoreId);

        return new ResponseEntity<>(new ScoreResponseDTO(pointsEarned), HttpStatus.OK);
    }
}
