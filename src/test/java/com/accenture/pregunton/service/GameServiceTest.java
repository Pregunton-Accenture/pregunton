package com.accenture.pregunton.service;

import com.accenture.pregunton.exception.CategoryNotFoundException;
import com.accenture.pregunton.exception.GameCodeNotFoundException;
import com.accenture.pregunton.mapper.MapperList;
import com.accenture.pregunton.exception.GameIdNotFoundException;
import com.accenture.pregunton.model.Game;
import com.accenture.pregunton.model.Player;
import com.accenture.pregunton.pojo.GameDto;
import com.accenture.pregunton.pojo.QuestionDto;
import com.accenture.pregunton.repository.CategoryRepository;
import com.accenture.pregunton.repository.GameRepository;
import com.accenture.pregunton.repository.RulesRepository;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@RunWith(MockitoJUnitRunner.Silent.class)
public class GameServiceTest {

    @Mock
    private GameRepository gameRepository;
    @Mock
    private RulesRepository ruleRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private MapperList mapperList;
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

    @Test(expected = GameIdNotFoundException.class)
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

    @Test(expected = GameIdNotFoundException.class)
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

    @Test(expected = GameIdNotFoundException.class)
    public void addOnePlayer_WhenGameIDDoesNotExists_ShouldThrowGameNotFoundException() {
        Mockito.when(gameRepository.findById(ModelUtil.ID)).thenReturn(Optional.empty());
        gameService.addOnePlayer(ModelUtil.ID, ModelUtil.PLAYER_REQUEST_DTO);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addOnePlayer_WhenGameIDIsNull_ShouldThrowIllegalArgumentException() {
        Mockito.when(gameRepository.findById(null)).thenThrow(IllegalArgumentException.class);
        gameService.addOnePlayer(null, ModelUtil.PLAYER_REQUEST_DTO);
    }

    @Test
    public void obtainQuestions_WhenSendingValidGameCode_ShouldReturnAListOfQuestions() {
        Mockito.when(gameRepository.findByCode(ModelUtil.CODE)).thenReturn(Optional.of(ModelUtil.GAME));
        Mockito.when(mapperList.mapToDtoList(Stream.of(ModelUtil.QUESTION).collect(Collectors.toList()), o -> modelMapper.map(o, QuestionDto.class)))
                .thenReturn(Stream.of(ModelUtil.QUESTION_DTO).collect(Collectors.toList()));
        gameService.obtainQuestions(ModelUtil.CODE);
    }

    @Test(expected = GameCodeNotFoundException.class)
    public void obtainQuestions_WhendSendingInvalidGameCode_ShouldThrowGameNotFoundException() {
        Mockito.when(gameRepository.findById(ModelUtil.ID)).thenReturn(Optional.empty());
        gameService.obtainQuestions(ModelUtil.CODE);
    }

}
