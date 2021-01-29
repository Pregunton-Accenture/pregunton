package com.accenture.pregunton.service;

import com.accenture.pregunton.exception.CategoryNotFoundException;
import com.accenture.pregunton.exception.GameNotFoundException;
import com.accenture.pregunton.mapper.MapperList;
import com.accenture.pregunton.model.*;
import com.accenture.pregunton.pojo.Answer;
import com.accenture.pregunton.pojo.GameDto;
import com.accenture.pregunton.pojo.QuestionDto;
import com.accenture.pregunton.pojo.request.PlayerRequestDto;
import com.accenture.pregunton.repository.CategoryRepository;
import com.accenture.pregunton.repository.GameRepository;
import com.accenture.pregunton.repository.RuleRepository;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private RuleRepository ruleRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private MapperList mapperList;

    private static final String GAME_ID_NOT_FOUND = "Game not found with id: ";
    private static final String GAME_CODE_NOT_FOUND = "Game not found with code: ";
    private static final String CATEGORY_ID_NOT_FOUND = "Category not found with id: ";

    public Game create(GameDto gameDto, Long masterId, Long categoryId) throws RuntimeException{
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow((() -> new CategoryNotFoundException(CATEGORY_ID_NOT_FOUND + categoryId)));
        Game game = mapper.map(gameDto, Game.class);
        game.setCategory(category);
        game.setCode(RandomStringUtils.random(6, true, true).toUpperCase());
        gameRepository.save(game);
        saveRulesGame(game.getRules(), game);
        return game;
    }

    public void delete(Long id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new GameNotFoundException(GAME_ID_NOT_FOUND + id));
        gameRepository.delete(game);
    }

    public Optional<GameDto> getOne(Long id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new GameNotFoundException(GAME_ID_NOT_FOUND + id));
        return Optional.of(mapper.map(game, GameDto.class));
    }

    public void addOnePlayer(Long gameId, PlayerRequestDto playerDto) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException(GAME_CODE_NOT_FOUND + gameId));

        Player player = mapper.map(playerDto, Player.class);

        if (Objects.isNull(game.getPlayers())) {
            game.setPlayers(Lists.newArrayList(player));
        } else {
            game.getPlayers().add(player);
        }

        gameRepository.save(game);
    }

    public List<QuestionDto> obtainQuestions(String gameCode) {
        Game game = gameRepository.findByCode(gameCode)
                .orElseThrow(() -> new GameNotFoundException(GAME_CODE_NOT_FOUND + gameCode));

        List<Question> filterQuestions = game.getQuestions()
                .stream()
                .filter(question -> question.getAnswer().equals(Answer.SIN_RESPUESTA))
                .collect(Collectors.toList());

        return Optional.ofNullable(mapperList.mapToDtoList(filterQuestions,
                question -> mapper.map(question, QuestionDto.class)))
                .orElse(Collections.emptyList());
    }

    private void saveRulesGame(Set<Rule> rules, Game game) {
        rules.forEach(rule -> rule.setGame(game));
        ruleRepository.saveAll(rules);
    }
}
