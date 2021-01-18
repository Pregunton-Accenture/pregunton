package com.accenture.pregunton.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class PlayerDto implements Serializable {

    @ApiModelProperty(notes = "The hit limit of a given player.", name = "hitsLimit", required = true)
    @NotNull(message = "The hit limit of the player cannot be missing or empty")
    private int hitsLimit;

    @ApiModelProperty(notes = "The nick name of the player", name = "nickName", required = true)
    @NotNull(message = "The nick name cannot be missing or empty")
    private String nickName;

    @ApiModelProperty(notes = "The questions asked of a certain player.", name = "hitsLimit")
    private List<QuestionDto> questions;

}
