package com.accenture.pregunton.controller;

import com.accenture.pregunton.pojo.GameDto;
import com.accenture.pregunton.pojo.PlayerDto;
import com.accenture.pregunton.service.GameService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.Optional;

@RestController
@Validated
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

    @DeleteMapping("/{gameId}")
    @ApiOperation("Delete a game.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    public ResponseEntity<Void> deleteGame(@NotNull @PathVariable Long gameId) {
        gameService.delete(gameId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{gameId}")
    @ApiOperation("Obtain an specific game.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    public ResponseEntity<GameDto> obtainGame(@NotNull @PathVariable Long gameId) {
        Optional<GameDto> gameDto = gameService.getOne(gameId);
        return gameDto.map(ResponseEntity::ok).orElseGet((() -> ResponseEntity.noContent().build()));
    }

    @PutMapping("/{gameId}")
    @ApiOperation("Add player to existing game.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    public ResponseEntity<Void> addPlayer(@NotNull @PathVariable Long gameId, @RequestBody PlayerDto playerDto) {
        gameService.addOnePlayer(gameId, playerDto);
        return ResponseEntity.noContent().build();
    }

}
