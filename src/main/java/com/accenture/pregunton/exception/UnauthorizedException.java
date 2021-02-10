package com.accenture.pregunton.exception;

import lombok.Getter;

@Getter
public class UnauthorizedException extends RuntimeException {

  private String path;

  public UnauthorizedException(String path) {
    this.path = path;
  }
}
