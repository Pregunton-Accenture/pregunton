package com.accenture.pregunton.exception;

public class GameCodeNotFoundException extends RuntimeException {

  public GameCodeNotFoundException(String code) {
    super("Game not found with code: " + code);
  }

}
