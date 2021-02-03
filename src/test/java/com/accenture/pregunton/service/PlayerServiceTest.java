package com.accenture.pregunton.service;

import com.accenture.pregunton.exception.GameCodeNotFoundException;
import com.accenture.pregunton.exception.GameOverException;
import com.accenture.pregunton.exception.PlayerNotFoundException;
import com.accenture.pregunton.model.Game;
import com.accenture.pregunton.model.Player;
import com.accenture.pregunton.pojo.HitDto;
import com.accenture.pregunton.pojo.PlayerDto;
import com.accenture.pregunton.pojo.QuestionDto;
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

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;

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
    Mockito.when(gameRepository.findByCode(anyString()))
        .thenReturn(Optional.of(ModelUtil.GAME));

    playerService.askQuestion(ModelUtil.ID, ModelUtil.CODE, ModelUtil.DUMMY_QUESTION);

    Mockito.verify(playerRepository, Mockito.times(1))
        .save(ModelUtil.PLAYER);
    Mockito.verify(gameRepository, Mockito.times(1))
        .save(ModelUtil.GAME);

    Mockito.when(playerService.askQuestion(ModelUtil.ID, ModelUtil.CODE, ModelUtil.DUMMY_QUESTION))
        .thenReturn(ModelUtil.QUESTION_DTO);
  }

  @Test
  public void askQuestion_IfTheGameHasNotQuestionYet_ShouldAddNewQuestion() {
    Game game = Game.builder()
        .build();

    Mockito.when(gameRepository.findByCode(anyString()))
        .thenReturn(Optional.of(game));

    playerService.askQuestion(ModelUtil.ID, ModelUtil.CODE, ModelUtil.DUMMY_QUESTION);

    Mockito.verify(playerRepository, Mockito.times(1))
        .save(ModelUtil.PLAYER);
    Mockito.verify(gameRepository, Mockito.times(1))
        .save(game);

    Mockito.when(playerService.askQuestion(ModelUtil.ID, ModelUtil.CODE, ModelUtil.DUMMY_QUESTION))
        .thenReturn(ModelUtil.QUESTION_DTO);
  }

  @Test(expected = PlayerNotFoundException.class)
  public void askQuestion_WhenSendingInvalidId_ShouldThrowPlayerNotFoundException() {
    Mockito.when(playerRepository.findById(any()))
        .thenThrow(new PlayerNotFoundException(ModelUtil.ID));
    playerService.askQuestion(ModelUtil.ID, ModelUtil.CODE, ModelUtil.DUMMY_QUESTION);
  }

  @Test(expected = GameCodeNotFoundException.class)
  public void askQuestion_WhenSavingTheQuestionsOfTheGameAndTheGameDontExist_ShouldThrowGameNotFoundException() {
    Mockito.when(gameRepository.findByCode(any()))
        .thenThrow(new GameCodeNotFoundException(ModelUtil.CODE));
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
    playerService.getPlayer(any());

    Mockito.doThrow(new PlayerNotFoundException(ModelUtil.ID))
        .when(playerRepository)
        .findById(anyLong());
  }

  @Test
  public void makeAGuess_IfThePlayerMakeAGuessAndIsCorrect_ShouldReturnTrue() {
    Mockito.when(gameRepository.findByCode(anyString()))
        .thenReturn(Optional.of(ModelUtil.GAME));
    playerService.makeAGuess(ModelUtil.ID, ModelUtil.CODE, ModelUtil.CORRECT_GUESS);
    Mockito.when(playerService.makeAGuess(ModelUtil.ID, ModelUtil.CODE, ModelUtil.CORRECT_GUESS))
        .thenReturn(ModelUtil.HIT_DTO);
  }

  @Test(expected = GameOverException.class)
  public void makeAGuess_IfThePlayerMakeAGuessAndDontHaveMoreChances_ShouldReturnGameOverException() {
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
        .thenThrow(new PlayerNotFoundException(ModelUtil.ID));
    playerService.makeAGuess(ModelUtil.ID, ModelUtil.CODE, ModelUtil.CORRECT_GUESS);
  }

  @Test(expected = GameCodeNotFoundException.class)
  public void makeAGuess_IfThePlayerMakeAGuessAndTheGameDontExist_ShouldThrowGameNotFoundException() {
    Mockito.when(gameRepository.findByCode(any()))
        .thenThrow(new GameCodeNotFoundException(ModelUtil.CODE));
    playerService.makeAGuess(ModelUtil.ID, ModelUtil.CODE, ModelUtil.CORRECT_GUESS);
  }

}
