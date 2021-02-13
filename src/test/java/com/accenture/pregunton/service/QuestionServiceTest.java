package com.accenture.pregunton.service;

import com.accenture.pojo.QuestionDto;
import com.accenture.pregunton.exception.QuestionIdNotFoundException;
import com.accenture.pregunton.repository.QuestionRepository;
import com.accenture.pregunton.util.ModelUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class QuestionServiceTest {

  @InjectMocks
  private QuestionService questionService;

  @Mock
  private QuestionRepository questionRepository;

  @Test
  public void updateAnswer_WhenSendValidQuestion_ShouldCallSaveOnce() {
    doReturn(Optional.of(ModelUtil.QUESTION)).when(questionRepository)
        .findById(ModelUtil.QUESTION_DTO.getId());
    doReturn(ModelUtil.QUESTION).when(questionRepository)
        .save(ModelUtil.QUESTION);

    questionService.updateAnswer(ModelUtil.QUESTION_DTO);

    verify(questionRepository, times(1)).save(ModelUtil.QUESTION);
  }

  @Test
  public void updateAnswer_WhenSendNullQuestion_ShouldThrowNullPointerException() {
    assertThrows(NullPointerException.class, () -> questionService.updateAnswer(null));
  }

  @Test
  public void updateAnswer_WhenSendNullIdQuestion_ShouldThrowNullPointerException() {
    QuestionDto questionDto = new QuestionDto();
    questionDto.setId(null);
    assertThrows(NullPointerException.class, () -> questionService.updateAnswer(questionDto));
  }

  @Test
  public void updateAnswer_WhenSendNotExistingQuestion_ShouldThrowQuestionIdNotFoundException() {
    doReturn(Optional.empty()).when(questionRepository)
        .findById(ModelUtil.QUESTION_DTO.getId());

    assertThrows(QuestionIdNotFoundException.class, () -> questionService.updateAnswer(ModelUtil.QUESTION_DTO));
  }
}
