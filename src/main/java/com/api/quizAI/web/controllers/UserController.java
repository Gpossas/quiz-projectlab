package com.api.quizAI.web.controllers;

import com.api.quizAI.business.services.UserService;
import com.api.quizAI.core.domain.User;
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
@RequestMapping(value = "/user", produces = "application/json")
@Slf4j
@RequiredArgsConstructor
public class UserController
{
    private final UserService userService;

    @Operation(summary = "Create user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = BadRequestExample.class)))
    })
    @PostMapping(consumes = "application/json")
    public ResponseEntity<User> createUser(@Valid @RequestBody UserRequestDTO userRequest)
    {
        log.info("starting user creation request");

        User user = userService.save(User.builder().username(userRequest.username()).build());

        log.info("successfully created user");

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ProblemDetailExample.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = BadRequestExample.class)))
    })
    @DeleteMapping(value = "/{id}", consumes = "application/json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("id") UUID userId)
    {
        log.info("starting user delete request");

        userService.delete(userId);

        log.info("successfully deleted user");
    }
}
