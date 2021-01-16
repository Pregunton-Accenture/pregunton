package com.accenture.pregunton.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RuleDto {

    @ApiModelProperty(notes = "The rules of the game", name = "rules", required = true)
    @NotNull(message = "The name rule cannot be missing or empty")
    private String nameRule;

    @ApiModelProperty(notes = "The category of the game", name = "value", required = true)
    @NotNull(message = "The value of the rule cannot be missing or empty")
    private String value;

}
