package com.accenture.pregunton.controller;

import com.accenture.pregunton.exception.GameNotFoundException;
import com.accenture.pregunton.pojo.GameDto;
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
    public void whenValidInputCreateGame_thenReturns201() throws Exception {
        Mockito.when(gameService.create(ModelUtil.GAME_DTO, ModelUtil.ID, ModelUtil.ID)).thenReturn(ModelUtil.GAME);

        mvc.perform(
                post("/games/v1.0/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("utf-8")
                    .content(objectMapper.writeValueAsString(ModelUtil.GAME))
                    .header("masterId", String.valueOf(2L))
                    .header("categoryId", String.valueOf(2L))
        ).andExpect(status().isCreated());

    }

    @Test
    public void whenNotValidInputCreateGame_thenReturns400() throws Exception{

        mvc.perform(
                post("/games/v1.0/")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ModelUtil.GAME_DTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
        ).andExpect(status().isBadRequest());

    }

    @Test
    public void whenThrowException_thenReturns500() throws Exception {

        Mockito.doThrow(new RuntimeException("500")).when(gameService).create(any(GameDto.class), anyLong(), anyLong());

        mvc.perform(
                post("/games/v1.0/")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ModelUtil.GAME_DTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("masterId", String.valueOf(2L))
                        .header("categoryId", String.valueOf(2L))
                        .characterEncoding("utf-8")
        ).andExpect(status().is5xxServerError());

    }

    @Test
    public void whenDeletingGame_thenReturn204() throws Exception {
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
    public void whenGetAGame_thenReturns200() throws Exception {

        Mockito.when(gameService.getOne(ModelUtil.ID)).thenReturn(Optional.of(ModelUtil.GAME_DTO));

        mvc.perform(
                get("/games/v1.0/{gameId}", ModelUtil.ID)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
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

}
