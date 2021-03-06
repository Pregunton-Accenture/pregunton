package com.accenture.pregunton.controller;

import com.accenture.pregunton.exception.GameOverException;
import com.accenture.pregunton.exception.LastQuestionNotAnswerException;
import com.accenture.pregunton.exception.PlayerNotFoundException;
import com.accenture.pregunton.service.PlayerService;
import com.accenture.pregunton.util.ModelUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PlayerController.class)
public class PlayerControllerTest {

  @Autowired
  private MockMvc mvc;
  @Autowired
  private ObjectMapper objectMapper;
  @InjectMocks
  private PlayerController subject;
  @MockBean
  private PlayerService playerService;
  @MockBean
  private RestTemplate restTemplate;

  @BeforeEach
  public void setUp() {
    doReturn(ResponseEntity.ok(false)).when(restTemplate)
        .exchange(any(), eq(HttpMethod.POST), any(), eq(Boolean.class));
  }

  @Test
  public void makeAQuestion_whenValidInput_ShouldReturn200() throws Exception {

    Mockito.when(playerService.askQuestion(anyString(), anyString(), anyString()))
        .thenReturn(ModelUtil.QUESTION_DTO);

    mvc.perform(patch("/players/v1.0/{nickName}/questions", ModelUtil.NICK_NAME).characterEncoding("utf-8")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(ModelUtil.QUESTION_DTO))
        .header("code", ModelUtil.CODE))
        .andExpect(status().isOk());
  }

  @Test
  public void makeAQuestion_whenAPlayerWantToAskAnotherQuestionWhenHisLastQuestionWasNotAnswer_ShouldThrowLastQuestionNotAnswerException()
      throws Exception {
    Mockito.when(playerService.askQuestion(any(), any(), any()))
        .thenThrow(new LastQuestionNotAnswerException(ModelUtil.PLAYER_DTO.getNickName()));

    mvc.perform(patch("/players/v1.0/{nickName}/questions", ModelUtil.NICK_NAME).characterEncoding("utf-8")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(ModelUtil.QUESTION_DTO))
        .header("code", ModelUtil.CODE))
        .andExpect(status().is4xxClientError());
  }

  @Test
  public void obtainPlayer_whenValidInput_ShouldReturn200() throws Exception {

    Mockito.when(playerService.getPlayer(anyString()))
        .thenReturn(Optional.of(ModelUtil.PLAYER_DTO));

    mvc.perform(get("/players/v1.0/{nickName}", ModelUtil.NICK_NAME).contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(ModelUtil.PLAYER_DTO))
        .param("nickName", String.valueOf(ModelUtil.NICK_NAME))
        .characterEncoding("utf-8"))
        .andExpect(status().isOk());

  }

  @Test
  public void obtainPlayer_whenValidInputButEmptyOptional_ShouldReturn204() throws Exception {

    Mockito.when(playerService.getPlayer(anyString()))
        .thenReturn(Optional.empty());

    mvc.perform(get("/players/v1.0/{nickName}", ModelUtil.NICK_NAME).contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(ModelUtil.PLAYER_DTO))
        .param("nickName", String.valueOf(ModelUtil.NICK_NAME))
        .characterEncoding("utf-8"))
        .andExpect(status().isNoContent());

  }

  @Test
  public void obtainPlayer_WhenInvalidInput_ShouldReturn404Exception() throws Exception {

    Mockito.doThrow(new PlayerNotFoundException(ModelUtil.NICK_NAME))
        .when(playerService)
        .getPlayer(any());

    mvc.perform(get("/player/v1.0/{nickName}", ModelUtil.NICK_NAME).contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(ModelUtil.PLAYER_DTO))
        .param("nickName", String.valueOf(ModelUtil.NICK_NAME))
        .characterEncoding("utf-8"))
        .andExpect(status().is4xxClientError());

  }

  @Test
  public void makeAGuess_WhenAPlayerMakeAGuess_ShouldReturn200() throws Exception {

    Mockito.when(playerService.makeAGuess(ModelUtil.NICK_NAME, ModelUtil.CODE, ModelUtil.CORRECT_GUESS))
        .thenReturn(ModelUtil.HIT_DTO);

    mvc.perform(post("/players/v1.0/{nickName}/guess", ModelUtil.NICK_NAME).contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(ModelUtil.HIT_DTO))
        .header("code", ModelUtil.CODE)
        .characterEncoding("utf-8"))
        .andExpect(status().isOk());

  }

  @Test
  public void makeAGuess_WhenAPlayerHasNoMoreChances_ShouldReturn400Exception() throws Exception {

    Mockito.when(playerService.makeAGuess(any(), any(), any()))
        .thenThrow(new GameOverException(ModelUtil.PLAYER_DTO.getNickName()));

    mvc.perform(post("/players/v1.0/{nickName}/guess", ModelUtil.NICK_NAME).contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(ModelUtil.HIT_DTO))
        .header("code", ModelUtil.CODE)
        .characterEncoding("utf-8"))
        .andExpect(status().is4xxClientError());

  }

  @Test
  public void obtainPlayer_WhenSendinInvalidPlayerId_ShouldThrowPlayerNotFoundException() throws Exception {

    Mockito.when(playerService.getPlayer(any()))
        .thenThrow(new PlayerNotFoundException(ModelUtil.NICK_NAME));

    mvc.perform(get("/players/v1.0/{nickName}", ModelUtil.NICK_NAME).contentType(MediaType.APPLICATION_JSON)
        .characterEncoding("utf-8")
        .content(objectMapper.writeValueAsString(ModelUtil.PLAYER_DTO))
        .param("nickName", String.valueOf(ModelUtil.NICK_NAME)))
        .andExpect(status().is4xxClientError());
  }

}
