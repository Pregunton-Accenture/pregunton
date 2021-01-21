package com.accenture.pregunton.util;

import com.accenture.pregunton.model.*;
import com.accenture.pregunton.pojo.Answer;
import com.accenture.pregunton.pojo.GameDto;
import com.accenture.pregunton.pojo.PlayerDto;
import com.accenture.pregunton.pojo.QuestionDto;
import com.accenture.pregunton.pojo.request.PlayerRequestDto;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModelUtil {

    public static ModelMapper MODEL_MAPPER = new ModelMapper();

    //Dummy Data
    public static final Long ID = 1L;
    public static final int HITS_LIMIT = 5;
    public static final String DUMMY_CATEGORY = "PERSONAJES";
    public static final String DUMMY_QUESTION = "Es una persona?";
    public static final String DUMMY_RULE = "Solo 3 palabras por pregunta";
    public static final String RULE_VALUE = "3";
    public static final String HIT = "John Doe";
    public static final String NICK_NAME = "Ruso";
    public static final String CODE = "ABC12";

    //models
    public static final Category CATEGORY = createCategory();
    public static final Question QUESTION = createQuestion();
    public static final Player PLAYER = createPlayer();
    public static final Rule RULE = createRule();
    public static final Game GAME = createGame();

    //Dtos
    public static final GameDto GAME_DTO = MODEL_MAPPER.map(GAME, GameDto.class);
    public static final PlayerDto PLAYER_DTO = MODEL_MAPPER.map(PLAYER, PlayerDto.class);
    public static final PlayerRequestDto PLAYER_REQUEST_DTO = MODEL_MAPPER.map(PLAYER, PlayerRequestDto.class);
    public static final QuestionDto QUESTION_DTO = MODEL_MAPPER.map(QUESTION, QuestionDto.class);


    public static Game createGame() {
        return  Game.builder()
                .id(ID)
                .category(CATEGORY)
                .code(CODE)
                .questions(Stream.of(QUESTION).collect(Collectors.toList()))
                .rules(Stream.of(RULE).collect(Collectors.toSet()))
                .players(Stream.of(PLAYER).collect(Collectors.toList()))
                .hit(HIT)
                .build();
    }

    public static Player createPlayer() {
        return Player.builder()
                .id(ID)
                .hitsLimit(HITS_LIMIT)
                .nickName(NICK_NAME)
                .questions(Stream.of(QUESTION).collect(Collectors.toList()))
                .build();
    }

    public static Question createQuestion() {
        return Question.builder()
                .id(ID)
                .question(DUMMY_QUESTION)
                .published(LocalDateTime.now())
                .answer(Answer.SIN_RESPUESTA)
                .build();
    }

    public static Rule createRule() {
        return Rule.builder()
                .id(ID)
                .nameRule(DUMMY_RULE)
                .value(RULE_VALUE)
                .build();
    }

    public static Category createCategory() {
        return Category.builder()
                .id(ID)
                .name(DUMMY_CATEGORY)
                .build();
    }

}
