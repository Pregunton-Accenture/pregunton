package com.accenture.pregunton.exception;

public class GameOverException extends RuntimeException {

    public GameOverException(String nickname) {
        super("Player " + nickname + " already lose");
    }

}
