package com.accenture.pregunton.service;

import com.accenture.pregunton.exception.CategoryNotFoundException;
import com.accenture.pregunton.exception.GameNotFoundException;
import com.accenture.pregunton.model.Game;
import com.accenture.pregunton.model.Player;
import com.accenture.pregunton.pojo.GameDto;
import com.accenture.pregunton.repository.CategoryRepository;
import com.accenture.pregunton.repository.GameRepository;
import com.accenture.pregunton.repository.RuleRepository;
import com.accenture.pregunton.util.ModelUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@RunWith(MockitoJUnitRunner.Silent.class)
public class GameServiceTest {

    @Mock
    private GameRepository gameRepository;
    @Mock
    private RuleRepository ruleRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private GameService gameService;

    @Test
    public void shouldCreateNewGame() {
        Game game = ModelUtil.createGame();
        Mockito.when(modelMapper.map(any(), eq(Game.class))).thenReturn(game);
        Mockito.when(categoryRepository.findById(ModelUtil.ID)).thenReturn(Optional.of(ModelUtil.CATEGORY));
        gameService.create(ModelUtil.GAME_DTO, ModelUtil.ID, ModelUtil.ID);
        Mockito.verify(gameRepository, Mockito.times(1)).save(game);
    }

    @Test(expected = CategoryNotFoundException.class)
    public void create_WhenCategoryIDDoesNotExists_ShouldThrowCategoryNotFoundException() {
        Game game = ModelUtil.createGame();
        Mockito.when(categoryRepository.findById(ModelUtil.ID)).thenReturn(Optional.empty());
        gameService.create(ModelUtil.GAME_DTO, ModelUtil.ID, ModelUtil.ID);
    }

    @Test
    public void shouldDeleteGame() {
        Mockito.when(gameRepository.findById(ModelUtil.ID)).thenReturn(Optional.of(ModelUtil.GAME));
        gameService.delete(ModelUtil.ID);
        Mockito.verify(gameRepository, Mockito.times(1)).delete(ModelUtil.GAME);
    }

    @Test(expected = GameNotFoundException.class)
    public void delete_WhenIDDoesNotExists_ShouldThrowGameNotFoundException() {
        Mockito.when(gameRepository.findById(ModelUtil.ID)).thenReturn(Optional.empty());
        gameService.delete(ModelUtil.ID);
    }

    @Test
    public void getOne_WhenIDExists_ShouldReturnGameDto() {
        Mockito.when(gameRepository.findById(ModelUtil.ID)).thenReturn(Optional.of(ModelUtil.GAME));
        Mockito.when(modelMapper.map(any(), eq(GameDto.class))).thenReturn(ModelUtil.GAME_DTO);

        Optional<GameDto> result = gameService.getOne(ModelUtil.ID);

        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(ModelUtil.GAME_DTO, result.get());
    }

    @Test(expected = GameNotFoundException.class)
    public void shouldThrowAnExceptionWhenGameNotFound() {
        Mockito.when(gameRepository.findById(ModelUtil.ID)).thenReturn(Optional.empty());
        gameService.getOne(ModelUtil.ID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getOne_WhenIDIsNull_ShouldThrowIllegalArgumentException() {
        Mockito.when(gameRepository.findById(null)).thenThrow(IllegalArgumentException.class);
        gameService.getOne(null);
    }

    @Test
    public void shouldAddAPlayerToAnExistingGame() {
        Mockito.when(gameRepository.findById(ModelUtil.ID)).thenReturn(Optional.of(ModelUtil.GAME));
        Mockito.when(modelMapper.map(any(), eq(Player.class))).thenReturn(ModelUtil.PLAYER);
        gameService.addOnePlayer(ModelUtil.ID, ModelUtil.PLAYER_REQUEST_DTO);
    }

    @Test
    public void shouldAddAPlayerIfThereIsNone() {
        Game game = Game.builder().build();
        Mockito.when(gameRepository.findById(ModelUtil.ID)).thenReturn(Optional.of(game));
        Mockito.when(modelMapper.map(any(), eq(Player.class))).thenReturn(ModelUtil.PLAYER);
        gameService.addOnePlayer(ModelUtil.ID, ModelUtil.PLAYER_REQUEST_DTO);
    }

    @Test(expected = GameNotFoundException.class)
    public void addOnePlayer_WhenGameIDDoesNotExists_ShouldThrowGameNotFoundException() {
        Mockito.when(gameRepository.findById(ModelUtil.ID)).thenReturn(Optional.empty());
        gameService.addOnePlayer(ModelUtil.ID, ModelUtil.PLAYER_REQUEST_DTO);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addOnePlayer_WhenGameIDIsNull_ShouldThrowIllegalArgumentException() {
        Mockito.when(gameRepository.findById(null)).thenThrow(IllegalArgumentException.class);
        gameService.addOnePlayer(null, ModelUtil.PLAYER_REQUEST_DTO);
    }

}
