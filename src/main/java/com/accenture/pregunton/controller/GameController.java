package com.accenture.pregunton.controller;

import com.accenture.pregunton.pojo.GameDto;
import com.accenture.pregunton.service.GameService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/pregunton")
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
    public ResponseEntity<Object> createGame(@RequestBody GameDto gameDto, @RequestHeader Long masterId) {
        gameService.create(gameDto, masterId);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/")
                .build()
                .toUri();
        return ResponseEntity.created(location).body(gameDto);
    }

}
