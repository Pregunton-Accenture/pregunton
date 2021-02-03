package com.accenture.pregunton.service;

import com.accenture.model.Category;
import com.accenture.model.Game;
import com.accenture.model.Player;
import com.accenture.model.Question;
import com.accenture.model.Rules;
import com.accenture.pojo.Answer;
import com.accenture.pojo.GameDto;
import com.accenture.pojo.QuestionDto;
import com.accenture.pojo.request.PlayerRequestDto;
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

  @Transactional
  public Game create(GameDto gameDto, Long masterId, Long categoryId) throws RuntimeException {
    Objects.requireNonNull(gameDto);
    Objects.requireNonNull(masterId);
    Objects.requireNonNull(categoryId);
    Category category = categoryRepository.findById(categoryId)
        .orElseThrow((() -> new CategoryNotFoundException(categoryId)));

    Game game = mapper.map(gameDto, Game.class);
    Rules savedRules = rulesService.save(game.getRules());

    game.setRules(savedRules);
    game.setCategory(category);
    game.setCode(RandomStringUtils.random(6, true, true)
        .toUpperCase());
    return gameRepository.save(game);
  }

  public void delete(Long id) {
    Game game = gameRepository.findById(id)
        .orElseThrow(() -> new GameIdNotFoundException(id));
    gameRepository.delete(game);
  }

  public Optional<GameDto> getOne(Long id) {
    Game game = gameRepository.findById(id)
        .orElseThrow(() -> new GameIdNotFoundException(id));
    return Optional.of(mapper.map(game, GameDto.class));
  }

  public void addOnePlayer(Long gameId, PlayerRequestDto playerDto) {
    Game game = gameRepository.findById(gameId)
        .orElseThrow(() -> new GameIdNotFoundException(gameId));

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

  public List<QuestionDto> obtainQuestions(String gameCode, boolean withAllQuestion) {
    Game game = gameRepository.findByCode(gameCode)
        .orElseThrow(() -> new GameCodeNotFoundException(gameCode));

    List<Question> filterQuestions = game.getQuestions()
        .stream()
        .filter(getQuestionsPredicate(withAllQuestion))
        .collect(Collectors.toList());

    return mapperList.mapToDtoList(filterQuestions, question -> mapper.map(question, QuestionDto.class));
  }

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
