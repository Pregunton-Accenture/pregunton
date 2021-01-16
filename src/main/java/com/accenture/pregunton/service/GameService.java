package com.accenture.pregunton.service;

import com.accenture.pregunton.model.Game;
import com.accenture.pregunton.model.Rule;
import com.accenture.pregunton.pojo.GameDto;
import com.accenture.pregunton.repository.GameRepository;
import com.accenture.pregunton.repository.RuleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    private void saveRulesGame(Set<Rule> rules, Game game) {
        rules.forEach(rule -> rule.setGame(game));
        ruleRepository.saveAll(rules);
    }

}
