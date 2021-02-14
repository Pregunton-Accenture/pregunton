package com.accenture.pregunton.exception;

public class PlayerNotFoundException extends RuntimeException {

  public PlayerNotFoundException(String nickName) {
    super("Player not found with nickname: " + nickName);
  }

}
