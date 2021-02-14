package com.accenture.pregunton.controller;

import com.accenture.pregunton.exception.QuestionIdNotFoundException;
import com.accenture.pregunton.service.QuestionService;
import com.accenture.pregunton.util.ModelUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(QuestionController.class)
public class QuestionControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @InjectMocks
  private QuestionController questionController;
  @Autowired
  private ObjectMapper objectMapper;
  @MockBean
  private RestTemplate restTemplate;
  @MockBean
  private QuestionService questionService;

  @Test
  public void update_WhenSendValidQuestionDto_ShouldReturnOk() throws Exception {
    doReturn(ResponseEntity.ok(false)).when(restTemplate)
        .exchange(any(), eq(HttpMethod.POST), any(), eq(Boolean.class));
    doNothing().when(questionService)
        .updateAnswer(any());

    this.mockMvc.perform(patch("/questions/v1.0").contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(ModelUtil.QUESTION_DTO)))
        .andExpect(status().isOk());

    verify(questionService, times(1)).updateAnswer(any());
  }

  @Test
  public void update_WhenSendInvalidQuestionDtoId_ShouldReturnNotFound() throws Exception {
    doReturn(ResponseEntity.ok(false)).when(restTemplate)
        .exchange(any(), eq(HttpMethod.POST), any(), eq(Boolean.class));
    doThrow(QuestionIdNotFoundException.class).when(questionService)
        .updateAnswer(any());

    this.mockMvc.perform(patch("/questions/v1.0").contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(ModelUtil.QUESTION_DTO)))
        .andExpect(status().isNotFound());
  }
}
