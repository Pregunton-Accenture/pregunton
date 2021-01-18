package com.accenture.pregunton.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
public class QuestionDto implements Serializable {

    @ApiModelProperty(notes = "The question that the player asked.", name = "question", required = true)
    @NotNull(message = "The hit limit of the player cannot be missing or empty")
    private String question;

    @ApiModelProperty(notes = "The answer that the master gave to that question.", name = "answer", required = false)
    private Answer answer;

}
