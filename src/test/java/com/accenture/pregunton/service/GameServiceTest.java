package com.accenture.pregunton.service;

import com.accenture.pregunton.exception.GameNotFoundException;
import com.accenture.pregunton.model.Game;
import com.accenture.pregunton.model.Player;
import com.accenture.pregunton.pojo.GameDto;
import com.accenture.pregunton.pojo.PlayerDto;
import com.accenture.pregunton.repository.GameRepository;
import com.accenture.pregunton.repository.RuleRepository;
import com.accenture.pregunton.util.ModelUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;

@RunWith(MockitoJUnitRunner.Silent.class)
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

    @Test
    public void shouldDeleteGame() {
        Mockito.when(gameRepository.findById(ModelUtil.ID)).thenReturn(Optional.of(ModelUtil.GAME));
        gameService.delete(ModelUtil.ID);
        Mockito.verify(gameRepository).delete(ModelUtil.GAME);
    }

    @Test
    public void shouldGetOneGame() {
        Mockito.when(gameRepository.findById(ModelUtil.ID))
                .thenReturn(Optional.of(ModelUtil.GAME));
        Mockito.when(modelMapper.map(any(), eq(GameDto.class)))
                .thenReturn(ModelUtil.GAME_DTO);
        Mockito.when(gameService.getOne(ModelUtil.ID))
                .thenReturn(Optional.of(ModelUtil.GAME_DTO));
    }

    @Test(expected = GameNotFoundException.class)
    public void shouldThrowAnExceptionWhenGameNotFound() throws GameNotFoundException {
        Mockito.when(gameRepository.findById(ModelUtil.ID))
                .thenThrow(new GameNotFoundException("Game not found with id: " + ModelUtil.ID));
        gameService.getOne(ModelUtil.ID);
    }

    @Test
    public void shouldAddAPlayertoAnExistingGame() {
        Mockito.when(gameRepository.findById(ModelUtil.ID)).thenReturn(Optional.of(ModelUtil.GAME));
        Mockito.when(modelMapper.map(any(), eq(Player.class))).thenReturn(ModelUtil.PLAYER);
        gameService.addOnePlayer(ModelUtil.ID, ModelUtil.PLAYER_DTO);
    }

    @Test
    public void shouldAddAPlayerIfThereIsNone() {
        Game game = Game.builder().build();
        Mockito.when(gameRepository.findById(ModelUtil.ID)).thenReturn(Optional.of(game));
        Mockito.when(modelMapper.map(any(), eq(Player.class))).thenReturn(ModelUtil.PLAYER);
        gameService.addOnePlayer(ModelUtil.ID, ModelUtil.PLAYER_DTO);
    }

}
