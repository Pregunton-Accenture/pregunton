package com.accenture.pregunton.service;

import com.accenture.model.Category;
import com.accenture.model.Game;
import com.accenture.model.Player;
import com.accenture.model.Question;
import com.accenture.model.Rules;
import com.accenture.pojo.Answer;
import com.accenture.pojo.GameDto;
import com.accenture.pojo.GameStatus;
import com.accenture.pojo.PlayerDto;
import com.accenture.pojo.QuestionDto;
import com.accenture.pregunton.exception.CategoryNotFoundException;
import com.accenture.pregunton.exception.GameCodeNotFoundException;
import com.accenture.pregunton.exception.GameIdNotFoundException;
import com.accenture.pregunton.mapper.MapperList;
import com.accenture.pregunton.repository.CategoryRepository;
import com.accenture.pregunton.repository.GameRepository;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class GameService {

  @Autowired
  private GameRepository gameRepository;
  @Autowired
  private RulesService rulesService;
  @Autowired
  private CategoryRepository categoryRepository;
  @Autowired
  private ModelMapper mapper;
  @Autowired
  private MapperList mapperList;

  /**
   * Create a new game by a player who in this case would be the master and stored to the database.
   *
   * @param gameDto the game data
   * @param master the player that create te game
   * @param categoryId the category
   *
   * @return the game saved on database
   *
   * @throws RuntimeException if the creation of a game throws any error
   */
  @Transactional
  public Game create(GameDto gameDto, String master, Long categoryId) throws RuntimeException {
    Objects.requireNonNull(gameDto);
    Objects.requireNonNull(master);
    Objects.requireNonNull(categoryId);
    Category category = categoryRepository.findById(categoryId)
        .orElseThrow((() -> new CategoryNotFoundException(categoryId)));

    Game game = mapper.map(gameDto, Game.class);
    Rules savedRules = rulesService.save(game.getRules());

    game.setRules(savedRules);
    game.setMaster(master);
    game.setCategory(category);
    game.setStatus(GameStatus.IN_PROGRESS);
    game.setCode(RandomStringUtils.random(6, true, true)
        .toUpperCase());
    return gameRepository.save(game);
  }

  /**
   * Delete a created game from the database
   *
   * @param id the game id
   */
  public void delete(Long id) {
    Game game = gameRepository.findById(id)
        .orElseThrow(() -> new GameIdNotFoundException(id));
    gameRepository.delete(game);
  }

  /**
   * Retrieves the game data from database
   *
   * @param id the game id
   *
   * @return the game model saved in database
   */
  public Optional<GameDto> getOne(Long id) {
    return gameRepository.findById(id)
        .map(game -> mapper.map(game, GameDto.class));
  }

  /**
   * Add a new player to a game
   *
   * @param gameCode the game code
   * @param playerDto the player
   */
  public void addOnePlayer(String gameCode, PlayerDto playerDto) {
    Game game = gameRepository.findByCode(gameCode)
        .orElseThrow(() -> new GameCodeNotFoundException(gameCode));

    Player player = mapper.map(playerDto, Player.class);
    player.setHitsLimit(game.getRules()
        .getHitLimit());
    player.setQuestionsLimit(game.getRules()
        .getQuestionLimit());

    if (Objects.isNull(game.getPlayers())) {
      game.setPlayers(Lists.newArrayList(player));
    } else {
      game.getPlayers()
          .add(player);
    }
    gameRepository.save(game);
  }

  /**
   * Save the question of the player in the current game
   *
   * @param gameCode the game code
   * @param question the question of the player
   */
  public void saveQuestion(String gameCode, Question question) {
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

  /**
   * Get the questions of a specific game or get only the questions that are not answered yet
   *
   * @param gameCode the game code
   * @param withAllQuestion true if retrieve all questions or false if retrieves only the questions that are no answered yet
   *
   * @return an empty list or a list of question
   */
  public List<QuestionDto> obtainQuestions(String gameCode, boolean withAllQuestion) {
    Game game = gameRepository.findByCode(gameCode)
        .orElseThrow(() -> new GameCodeNotFoundException(gameCode));

    List<Question> filterQuestions = game.getQuestions()
        .stream()
        .filter(getQuestionsPredicate(withAllQuestion))
        .collect(Collectors.toList());

    return mapperList.mapToDtoList(filterQuestions, question -> mapper.map(question, QuestionDto.class));
  }

  /**
   * The predicate to filter the list of questions
   * <p>
   * Filter the questions between only the question that are not answer yet or all the question of a game
   * </p>
   *
   * @param withAllQuestion true if retrieve all questions or false if retrieves only the questions that are no answered yet
   *
   * @return a predicate that indicates if retrieve all the questions or only that are not answered yet
   */
  private Predicate<Question> getQuestionsPredicate(boolean withAllQuestion) {
    Predicate<Question> predicate;
    if (withAllQuestion) {
      predicate = question -> true;
    } else {
      predicate = question -> question.getAnswer()
          .equals(Answer.SIN_RESPUESTA);
    }
    return predicate;
  }
}
