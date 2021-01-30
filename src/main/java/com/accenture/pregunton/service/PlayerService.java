package com.accenture.pregunton.service;

import com.accenture.pregunton.exception.GameCodeNotFoundException;
import com.accenture.pregunton.exception.GameOverException;
import com.accenture.pregunton.exception.PlayerNotFoundException;
import com.accenture.pregunton.model.Game;
import com.accenture.pregunton.model.Hit;
import com.accenture.pregunton.model.Player;
import com.accenture.pregunton.model.Question;
import com.accenture.pregunton.pojo.Answer;
import com.accenture.pregunton.pojo.HitDto;
import com.accenture.pregunton.pojo.PlayerDto;
import com.accenture.pregunton.pojo.QuestionDto;
import com.accenture.pregunton.repository.GameRepository;
import com.accenture.pregunton.repository.HitRepository;
import com.accenture.pregunton.repository.PlayerRepository;
import com.google.common.collect.Lists;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

@Service
public class PlayerService {

  private static final String GAME_NOT_FOUND = "Game not found with code: ";
  private static final String PLAYER_NOT_FOUND = "Player not found with id: ";
  @Autowired
  private PlayerRepository playerRepository;
  @Autowired
  private GameRepository gameRepository;
  @Autowired
  private HitRepository hitRepository;
  @Autowired
  private ModelMapper mapper;

  public QuestionDto askQuestion(Long playerId, String gameCode, String playerQuestion) {
    Player player = playerRepository.findById(playerId)
        .orElseThrow(() -> new PlayerNotFoundException(PLAYER_NOT_FOUND + playerId));

    Question question = Question.builder()
        .question(playerQuestion)
        .answer(Answer.SIN_RESPUESTA)
        .published(LocalDateTime.now())
        .player(player)
        .build();

    playerRepository.save(player);
    saveGameQuestion(gameCode, question);

    return mapper.map(question, QuestionDto.class);
  }

  public HitDto makeAGuess(Long playerId, String gameCode, String guess) {
    Player player = playerRepository.findById(playerId)
        .orElseThrow(() -> new PlayerNotFoundException(PLAYER_NOT_FOUND + playerId));
    Game game = gameRepository.findByCode(gameCode)
        .orElseThrow(() -> new GameCodeNotFoundException(gameCode));

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
      throw new GameOverException("This player already lose.");
    }
  }

  private void saveGameQuestion(String gameCode, Question question) {
    Game game = gameRepository.findByCode(gameCode)
        .orElseThrow(() -> new GameCodeNotFoundException(gameCode));

    if (Objects.isNull(game.getQuestions())) {
      game.setQuestions(Lists.newArrayList(question));
    } else {
      game.getQuestions()
          .add(question);
    }

    gameRepository.save(game);

  }

}
