package com.accenture.pregunton.service;

import com.accenture.pregunton.model.Game;
import com.accenture.pregunton.repository.GameRepository;
import com.accenture.pregunton.repository.RuleRepository;
import com.accenture.pregunton.util.ModelUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import static org.mockito.ArgumentMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class GameServiceTest {

    @Mock
    private GameRepository gameRepository;
    @Mock
    private RuleRepository ruleRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private GameService gameService;

    @Test
    public void shouldCreateNewGame() {
        Game game = ModelUtil.createGame();
        Mockito.when(modelMapper.map(any(), eq(Game.class))).thenReturn(game);
        gameService.create(ModelUtil.GAME_DTO, ModelUtil.ID);
        Mockito.verify(gameRepository, Mockito.times(1)).save(game);
    }

}
