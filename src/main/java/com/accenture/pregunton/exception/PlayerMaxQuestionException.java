package com.accenture.pregunton.exception;

public class PlayerMaxQuestionException extends RuntimeException {

  public PlayerMaxQuestionException(String nickname) {
    super("Player " + nickname + " exceeded the max number of questions");
  }
}
