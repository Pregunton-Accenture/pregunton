package com.accenture.pregunton.controller;

import com.accenture.pregunton.service.PlayerService;
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

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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

    @Test
    public void makeAQuestion_whenValidInput_ShouldReturn200() throws Exception {

        Mockito.when(playerService.askQuestion(anyLong(), anyString(), anyString())).thenReturn(ModelUtil.QUESTION_DTO);

        mvc.perform(
                patch("/player/v1.0")
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ModelUtil.QUESTION_DTO))
                .header("playerId", ModelUtil.ID)
                .header("code", ModelUtil.CODE)
        ).andExpect(status().isOk());
    }

    @Test
    public void obtainPlayer_whenValidInput_ShouldReturn200() throws Exception {

        Mockito.when(playerService.getPlayer(anyLong())).thenReturn(Optional.of(ModelUtil.PLAYER_DTO));

        mvc.perform(
                get("/player/v1.0/{playerId}", ModelUtil.ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ModelUtil.PLAYER_DTO))
                .param("playerId", String.valueOf(ModelUtil.ID))
                .characterEncoding("utf-8")
        ).andExpect(status().isOk());

    }


}
