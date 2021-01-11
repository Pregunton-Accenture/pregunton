package com.accenture.pregunton.service;

import com.accenture.pregunton.model.Game;
import com.accenture.pregunton.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    public Game create(Long masterId) {
        //Dummy Game
        return new Game();
    }

}
