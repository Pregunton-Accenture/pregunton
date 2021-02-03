package com.accenture.pregunton.controller;

import com.accenture.pojo.HitDto;
import com.accenture.pojo.PlayerDto;
import com.accenture.pojo.QuestionDto;
import com.accenture.pregunton.service.PlayerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@RestController
@RequestMapping("/players")
@Api(tags = "Player API", description = "This API has operations related to Player Controller")
public class PlayerController {

  @Autowired
  private PlayerService playerService;

  @PatchMapping("/v1.0/{playerId}/questions")
  @ApiOperation("Ask a question.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "OK"),
      @ApiResponse(code = 400, message = "Bad Request"),
      @ApiResponse(code = 404, message = "Not Found"),
      @ApiResponse(code = 500, message = "Internal Server Error"),
  })
  public ResponseEntity<QuestionDto> makeAQuestion(@PathVariable Long playerId, @RequestHeader String code,
                                                   String question) {
    QuestionDto questionDto = playerService.askQuestion(playerId, code, question);
    return ResponseEntity.ok(questionDto);
  }

  @PostMapping("/v1.0/{playerId}/guess")
  @ApiOperation("Make a guess.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "OK"),
      @ApiResponse(code = 400, message = "Bad Request"),
      @ApiResponse(code = 404, message = "Not Found"),
      @ApiResponse(code = 500, message = "Internal Server Error"),
  })
  public ResponseEntity<HitDto> makeAGuess(@PathVariable Long playerId, @RequestHeader String code, String guess) {
    HitDto playerGuess = playerService.makeAGuess(playerId, code, guess);
    return ResponseEntity.ok(playerGuess);
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
    return gameDto.map(ResponseEntity::ok)
        .orElseGet((() -> ResponseEntity.noContent()
            .build()));
  }

}
