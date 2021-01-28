package com.accenture.pregunton.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
public class HitDto implements Serializable {

    @ApiModelProperty(notes = "The player guess of the game", name = "guess", required = true)
    @NotNull(message = "The player guess cannot be missing or empty")
    private String guess;

    @ApiModelProperty(notes = "The answer of the right guess", name = "isCorrect")
    private Boolean isCorrect;

}
