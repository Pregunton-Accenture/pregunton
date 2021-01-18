package com.accenture.pregunton.pojo;

import com.accenture.pregunton.model.Player;
import com.accenture.pregunton.model.Question;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class GameDto implements Serializable {

    @ApiModelProperty(notes = "The category of the game", name = "category", required = true)
    @NotNull(message = "The game category cannot be missing or empty")
    private Category category;

    private String hit;

    @ApiModelProperty(notes = "The rules of the game", name = "rules", required = true)
    @NotNull(message = "The game rules cannot be missing or empty")
    private Set<RuleDto> rules;

    @ApiModelProperty(notes = "The players of the game", name = "players", required = false)
    private List<PlayerDto> players;

    @ApiModelProperty(notes = "The questions the players asked", name = "questions", required = false)
    private List<Question> questions;

}
