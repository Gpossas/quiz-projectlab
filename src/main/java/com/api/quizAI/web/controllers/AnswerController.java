package com.api.quizAI.web.controllers;

import com.api.quizAI.business.services.AnswerService;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = "application/json")
@RequiredArgsConstructor
@Slf4j
public class AnswerController
{
    private final AnswerService answerService;

    @Operation(summary = "Process player answer in a question",
            description = """
            - Return the id of the correct answer for the question
            - Return the id of the answer chosen by the player
            - Return the points earned
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = CorrectAnswerIdDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ProblemDetailExample.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = BadRequestExample.class))),
            @ApiResponse(responseCode = "424", description = "Failed Dependency", content = @Content(schema = @Schema(implementation = BadRequestExample.class)))
    })
    @PatchMapping(consumes = "application/json")
    public ResponseEntity<PlayerAnswerResponseDTO> playerAnswerResponse(@Valid @RequestBody AnswerRequestDTO answerRequest)
    {
        log.info("starting player {} answer request for room {}", answerRequest.playerId(), answerRequest.roomId());

        PlayerAnswerResponseDTO response = answerService.playerResponse(answerRequest);

        log.info("successfully finished player {} answer request for room {}", answerRequest.playerId(), answerRequest.roomId());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
