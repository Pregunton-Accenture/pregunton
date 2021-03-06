package com.accenture.pregunton.controller;

import com.accenture.model.Game;
import com.accenture.pojo.GameDto;
import com.accenture.pojo.PlayerDto;
import com.accenture.pojo.QuestionDto;
import com.accenture.pregunton.service.GameService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@Validated
@Api(tags = "Game API", description = "This API has operations related to Game Controller")
@RequestMapping("/games")
public class GameController {

  @Autowired
  private GameService gameService;

  @PostMapping(value = "/v1.0")
  @ApiOperation("Creates a game.")
  @ApiResponses(value = {
      @ApiResponse(code = 201, message = "created"),
      @ApiResponse(code = 400, message = "Bad Request"),
      @ApiResponse(code = 500, message = "Internal Server Error"),
  })
  public ResponseEntity<String> createGame(@RequestBody GameDto gameDto, @RequestHeader String master,
                                           @RequestHeader Long categoryId) {
    Game game = gameService.create(gameDto, master, categoryId);
    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/")
        .build()
        .toUri();
    return ResponseEntity.created(location)
        .body(game.getCode());
  }

  @DeleteMapping("/v1.0/{gameId}")
  @ApiOperation("Delete a game.")
  @ApiResponses(value = {
      @ApiResponse(code = 204, message = "No Content"),
      @ApiResponse(code = 400, message = "Bad Request"),
      @ApiResponse(code = 404, message = "Not Found"),
      @ApiResponse(code = 500, message = "Internal Server Error"),
  })
  public ResponseEntity<Void> deleteGame(@NotNull @PathVariable Long gameId) {
    gameService.delete(gameId);
    return ResponseEntity.noContent()
        .build();
  }

  @GetMapping(value = "/v1.0/{gameId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation("Obtain an specific game.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "OK"),
      @ApiResponse(code = 400, message = "Bad Request"),
      @ApiResponse(code = 404, message = "Not Found"),
      @ApiResponse(code = 500, message = "Internal Server Error"),
  })
  @ResponseBody
  public ResponseEntity<GameDto> obtainGame(@NotNull @PathVariable Long gameId) {
    Optional<GameDto> gameDto = gameService.getOne(gameId);
    return gameDto.map(ResponseEntity::ok)
        .orElseGet((() -> ResponseEntity.noContent()
            .build()));
  }

  @PatchMapping("/v1.0/{gameCode}/players")
  @ApiOperation("Add player to existing game.")
  @ApiResponses(value = {
      @ApiResponse(code = 204, message = "No Content"),
      @ApiResponse(code = 400, message = "Bad Request"),
      @ApiResponse(code = 404, message = "Not Found"),
      @ApiResponse(code = 500, message = "Internal Server Error"),
  })
  public ResponseEntity<Void> addPlayer(@NotNull @PathVariable String gameCode, @RequestBody PlayerDto playerDto) {
    gameService.addOnePlayer(gameCode, playerDto);
    return ResponseEntity.noContent()
        .build();
  }

  @GetMapping(value = "/v1.0/{code}/questions", produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation("get the questions of a specific game or get only the questions that are not answered yet.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "OK"),
      @ApiResponse(code = 204, message = "No content"),
      @ApiResponse(code = 400, message = "Bad Request"),
      @ApiResponse(code = 404, message = "Not Found"),
      @ApiResponse(code = 500, message = "Internal Server Error"),
  })
  @ResponseBody
  public ResponseEntity<List<QuestionDto>> getGameQuestions(@PathVariable String code,
                                                            @RequestParam(name = "all") Boolean withAllQuestion) {
    List<QuestionDto> questions = gameService.obtainQuestions(code, withAllQuestion);
    return questions.isEmpty() ? ResponseEntity.noContent()
        .build() : ResponseEntity.ok(questions);
  }

}
