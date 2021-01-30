package com.accenture.pregunton.util;

import com.accenture.pregunton.model.*;
import com.accenture.pregunton.pojo.*;
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
    public static final Question QUESTION = createQuestion();
    public static final Player PLAYER = createPlayer();
    public static final Hit HIT = createHit();
    public static final Rules RULES = createRules();
    public static final Rules RULES_WITH_ID = createRules();
    public static final Game GAME = createGame();

    //Dtos
    public static final GameDto GAME_DTO = MODEL_MAPPER.map(GAME, GameDto.class);
    public static final PlayerDto PLAYER_DTO = MODEL_MAPPER.map(PLAYER, PlayerDto.class);
    public static final PlayerRequestDto PLAYER_REQUEST_DTO = MODEL_MAPPER.map(PLAYER, PlayerRequestDto.class);
    public static final QuestionDto QUESTION_DTO = MODEL_MAPPER.map(QUESTION, QuestionDto.class);
    public static final HitDto HIT_DTO = MODEL_MAPPER.map(HIT, HitDto.class);


    public static Game createGame() {
        return  Game.builder()
                .id(ID)
                .category(CATEGORY)
                .code(CODE)
                .questions(Stream.of(QUESTION).collect(Collectors.toList()))
                .rules(RULES)
                .players(Stream.of(PLAYER).collect(Collectors.toList()))
                .hit(HIT_VALUE)
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

    public static Rules createRules() {
        return Rules.builder()
            .hitLimit(DUMMY_HIT_LIMIT)
            .questionLimit(DUMMY_QUESTION_LIMIT)
            .build();
    }
    public static Rules createRulesWithID() {
        return Rules.builder()
            .id(1L)
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
