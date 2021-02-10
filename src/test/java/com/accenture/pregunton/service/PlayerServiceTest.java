package com.accenture.pregunton.service;

import com.accenture.model.Game;
import com.accenture.model.Player;
import com.accenture.model.Question;
import com.accenture.pojo.Answer;
import com.accenture.pojo.HitDto;
import com.accenture.pojo.PlayerDto;
import com.accenture.pojo.QuestionDto;
import com.accenture.pregunton.exception.GameCodeNotFoundException;
import com.accenture.pregunton.exception.GameOverException;
import com.accenture.pregunton.exception.LastQuestionNotAnswerException;
import com.accenture.pregunton.exception.PlayerMaxQuestionException;
import com.accenture.pregunton.exception.PlayerNotFoundException;
import com.accenture.pregunton.repository.GameRepository;
import com.accenture.pregunton.repository.HitRepository;
import com.accenture.pregunton.repository.PlayerRepository;
import com.accenture.pregunton.util.ModelUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;

@RunWith(MockitoJUnitRunner.Silent.class)
public class PlayerServiceTest {

  @InjectMocks
  private PlayerService playerService;
  @Mock
  private ModelMapper modelMapper;
  @Mock
  private PlayerRepository playerRepository;
  @Mock
  private HitRepository hitRepository;
  @Mock
  private GameRepository gameRepository;
  @Mock
  private GameService gameService;

  @Before
  public void setup() {
    Mockito.when(playerRepository.findById(anyLong()))
        .thenReturn(Optional.of(ModelUtil.PLAYER));

    Mockito.when(modelMapper.map(ModelUtil.QUESTION, QuestionDto.class))
        .thenReturn(ModelUtil.QUESTION_DTO);
    Mockito.when(modelMapper.map(ModelUtil.PLAYER, PlayerDto.class))
        .thenReturn(ModelUtil.PLAYER_DTO);
    Mockito.when(modelMapper.map(ModelUtil.HIT, HitDto.class))
        .thenReturn(ModelUtil.HIT_DTO);
  }

  @Test
  public void askQuestion_ShouldAddAQuestionToTheGameAndThePlayer() {
    Game game = ModelUtil.createGame();
    game.setQuestions(null);

    Mockito.when(gameRepository.findByCode(anyString()))
        .thenReturn(Optional.of(game));
    doNothing().when(gameService)
        .saveQuestion(anyString(), any());

    playerService.askQuestion(ModelUtil.ID, ModelUtil.CODE, ModelUtil.DUMMY_QUESTION);

    Mockito.verify(playerRepository, Mockito.times(1))
        .save(ModelUtil.PLAYER);
  }

  @Test(expected = PlayerNotFoundException.class)
  public void askQuestion_WhenSendingInvalidId_ShouldThrowPlayerNotFoundException() {
    Mockito.when(playerRepository.findById(any()))
        .thenReturn(Optional.empty());
    playerService.askQuestion(ModelUtil.ID, ModelUtil.CODE, ModelUtil.DUMMY_QUESTION);
  }

  @Test(expected = GameCodeNotFoundException.class)
  public void askQuestion_WhenSavingTheQuestionsOfTheGameAndTheGameDoesNotExist_ShouldThrowGameNotFoundException() {
    Mockito.when(gameRepository.findByCode(any()))
        .thenReturn(Optional.empty());
    playerService.askQuestion(ModelUtil.ID, ModelUtil.CODE, ModelUtil.DUMMY_QUESTION);
  }

  @Test(expected = LastQuestionNotAnswerException.class)
  public void askQuestion_WhenSendingAQuestionAndTheLastOneHasBeenNotAnswerYet_ShouldThrowLastQuestionNotAnswerException() {
    Player player = ModelUtil.createPlayer();
    Game game = ModelUtil.createGame();
    game.setQuestions(Stream.of(Question.builder()
        .answer(Answer.SIN_RESPUESTA)
        .player(player)
        .build())
        .collect(Collectors.toList()));

    Mockito.when(playerRepository.findById(anyLong()))
        .thenReturn(Optional.of(player));
    Mockito.when(gameRepository.findByCode(anyString()))
        .thenReturn(Optional.of(game));

    playerService.askQuestion(ModelUtil.ID, ModelUtil.CODE, ModelUtil.DUMMY_QUESTION);
  }

  @Test
  public void askQuestion_WhenSendingAQuestionAndListIsEmpty_ShouldSaveTheQuestion() {
    Game game = ModelUtil.createGame();
    game.setQuestions(new ArrayList<>());

    Mockito.when(playerRepository.findById(anyLong()))
        .thenReturn(Optional.of(ModelUtil.PLAYER));
    Mockito.when(gameRepository.findByCode(anyString()))
        .thenReturn(Optional.of(game));

    playerService.askQuestion(ModelUtil.ID, ModelUtil.CODE, ModelUtil.DUMMY_QUESTION);

    Mockito.verify(playerRepository, Mockito.times(1))
        .save(any());
  }

  @Test(expected = PlayerMaxQuestionException.class)
  public void askQuestion_WhenSavingTheQuestionsOfTheGameAndPlayerExceedQuestionLimit_ShouldThrowPlayerMaxQuestionException() {
    Player player = ModelUtil.createPlayer();
    player.setQuestionsLimit(0);

    Mockito.when(playerRepository.findById(anyLong()))
        .thenReturn(Optional.of(player));

    playerService.askQuestion(ModelUtil.ID, ModelUtil.CODE, ModelUtil.DUMMY_QUESTION);
  }

  @Test(expected = GameOverException.class)
  public void askQuestion_WhenSavingTheQuestionsOfTheGameAndPlayerExceedHitLimit_ShouldThrowGameOverException() {
    Player player = ModelUtil.createPlayer();
    player.setHitsLimit(0);

    Mockito.when(playerRepository.findById(anyLong()))
        .thenReturn(Optional.of(player));

    playerService.askQuestion(ModelUtil.ID, ModelUtil.CODE, ModelUtil.DUMMY_QUESTION);
  }

  @Test(expected = PlayerNotFoundException.class)
  public void askQuestion_WhenSavingTheQuestionsOfTheGameAndPlayerDoNotExists_ShouldThrowPlayerNotFoundException() {
    Mockito.when(playerRepository.findById(anyLong()))
        .thenReturn(Optional.empty());

    playerService.askQuestion(ModelUtil.ID, ModelUtil.CODE, ModelUtil.DUMMY_QUESTION);
  }

  @Test
  public void getPlayer_whenSendingValidId_ShouldReturnAPlayer() {
    Optional<PlayerDto> playerDto = playerService.getPlayer(ModelUtil.ID);
    Mockito.when(playerService.getPlayer(anyLong()))
        .thenReturn(Optional.of(ModelUtil.PLAYER_DTO));

    assertEquals(ModelUtil.PLAYER_DTO, playerDto.get());
  }

  @Test(expected = PlayerNotFoundException.class)
  public void getPlayer_WhenSendingInvalidID_ShouldReturnExpcetion() {
    Mockito.doThrow(new PlayerNotFoundException(ModelUtil.ID))
        .when(playerRepository)
        .findById(anyLong());

    playerService.getPlayer(ModelUtil.ID);
  }

  @Test
  public void makeAGuess_IfThePlayerMakeAGuessAndIsCorrect_ShouldReturnTrue() {
    Mockito.when(playerRepository.findById(ModelUtil.ID))
        .thenReturn(Optional.of(ModelUtil.PLAYER));
    Mockito.when(gameRepository.getHitByCode(ModelUtil.CODE))
        .thenReturn(Optional.of(ModelUtil.CORRECT_GUESS));
    Mockito.when(hitRepository.save(any()))
        .thenReturn(ModelUtil.HIT);
    Mockito.when(playerRepository.save(any()))
        .thenReturn(ModelUtil.PLAYER);
    Mockito.when(modelMapper.map(any(), any()))
        .thenReturn(ModelUtil.HIT_DTO);
    Mockito.when(gameRepository.findByCode(ModelUtil.CODE))
        .thenReturn(Optional.of(ModelUtil.GAME));

    HitDto result = playerService.makeAGuess(ModelUtil.ID, ModelUtil.CODE, ModelUtil.CORRECT_GUESS);

    assertEquals(ModelUtil.HIT_DTO, result);
  }

  @Test(expected = GameOverException.class)
  public void makeAGuess_IfThePlayerMakeAGuessAndDoesNotHaveMoreChances_ShouldReturnGameOverException() {
    Mockito.when(gameRepository.findByCode(anyString()))
        .thenReturn(Optional.of(ModelUtil.GAME));

    Player player = ModelUtil.createPlayer();
    player.setHitsLimit(0);

    Mockito.when(playerRepository.findById(anyLong()))
        .thenReturn(Optional.of(player));

    playerService.makeAGuess(ModelUtil.ID, ModelUtil.CODE, ModelUtil.CORRECT_GUESS);

    Mockito.doThrow(new GameOverException("This player already lose."))
        .when(playerService)
        .makeAGuess(ModelUtil.ID, ModelUtil.CODE, ModelUtil.CORRECT_GUESS);
  }

  @Test(expected = PlayerNotFoundException.class)
  public void makeAGuess_IfThePlayerMakeAGuessAndDoNotExist_ShouldThrowPlayerNotFoundException() {
    Mockito.when(playerRepository.findById(any()))
        .thenReturn(Optional.empty());
    playerService.makeAGuess(ModelUtil.ID, ModelUtil.CODE, ModelUtil.CORRECT_GUESS);
  }

  @Test(expected = GameCodeNotFoundException.class)
  public void makeAGuess_IfThePlayerMakeAGuessAndTheGameDoNotExist_ShouldThrowGameNotFoundException() {
    Mockito.when(gameRepository.findByCode(any()))
        .thenReturn(Optional.empty());
    playerService.makeAGuess(ModelUtil.ID, ModelUtil.CODE, ModelUtil.CORRECT_GUESS);
  }

}
