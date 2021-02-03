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

    @ApiModelProperty(notes = "The nick name of the player", name = "nickName", required = true)
    @NotNull(message = "The nick name cannot be missing or empty")
    private String nickName;
}
