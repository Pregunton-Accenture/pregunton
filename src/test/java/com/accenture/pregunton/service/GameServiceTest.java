package com.accenture.pregunton.service;

import com.accenture.model.Game;
import com.accenture.model.Player;
import com.accenture.model.Question;
import com.accenture.pojo.Answer;
import com.accenture.pojo.GameDto;
import com.accenture.pojo.QuestionDto;
import com.accenture.pregunton.exception.CategoryNotFoundException;
import com.accenture.pregunton.exception.GameCodeNotFoundException;
import com.accenture.pregunton.exception.GameIdNotFoundException;
import com.accenture.pregunton.mapper.MapperList;
import com.accenture.pregunton.repository.CategoryRepository;
import com.accenture.pregunton.repository.GameRepository;
import com.accenture.pregunton.util.ModelUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@RunWith(MockitoJUnitRunner.Silent.class)
public class GameServiceTest {

  @Mock
  private GameRepository gameRepository;
  @Mock
  private RulesService rulesService;
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
    Mockito.when(modelMapper.map(any(), eq(Game.class)))
        .thenReturn(game);
    Mockito.when(categoryRepository.findById(ModelUtil.ID))
        .thenReturn(Optional.of(ModelUtil.CATEGORY));
    Mockito.when(rulesService.save(any()))
        .thenReturn(ModelUtil.RULES_WITH_ID);

    gameService.create(ModelUtil.GAME_DTO, ModelUtil.ID, ModelUtil.ID);
    Mockito.verify(gameRepository, Mockito.times(1))
        .save(game);
  }

  @Test(expected = CategoryNotFoundException.class)
  public void create_WhenCategoryIDDoesNotExists_ShouldThrowCategoryNotFoundException() {
    Game game = ModelUtil.createGame();
    Mockito.when(categoryRepository.findById(ModelUtil.ID))
        .thenReturn(Optional.empty());
    gameService.create(ModelUtil.GAME_DTO, ModelUtil.ID, ModelUtil.ID);
  }

  @Test
  public void shouldDeleteGame() {
    Mockito.when(gameRepository.findById(ModelUtil.ID))
        .thenReturn(Optional.of(ModelUtil.GAME));
    gameService.delete(ModelUtil.ID);
    Mockito.verify(gameRepository, Mockito.times(1))
        .delete(ModelUtil.GAME);
  }

  @Test(expected = GameIdNotFoundException.class)
  public void delete_WhenIDDoesNotExists_ShouldThrowGameNotFoundException() {
    Mockito.when(gameRepository.findById(ModelUtil.ID))
        .thenReturn(Optional.empty());
    gameService.delete(ModelUtil.ID);
  }

  @Test
  public void getOne_WhenIDExists_ShouldReturnGameDto() {
    Mockito.when(gameRepository.findById(ModelUtil.ID))
        .thenReturn(Optional.of(ModelUtil.GAME));
    Mockito.when(modelMapper.map(any(), eq(GameDto.class)))
        .thenReturn(ModelUtil.GAME_DTO);

    Optional<GameDto> result = gameService.getOne(ModelUtil.ID);

    Assert.assertTrue(result.isPresent());
    assertEquals(ModelUtil.GAME_DTO, result.get());
  }

  @Test
  public void getOne_WhenIdDoesNotExist_ShouldReturnEmptyOptional() {
    Mockito.when(gameRepository.findById(ModelUtil.ID))
        .thenReturn(Optional.empty());
    assertFalse(gameService.getOne(ModelUtil.ID)
        .isPresent());
  }

  @Test(expected = IllegalArgumentException.class)
  public void getOne_WhenIDIsNull_ShouldThrowIllegalArgumentException() {
    Mockito.when(gameRepository.findById(null))
        .thenThrow(IllegalArgumentException.class);
    gameService.getOne(null);
  }

  @Test
  public void shouldAddAPlayerToAnExistingGame() {
    Mockito.when(gameRepository.findById(ModelUtil.ID))
        .thenReturn(Optional.of(ModelUtil.GAME));
    Mockito.when(modelMapper.map(any(), eq(Player.class)))
        .thenReturn(ModelUtil.PLAYER);
    gameService.addOnePlayer(ModelUtil.CODE, ModelUtil.PLAYER_DTO);
  }

  @Test
  public void shouldAddAPlayerIfThereIsNone() {
    Game game = Game.builder()
        .rules(ModelUtil.RULES)
        .build();
    Mockito.when(gameRepository.findById(ModelUtil.ID))
        .thenReturn(Optional.of(game));
    Mockito.when(modelMapper.map(any(), eq(Player.class)))
        .thenReturn(ModelUtil.PLAYER);
    gameService.addOnePlayer(ModelUtil.CODE, ModelUtil.PLAYER_DTO);
  }

  @Test(expected = GameIdNotFoundException.class)
  public void addOnePlayer_WhenGameIDDoesNotExists_ShouldThrowGameNotFoundException() {
    Mockito.when(gameRepository.findById(ModelUtil.ID))
        .thenReturn(Optional.empty());
    gameService.addOnePlayer(ModelUtil.CODE, ModelUtil.PLAYER_DTO);
  }

  @Test(expected = IllegalArgumentException.class)
  public void addOnePlayer_WhenGameIDIsNull_ShouldThrowIllegalArgumentException() {
    Mockito.when(gameRepository.findById(null))
        .thenThrow(IllegalArgumentException.class);
    gameService.addOnePlayer(null, ModelUtil.PLAYER_DTO);
  }

  @Test
  public void obtainQuestions_WhenSendingValidGameCodeAndWithAllQuestion_ShouldReturnAListOfQuestions() {
    Mockito.when(gameRepository.findByCode(ModelUtil.CODE))
        .thenReturn(Optional.of(ModelUtil.GAME));
    Mockito.when(mapperList.mapToDtoList(Stream.of(ModelUtil.QUESTION)
        .collect(Collectors.toList()), o -> modelMapper.map(o, QuestionDto.class)))
        .thenReturn(Stream.of(ModelUtil.QUESTION_DTO)
            .collect(Collectors.toList()));
    gameService.obtainQuestions(ModelUtil.CODE, true);
  }

  @Test
  public void obtainQuestions_WhenSendingValidGameCodeAndWithoutAllQuestion_ShouldReturnAListOfQuestions() {
    Question question1 = Question.builder()
        .question("question 2")
        .answer(Answer.SIN_RESPUESTA)
        .build();
    Question question2 = Question.builder()
        .question("question 3")
        .answer(Answer.NO)
        .build();
    QuestionDto questionDto1 = new QuestionDto();
    questionDto1.setQuestion("question 2");
    questionDto1.setAnswer(Answer.SIN_RESPUESTA);
    QuestionDto questionDto2 = new QuestionDto();
    questionDto2.setQuestion("question 3");
    questionDto2.setAnswer(Answer.NO);

    Game game = ModelUtil.createGame();
    game.setCode(ModelUtil.CODE);
    game.getQuestions()
        .add(question1);
    game.getQuestions()
        .add(question2);

    Mockito.when(gameRepository.findByCode(ModelUtil.CODE))
        .thenReturn(Optional.of(game));
    Mockito.when(mapperList.mapToDtoList(any(), any()))
        .thenCallRealMethod();
    Mockito.when(modelMapper.map(eq(question1), eq(QuestionDto.class)))
        .thenReturn(questionDto1);

    List<QuestionDto> result = gameService.obtainQuestions(ModelUtil.CODE, false);
    List<QuestionDto> expected = Collections.singletonList(questionDto1);

    assertEquals(expected, result);
  }

  @Test(expected = GameCodeNotFoundException.class)
  public void obtainQuestions_WhenSendingInvalidGameCode_ShouldThrowGameNotFoundException() {
    Mockito.when(gameRepository.findById(ModelUtil.ID))
        .thenReturn(Optional.empty());
    gameService.obtainQuestions(ModelUtil.CODE, true);
  }

  @Test
  public void saveQuestion_WhenGameCodeAndQuestionAreValidAndQuestionsListEmpty_ShouldNotThrowException() {
    Game game = ModelUtil.createGame();
    game.setQuestions(null);

    Mockito.when(gameRepository.findByCode(ModelUtil.CODE))
        .thenReturn(Optional.of(game));
    Mockito.when(gameRepository.save(any()))
        .thenReturn(game);

    gameService.saveQuestion(ModelUtil.CODE, ModelUtil.QUESTION);

    Mockito.verify(gameRepository, Mockito.times(1)).findByCode(any());
    Mockito.verify(gameRepository, Mockito.times(1)).save(any());
  }

  @Test
  public void saveQuestion_WhenGameCodeAndQuestionAreValidAndQuestionsListNotEmpty_ShouldNotThrowException() {
    Game game = ModelUtil.createGame();
    game.getQuestions()
        .add(ModelUtil.QUESTION);

    Mockito.when(gameRepository.findByCode(ModelUtil.CODE))
        .thenReturn(Optional.of(game));
    Mockito.when(gameRepository.save(any()))
        .thenReturn(game);

    gameService.saveQuestion(ModelUtil.CODE, ModelUtil.QUESTION);

    Mockito.verify(gameRepository, Mockito.times(1)).findByCode(any());
    Mockito.verify(gameRepository, Mockito.times(1)).save(any());
  }

  @Test(expected = GameCodeNotFoundException.class)
  public void saveQuestion_WhenGameCodeDoesNotExist_ShouldThrowGameCodeNotFoundException() {
    Mockito.when(gameRepository.findByCode(ModelUtil.CODE))
        .thenReturn(Optional.empty());

    gameService.saveQuestion(ModelUtil.CODE, ModelUtil.QUESTION);
  }

}
