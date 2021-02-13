package com.accenture.pregunton.controller;

import com.accenture.pojo.Answer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("answers")
@Api(tags = "Answer API", description = "This API has operations related to Answer Controller")
public class AnswerController {

  @GetMapping("v1.0")
  @ApiOperation("Retrieves a list of answers.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Ok"),
  })
  public ResponseEntity<Answer[]> getAll() {
    return ResponseEntity.ok(Answer.values());
  }
}
