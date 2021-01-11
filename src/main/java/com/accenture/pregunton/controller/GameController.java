package com.accenture.pregunton.controller;

import com.accenture.pregunton.model.Game;
import com.accenture.pregunton.service.GameService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping("/")
    @ApiOperation("Creates a game.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "created"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    public ResponseEntity<Game> createGame(@RequestHeader Long masterId) {
        Game game = gameService.create(masterId);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/")
                .build()
                .toUri();
        return ResponseEntity.created(location).body(game);
    }

}
