package com.accenture.pregunton.pojo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PlayerRequestDto {

    @ApiModelProperty(notes = "The hit limit of a given player.", name = "hitsLimit", required = true)
    @NotNull(message = "The hit limit of the player cannot be missing or empty")
    private int hitsLimit;

    @ApiModelProperty(notes = "The nick name of the player", name = "nickName", required = true)
    @NotNull(message = "The nick name cannot be missing or empty")
    private String nickName;

}
