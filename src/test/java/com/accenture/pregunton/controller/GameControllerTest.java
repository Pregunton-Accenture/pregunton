package com.accenture.pregunton.controller;

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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

        mvc.perform(
                post("/pregunton/")
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(ModelUtil.GAME_DTO))
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("utf-8")
                    .header("masterId", String.valueOf(2L))
        ).andExpect(status().isCreated());

    }

    @Test
    public void whenNotValidInputCreateGame_thenReturns400() throws Exception{

        mvc.perform(
                post("/pregunton/")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ModelUtil.GAME_DTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
        ).andExpect(status().isBadRequest());

    }

    @Test
    public void whenThrowException_thenReturns500() throws Exception {

        Mockito.doThrow(new RuntimeException("500")).when(gameService).create(any(GameDto.class), anyLong());

        mvc.perform(
                post("/pregunton/")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ModelUtil.GAME_DTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("masterId", String.valueOf(2L))
                        .characterEncoding("utf-8")
        ).andExpect(status().is5xxServerError());

    }

}
