package com.accenture.pregunton.exception;

public class GameIdNotFoundException extends RuntimeException {

  public GameIdNotFoundException(Long id) {
    super("Game not found with id: " + id);
  }

}
