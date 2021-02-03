package com.accenture.pregunton.util;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@ToString
@Builder
public class SimpleResponse {

  private String message;
  private HttpStatus status;

}