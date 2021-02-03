package com.accenture.pregunton.service;

import com.accenture.model.Game;
import com.accenture.model.Hit;
import com.accenture.model.Player;
import com.accenture.model.Question;
import com.accenture.pojo.Answer;
import com.accenture.pojo.HitDto;
import com.accenture.pojo.PlayerDto;
import com.accenture.pojo.QuestionDto;
import com.accenture.pregunton.exception.GameNotFoundException;
import com.accenture.pregunton.exception.GameOverException;
import com.accenture.pregunton.exception.LastQuestionNotAnswerException;
import com.accenture.pregunton.exception.PlayerNotFoundException;
import com.accenture.pregunton.repository.GameRepository;
import com.accenture.pregunton.repository.HitRepository;
import com.accenture.pregunton.repository.PlayerRepository;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlayerService {

  @Autowired
  private PlayerRepository playerRepository;
  @Autowired
  private GameRepository gameRepository;
  @Autowired
  private HitRepository hitRepository;
  @Autowired
  private ModelMapper mapper;

  private static final String GAME_NOT_FOUND = "Game not found with code: ";
  private static final String PLAYER_NOT_FOUND = "Player not found with id: ";
  private static final String PLAYER_ALREADY_LOSE = "This player already lose.";

  public QuestionDto askQuestion(Long playerId, String gameCode, String playerQuestion) {
    Player player = playerRepository.findById(playerId)
        .orElseThrow(() -> new PlayerNotFoundException(PLAYER_NOT_FOUND + playerId));
    Game game = gameRepository.findByCode(gameCode)
        .orElseThrow(() -> new GameNotFoundException(GAME_NOT_FOUND + gameCode));

    if (checkIfPlayerQuestionHasBeenResponded(player, game)) {
      throw new LastQuestionNotAnswerException("La ultima pregunta del jugador todavia no ha sido respondida.");
    }

    Question question = Question.builder()
        .question(playerQuestion)
        .answer(Answer.SIN_RESPUESTA)
        .published(LocalDateTime.now())
        .player(player)
        .build();

    playerRepository.save(player);
    saveGameQuestion(game, question);

    return mapper.map(question, QuestionDto.class);
  }

  public HitDto makeAGuess(Long playerId, String gameCode, String guess) {
    Player player = playerRepository.findById(playerId)
        .orElseThrow(() -> new PlayerNotFoundException(PLAYER_NOT_FOUND + playerId));
    Game game = gameRepository.findByCode(gameCode)
        .orElseThrow(() -> new GameNotFoundException(GAME_NOT_FOUND + gameCode));

    checkIfPlayerAlreadyLose(player);

    Hit hit = Hit.builder()
        .guess(guess)
        .published(LocalDateTime.now())
        .player(player)
        .build();

    hit.setIsCorrect(game.getHit()
        .toLowerCase(Locale.ROOT)
        .equals(guess.toLowerCase(Locale.ROOT)));

    hitRepository.save(hit);

    return mapper.map(hit, HitDto.class);
  }

  public Optional<PlayerDto> getPlayer(Long playerId) {
    Player player = playerRepository.findById(playerId)
        .orElseThrow(() -> new PlayerNotFoundException(PLAYER_NOT_FOUND + playerId));

    return Optional.of(mapper.map(player, PlayerDto.class));
  }

  private void checkIfPlayerAlreadyLose(Player player) {
    int NO_MORE_CHANCES = 0;
    if (player.getHitsLimit() != NO_MORE_CHANCES) {
      player.setHitsLimit(player.getHitsLimit() - 1);
    } else {
      throw new GameOverException(PLAYER_ALREADY_LOSE);
    }
  }

  private void saveGameQuestion(Game game, Question question) {
    if (Objects.isNull(game.getQuestions())) {
      game.setQuestions(Lists.newArrayList(question));
    } else {
      game.getQuestions().add(question);
    }

    gameRepository.save(game);
  }

  private boolean checkIfPlayerQuestionHasBeenResponded(Player player, Game game) {
    if (Objects.isNull(game.getQuestions())) {
      return false;
    } else {
      List<Question> sortedQuestions = game.getQuestions().stream()
          .filter(question -> question.getPlayer().getId().equals(player.getId()))
          .sorted(Comparator.comparing(Question::getPublished))
          .collect(Collectors.toList());
      if(sortedQuestions.isEmpty()) {
        return false;
      } else {
        Question lastQuestion = Iterables.getLast(sortedQuestions);
        return lastQuestion.getAnswer()
            .equals(Answer.SIN_RESPUESTA);
      }
    }
  }

}
