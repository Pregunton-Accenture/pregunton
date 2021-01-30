package com.accenture.pregunton.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RulesDto {

  @ApiModelProperty(notes = "The player's max hits amount of the game", name = "hitLimit", required = true)
  @NotNull(message = "The hit limit rule cannot be missing or empty")
  private Integer hitLimit;

  @ApiModelProperty(notes = "The player's max questions amount of the game", name = "questionLimit", required = true)
  @NotNull(message = "The question limit of the rule cannot be missing or empty")
  private Integer questionLimit;

}
