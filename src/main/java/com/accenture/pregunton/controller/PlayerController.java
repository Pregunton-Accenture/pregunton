package com.accenture.pregunton.controller;

import com.accenture.pregunton.pojo.PlayerDto;
import com.accenture.pregunton.pojo.QuestionDto;
import com.accenture.pregunton.service.PlayerService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@RestController
@RequestMapping("/player")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @PatchMapping("/v1.0")
    @ApiOperation("Ask a question.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    public ResponseEntity<Object> makeAQuestion(@RequestHeader Long playerId, @RequestHeader String code, String question) {
        QuestionDto questionDto = playerService.askQuestion(playerId, code, question);
        return ResponseEntity.ok(questionDto);
    }

    @GetMapping(value = "/v1.0/{playerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Search player.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    public ResponseEntity<PlayerDto> obtainPlayer(@NotNull @PathVariable Long playerId) {
        Optional<PlayerDto> gameDto = playerService.getPlayer(playerId);
        return gameDto.map(ResponseEntity::ok).orElseGet((() -> ResponseEntity.noContent().build()));
    }

}
