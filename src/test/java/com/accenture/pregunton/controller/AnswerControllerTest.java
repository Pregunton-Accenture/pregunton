package com.accenture.pregunton.controller;

import com.accenture.pojo.Answer;
import com.accenture.pregunton.service.CategoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AnswerController.class)
public class AnswerControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @InjectMocks
  private AnswerController answerController;
  @Autowired
  private ObjectMapper objectMapper;
  @MockBean
  private RestTemplate restTemplate;

  @Test
  public void getAll_ShouldReturnAListOfAllAnswers() throws Exception {
    doReturn(ResponseEntity.ok(false)).when(restTemplate)
        .exchange(any(), eq(HttpMethod.POST), any(), eq(Boolean.class));

    this.mockMvc.perform(get("/answers/v1.0"))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(Answer.values())));
  }
}
