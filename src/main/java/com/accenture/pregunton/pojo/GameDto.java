package com.accenture.pregunton.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class GameDto implements Serializable {

    @ApiModelProperty(notes = "Game id", name = "id")
    private Long id;

}
