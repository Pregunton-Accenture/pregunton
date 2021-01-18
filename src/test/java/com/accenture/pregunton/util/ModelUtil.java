package com.accenture.pregunton.util;

import com.accenture.pregunton.model.Game;
import com.accenture.pregunton.model.Player;
import com.accenture.pregunton.model.Question;
import com.accenture.pregunton.model.Rule;
import com.accenture.pregunton.pojo.Answer;
import com.accenture.pregunton.pojo.Category;
import com.accenture.pregunton.pojo.GameDto;
import com.accenture.pregunton.pojo.PlayerDto;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModelUtil {

    public static ModelMapper MODEL_MAPPER = new ModelMapper();

    //Dummy Data
    public static final Long ID = 1L;
    public static final Category CATEGORY = Category.builder().name("PERSONAJES").build();
    public static final int HITS_LIMIT = 5;
    public static final String DUMMY_QUESTION = "Dummy Question";
    public static final String DUMMY_RULE = "Dummy Rule";
    public static final String DUMMY_RULE_VALUE = "Dummy Rule value";
    public static final String DUMMY_HIT = "Dummy Hit";
    public static final String NICK_NAME = "Ruso";

    //models
    public static final Question QUESTION = createQuestion();
    public static final Player PLAYER = createPlayer();
    public static final Rule RULE = createRule();
    public static final Game GAME = createGame();

    //Dtos
    public static final GameDto GAME_DTO = MODEL_MAPPER.map(GAME, GameDto.class);
    public static final PlayerDto PLAYER_DTO = MODEL_MAPPER.map(PLAYER, PlayerDto.class);


    public static Game createGame() {
        return  Game.builder()
                .id(ID)
                .category(CATEGORY)
                .questions(Stream.of(QUESTION).collect(Collectors.toList()))
                .rules(Stream.of(RULE).collect(Collectors.toSet()))
                .players(Stream.of(PLAYER).collect(Collectors.toList()))
                .hit(DUMMY_HIT)
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
                .value(DUMMY_RULE_VALUE)
                .build();
    }

}
