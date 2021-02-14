package com.accenture.pregunton.controller;

import com.accenture.pojo.QuestionDto;
import com.accenture.pregunton.service.QuestionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("questions")
@Api(tags = "Question API", description = "This API has operations related to Question Controller")
public class QuestionController {

  @Autowired
  private QuestionService questionService;

  @PatchMapping("v1.0")
  @ApiOperation("Updates the answer of a question.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Ok"),
      @ApiResponse(code = 400, message = "Bad Request"),
      @ApiResponse(code = 404, message = "Not Found"),
      @ApiResponse(code = 500, message = "Internal Server Error"),
  })
  public ResponseEntity<Void> update(@RequestBody QuestionDto questionDto) {
    questionService.updateAnswer(questionDto);
    return ResponseEntity.ok()
        .build();
  }
}
