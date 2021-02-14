package com.accenture.pregunton.service;

import com.accenture.model.Game;
import com.accenture.model.Hit;
import com.accenture.model.Player;
import com.accenture.model.Question;
import com.accenture.pojo.Answer;
import com.accenture.pojo.GameStatus;
import com.accenture.pojo.HitDto;
import com.accenture.pojo.PlayerDto;
import com.accenture.pojo.QuestionDto;
import com.accenture.pregunton.exception.GameCodeNotFoundException;
import com.accenture.pregunton.exception.GameFinishedException;
import com.accenture.pregunton.exception.GameOverException;
import com.accenture.pregunton.exception.LastQuestionNotAnswerException;
import com.accenture.pregunton.exception.PlayerMaxQuestionException;
import com.accenture.pregunton.exception.PlayerNotFoundException;
import com.accenture.pregunton.repository.GameRepository;
import com.accenture.pregunton.repository.HitRepository;
import com.accenture.pregunton.repository.PlayerRepository;
import com.google.common.collect.Iterables;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlayerService {

  @Autowired
  private PlayerRepository playerRepository;
  @Autowired
  private GameRepository gameRepository;
  @Autowired
  private GameService gameService;
  @Autowired
  private HitRepository hitRepository;
  @Autowired
  private ModelMapper mapper;

  /**
   * Adds the questions done by player in database.
   *
   * @param nickName the player that made the question
   * @param gameCode the game where the player ask
   * @param playerQuestion the question
   *
   * @return the question model saved in database
   *
   * @throws PlayerNotFoundException if the playerId does not exist
   * @throws PlayerMaxQuestionException if the player exceed the max number of questions
   * @throws GameOverException if the player already lose
   * @throws GameCodeNotFoundException if the gameCode does not exist
   * @throws LastQuestionNotAnswerException if the last question made by the player was not answered
   */
  public QuestionDto askQuestion(String nickName, String gameCode, String playerQuestion) {
    checkIfTheGameAlreadyFinished(gameCode);
    Player player = playerRepository.findByNickName(nickName)
        .orElseThrow(() -> new PlayerNotFoundException(nickName));
    checkIfPlayerCanAsk(player);
    checkIfPlayerCanGuess(player);
    Game game = gameRepository.findByCode(gameCode)
        .orElseThrow(() -> new GameCodeNotFoundException(gameCode));
    if (checkIfPlayerQuestionHasBeenResponded(player, game)) {
      throw new LastQuestionNotAnswerException(player.getNickName());
    }

    Question question = Question.builder()
        .question(playerQuestion)
        .answer(Answer.SIN_RESPUESTA)
        .published(LocalDateTime.now())
        .player(player)
        .build();

    gameService.saveQuestion(gameCode, question);
    player.setQuestionsLimit(player.getQuestionsLimit() - 1);
    playerRepository.save(player);

    return mapper.map(question, QuestionDto.class);
  }

  /**
   * Validates and saves a Guess made by player.
   * <p>
   * Saves the guess and put "correct" value with <b>true</b> is the string is equals to the {@link Game} hit.
   * </p>
   *
   * @param nickName the player that made the question
   * @param gameCode the game where the player ask
   * @param guess the hit made by player
   *
   * @return the hit model saved in database
   *
   * @throws PlayerNotFoundException if the playerId does not exist
   * @throws GameOverException if the player exceed the hit limit
   * @throws GameCodeNotFoundException if the gameCode does not exist
   */
  @Transactional
  public HitDto makeAGuess(String nickName, String gameCode, String guess) {
    checkIfTheGameAlreadyFinished(gameCode);
    Player player = playerRepository.findByNickName(nickName)
        .orElseThrow(() -> new PlayerNotFoundException(nickName));
    checkIfPlayerCanGuess(player);
    String gameHit = gameRepository.getHitByCode(gameCode)
        .orElseThrow(() -> new GameCodeNotFoundException(gameCode));

    Hit hit = Hit.builder()
        .guess(guess)
        .published(LocalDateTime.now())
        .player(player)
        .isCorrect(gameHit.toLowerCase(Locale.ROOT)
            .equals(guess.toLowerCase(Locale.ROOT)))
        .build();

    if (hit.getIsCorrect()) {
      Game game = gameRepository.findByCode(gameCode)
          .orElseThrow(() -> new GameCodeNotFoundException(gameCode));
      game.setStatus(GameStatus.FINISHED);
      gameRepository.save(game);
    }

    hitRepository.save(hit);
    player.setHitsLimit(player.getHitsLimit() - 1);
    playerRepository.save(player);

    return mapper.map(hit, HitDto.class);
  }

  /**
   * Retrieves the player by the playerId.
   *
   * @param nickName the player identifier to search
   *
   * @return an Optional with the player data or an empty Optional if the playerId does not exist.
   */
  public Optional<PlayerDto> getPlayer(String nickName) {
    return playerRepository.findByNickName(nickName)
        .map((player) -> mapper.map(player, PlayerDto.class));
  }

  /**
   * Validates if the player does not exceed the limit of hits.
   *
   * @param player the player data.
   *
   * @throws GameOverException if the player exceed the hit limit.
   */
  private void checkIfPlayerCanGuess(Player player) {
    int NO_MORE_CHANCES = 0;
    if (player.getHitsLimit() <= NO_MORE_CHANCES) {
      throw new GameOverException(player.getNickName());
    }
  }


  /**
   * Validates if the player does not exceed the limit of questions.
   *
   * @param player the player data.
   *
   * @throws PlayerMaxQuestionException if the player exceed the questions limit.
   */
  private void checkIfPlayerCanAsk(Player player) {
    int NO_MORE_CHANCES = 0;
    if (player.getQuestionsLimit() <= NO_MORE_CHANCES) {
      throw new PlayerMaxQuestionException(player.getNickName());
    }
  }

  /**
   * Validates if the last question of a player has been responded.
   *
   * @param player the player data
   * @param game the game data
   *
   * @return <b>true</b> if the last question was responded; otherwise <b>false</b>
   */
  private boolean checkIfPlayerQuestionHasBeenResponded(Player player, Game game) {
    boolean result = false;
    if (Objects.nonNull(game.getQuestions())) {
      List<Question> sortedQuestions = game.getQuestions()
          .stream()
          .filter(question -> question.getPlayer()
              .getId()
              .equals(player.getId()))
          .sorted(Comparator.comparing(Question::getPublished))
          .collect(Collectors.toList());
      if (!sortedQuestions.isEmpty()) {
        Question lastQuestion = Iterables.getLast(sortedQuestions);
        result = lastQuestion.getAnswer()
            .equals(Answer.SIN_RESPUESTA);
      }
    }
    return result;
  }

  /**
   * Validates if the game have already finished
   *
   * @param gameCode
   */
  private void checkIfTheGameAlreadyFinished(String gameCode) {
    Game game = gameRepository.findByCode(gameCode)
        .orElseThrow(() -> new GameCodeNotFoundException(gameCode));
    if (game.getStatus().equals(GameStatus.FINISHED)) {
      throw new GameFinishedException();
    }
  }
}
