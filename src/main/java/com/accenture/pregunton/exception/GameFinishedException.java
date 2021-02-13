package com.accenture.pregunton.exception;

public class GameFinishedException extends RuntimeException {
  public GameFinishedException() {
    super("The Game already finished.");
  }
}
