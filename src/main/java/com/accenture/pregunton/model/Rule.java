package com.accenture.pregunton.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@ToString(exclude = {"game"})
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "rules")
public class Rule implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name_rule")
    private String nameRule;
    @Column(name = "value")
    private String value;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "id_game")
    @JsonIgnore
    private Game game;

}
