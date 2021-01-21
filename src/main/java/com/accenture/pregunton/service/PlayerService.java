package com.accenture.pregunton.service;

import com.accenture.pregunton.exception.GameNotFoundException;
import com.accenture.pregunton.exception.GameOverException;
import com.accenture.pregunton.exception.PlayerNotFoundException;
import com.accenture.pregunton.model.Game;
import com.accenture.pregunton.model.Player;
import com.accenture.pregunton.model.Question;
import com.accenture.pregunton.pojo.Answer;
import com.accenture.pregunton.pojo.PlayerDto;
import com.accenture.pregunton.pojo.QuestionDto;
import com.accenture.pregunton.repository.GameRepository;
import com.accenture.pregunton.repository.PlayerRepository;
import com.google.common.collect.Lists;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private ModelMapper mapper;

    public QuestionDto askQuestion(Long playerId, String gameCode, String playerQuestion) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found with id: " + playerId));

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

    public Optional<PlayerDto> getPlayer (Long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found with id: " + playerId));

        return Optional.of(mapper.map(player, PlayerDto.class));
    }

    //Feature
    private void alreadyLost(Player player) {
        int NO_MORE_CHANCES = 0;
        if (player.getHitsLimit() != NO_MORE_CHANCES) {
            player.setHitsLimit(player.getHitsLimit() - 1);
        } else {
            throw new GameOverException("This player already lost.");
        }
    }

    private void saveGameQuestion(String gameCode, Question question) {
        Game game = gameRepository.findByCode(gameCode)
                .orElseThrow(() -> new GameNotFoundException("Game not found with code: " + gameCode));

        if (Objects.isNull(game.getQuestions())) {
            game.setQuestions(Lists.newArrayList(question));
        } else {
            game.getQuestions().add(question);
        }

        gameRepository.save(game);

    }

}
