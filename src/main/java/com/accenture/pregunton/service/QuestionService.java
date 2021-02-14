package com.accenture.pregunton.service;

import com.accenture.model.Question;
import com.accenture.pojo.Answer;
import com.accenture.pojo.QuestionDto;
import com.accenture.pregunton.exception.QuestionIdNotFoundException;
import com.accenture.pregunton.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class QuestionService {

  @Autowired
  private QuestionRepository questionRepository;

  public void updateAnswer(QuestionDto questionDto) {
    Objects.requireNonNull(questionDto);
    Objects.requireNonNull(questionDto.getId());
    Question question = questionRepository.findById(questionDto.getId())
        .orElseThrow(() -> new QuestionIdNotFoundException(questionDto.getId()));

    question.setAnswer(questionDto.getAnswer());
    questionRepository.save(question);
  }
}
