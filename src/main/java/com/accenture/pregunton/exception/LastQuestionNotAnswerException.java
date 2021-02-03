package com.accenture.pregunton.exception;

public class LastQuestionNotAnswerException extends RuntimeException {

  public LastQuestionNotAnswerException(String nickname) {
    super("The last question of " + nickname + " wasn't answered yet");
  }

}
