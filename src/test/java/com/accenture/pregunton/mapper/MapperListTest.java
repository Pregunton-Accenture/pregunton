package com.accenture.pregunton.mapper;

import com.accenture.model.Question;
import com.accenture.pojo.QuestionDto;
import com.accenture.pregunton.util.ModelUtil;
import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class MapperListTest {

  private MapperList mapperList;
  private ModelMapper mapper;

  @Before
  public void setUp() {
    mapperList = new MapperList();
    mapper = new ModelMapper();
  }

  @Test
  public void mapToDtoList_WhenSendingListWithFunction_ShouldConvertEntityListToDTOList() {
    List<Question> filterQuestions = Collections.singletonList(ModelUtil.QUESTION);

    List<QuestionDto> result =
        mapperList.mapToDtoList(filterQuestions, question -> mapper.map(question, QuestionDto.class));
    List<QuestionDto> expected = Collections.singletonList(ModelUtil.QUESTION_DTO);

    assertEquals(expected.get(0)
        .getQuestion(), result.get(0)
        .getQuestion());
  }
}
