package com.accenture.pregunton.service;

import com.accenture.pregunton.exception.GameNotFoundException;
import com.accenture.pregunton.model.Game;
import com.accenture.pregunton.model.Player;
import com.accenture.pregunton.model.Rule;
import com.accenture.pregunton.pojo.GameDto;
import com.accenture.pregunton.pojo.PlayerDto;
import com.accenture.pregunton.repository.GameRepository;
import com.accenture.pregunton.repository.RuleRepository;
import com.google.common.collect.Lists;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private RuleRepository ruleRepository;
    @Autowired
    private ModelMapper mapper;

    public void create(GameDto gameDto, Long masterId) throws RuntimeException{
        Game game = mapper.map(gameDto, Game.class);
        gameRepository.save(game);
        saveRulesGame(game.getRules(), game);
    }

    public void delete(Long id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new GameNotFoundException("Game not found with id: " + id));
        gameRepository.delete(game);
    }

    public Optional<GameDto> getOne(Long id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new GameNotFoundException("Game not found with id: " + id));
        return Optional.of(mapper.map(game, GameDto.class));
    }

    public void addOnePlayer(Long gameId, PlayerDto playerDto) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException("Game not found with id: " + gameId));

        Player player = mapper.map(playerDto, Player.class);

        if (Objects.isNull(game.getPlayers())) {
            game.setPlayers(Lists.newArrayList(player));
        } else {
            game.getPlayers().add(player);
        }

        gameRepository.save(game);
    }

    private void saveRulesGame(Set<Rule> rules, Game game) {
        rules.forEach(rule -> rule.setGame(game));
        ruleRepository.saveAll(rules);
    }
}
