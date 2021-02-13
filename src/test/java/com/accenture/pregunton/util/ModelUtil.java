package com.accenture.pregunton.util;

import com.accenture.model.Category;
import com.accenture.model.Game;
import com.accenture.model.Hit;
import com.accenture.model.Player;
import com.accenture.model.Question;
import com.accenture.model.Rules;
import com.accenture.pojo.Answer;
import com.accenture.pojo.GameDto;
import com.accenture.pojo.GameStatus;
import com.accenture.pojo.HitDto;
import com.accenture.pojo.PlayerDto;
import com.accenture.pojo.QuestionDto;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModelUtil {

  //Dummy Data
  public static final Long ID = 1L;
  public static final int HITS_LIMIT = 5;
  public static final String MASTER_NAME = "MASTER";
  public static final String DUMMY_CATEGORY = "SOME CATEGORY";
  public static final String DUMMY_QUESTION = "SOME QUESTION";
  public static final String CORRECT_GUESS = "John Doe";
  public static final Integer DUMMY_HIT_LIMIT = 5;
  public static final Integer DUMMY_QUESTION_LIMIT = 10;
  public static final String HIT_VALUE = "John Doe";
  public static final String NICK_NAME = "Ruso";
  public static final String CODE = "ABC12";
  //models
  public static final Category CATEGORY = createCategory();
  public static final Player PLAYER = createPlayer();
  public static final Question QUESTION = createQuestion();
  public static final Hit HIT = createHit();
  public static final Rules RULES = createRules();
  public static final Rules RULES_WITH_ID = createRules();
  public static final Game GAME = createGame();
  public static ModelMapper MODEL_MAPPER = new ModelMapper();
  //Dtos
  public static final PlayerDto PLAYER_DTO = MODEL_MAPPER.map(PLAYER, PlayerDto.class);
  public static final QuestionDto QUESTION_DTO = MODEL_MAPPER.map(QUESTION, QuestionDto.class);
  public static final HitDto HIT_DTO = MODEL_MAPPER.map(HIT, HitDto.class);
  public static final GameDto GAME_DTO = MODEL_MAPPER.map(GAME, GameDto.class);


  public static Game createGame() {
    return Game.builder()
        .id(ID)
        .category(CATEGORY)
        .code(CODE)
        .questions(Stream.of(QUESTION)
            .collect(Collectors.toList()))
        .rules(RULES)
        .players(Stream.of(PLAYER)
            .collect(Collectors.toList()))
        .hit(HIT_VALUE)
        .status(GameStatus.IN_PROGRESS)
        .build();
  }

  public static Player createPlayer() {
    return Player.builder()
        .id(ID)
        .hitsLimit(HITS_LIMIT)
        .nickName(NICK_NAME)
        .questionsLimit(DUMMY_QUESTION_LIMIT)
        .questions(Stream.of(QUESTION)
            .collect(Collectors.toList()))
        .build();
  }

  public static Question createQuestion() {
    return Question.builder()
        .id(ID)
        .question(DUMMY_QUESTION)
        .published(LocalDateTime.now())
        .answer(Answer.SI)
        .player(PLAYER)
        .build();
  }

  public static Rules createRules() {
    return Rules.builder()
        .hitLimit(DUMMY_HIT_LIMIT)
        .questionLimit(DUMMY_QUESTION_LIMIT)
        .build();
  }

  public static Category createCategory() {
    return Category.builder()
        .id(ID)
        .name(DUMMY_CATEGORY)
        .build();
  }

  public static Hit createHit() {
    return Hit.builder()
        .id(ID)
        .isCorrect(true)
        .published(LocalDateTime.now())
        .guess(ModelUtil.CORRECT_GUESS)
        .player(PLAYER)
        .build();
  }

}
