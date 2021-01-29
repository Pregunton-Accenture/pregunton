package com.accenture.pregunton.controller;

import com.accenture.pregunton.exception.CategoryNotFoundException;
import com.accenture.pregunton.exception.GameNotFoundException;
import com.accenture.pregunton.service.GameService;
import com.accenture.pregunton.util.ModelUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(GameController.class)
public class GameControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @InjectMocks
    private GameController subject;
    @MockBean
    private GameService gameService;

    @Test
    public void createGame_WhenValidInputCreateGame_ThenReturns201() throws Exception {

        Mockito.doReturn(ModelUtil.GAME).when(gameService).create(any(), anyLong(), anyLong());

        mvc.perform(
                post("/games/v1.0/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("utf-8")
                    .content(objectMapper.writeValueAsString(ModelUtil.GAME_DTO))
                    .header("masterId", ModelUtil.ID)
                    .header("categoryId", ModelUtil.ID)
        ).andExpect(status().isCreated());

    }

    @Test
    public void createGame_WhenSendingInvalidCategoryId_ShouldThrowCategoryNotFoundException() throws Exception {

        Mockito.when(gameService.create(any(), anyLong(), anyLong()))
                .thenThrow(new CategoryNotFoundException("Category not found"));

        mvc.perform(
                post("/games/v1.0/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("utf-8")
                    .content(objectMapper.writeValueAsString(ModelUtil.GAME_DTO))
                    .header("masterId", ModelUtil.ID)
                    .header("categoryId", ModelUtil.ID)
        ).andExpect(status().is4xxClientError());

    }

    @Test
    public void deleteGame_WhenDeletingGame_ThenReturn204() throws Exception {
        gameService.delete(ModelUtil.ID);
        Mockito.verify(gameService, Mockito.times(1)).delete(ModelUtil.ID);

        mvc.perform(
                delete("/games/v1.0/{gameId}", ModelUtil.ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .param("gameId", String.valueOf(ModelUtil.ID))
        ).andExpect(status().is2xxSuccessful());

    }

    @Test
    public void obtainGame_WhenGetAGame_ThenReturns200() throws Exception {

        Mockito.when(gameService.getOne(ModelUtil.ID)).thenReturn(Optional.of(ModelUtil.GAME_DTO));

        mvc.perform(
                get("/games/v1.0/game/{gameId}", ModelUtil.ID)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(ModelUtil.GAME_DTO))
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .param("gameId", String.valueOf(ModelUtil.ID))
                    .characterEncoding("utf-8")
        ).andExpect(status().is2xxSuccessful());

    }

    @Test
    public void obtainGame_WhenTryingToGetAGameThatNoneExists_ThenReturns400() throws Exception {

        Mockito.when(gameService.getOne(ModelUtil.ID))
                .thenThrow(new GameNotFoundException("Game not found with id: " + ModelUtil.ID));

        mvc.perform(
                get("/games/v1.0/{gameId}", ModelUtil.ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
        ).andExpect(status().is4xxClientError());
    }

    @Test
    public void addPlayer_WhenSendingValidDataOfAPlayer_ShouldReturn204() throws Exception {
        gameService.addOnePlayer(ModelUtil.ID, ModelUtil.PLAYER_REQUEST_DTO);

        Mockito.verify(gameService, Mockito.times(1)).addOnePlayer(ModelUtil.ID, ModelUtil.PLAYER_REQUEST_DTO);

        mvc.perform(
                patch("/games/v1.0/{gameId}", ModelUtil.ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ModelUtil.PLAYER_DTO))
                        .characterEncoding("utf-8")
        ).andExpect(status().is2xxSuccessful());

    }

    @Test
    public void getGameQuestions_WhenSendingValidCodeOfAGame_ShouldReturn200() throws Exception {

        Mockito.when(gameService.obtainQuestions(ModelUtil.CODE))
                .thenReturn(Stream.of(ModelUtil.QUESTION_DTO).collect(Collectors.toList()));

        mvc.perform(
                get("/games/v1.0/{code}", ModelUtil.CODE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("utf-8")
                    .header("code", ModelUtil.CODE)
        ).andExpect(status().isOk());

    }

    @Test
    public void getGameQuestions_WhenSendingInvalidCodeOfAGame_ShouldThrowGameNotFoundException() throws Exception {

        Mockito.when(gameService.obtainQuestions(any())).thenThrow(new GameNotFoundException("Game Not Found."));

        mvc.perform(
                get("/games/v1.0/{code}", ModelUtil.CODE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .header("code", ModelUtil.CODE)
        ).andExpect(status().is4xxClientError());

    }

}
