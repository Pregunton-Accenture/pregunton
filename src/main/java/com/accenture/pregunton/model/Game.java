package com.accenture.pregunton.model;

import com.accenture.pregunton.pojo.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString(exclude = {"players", "questions", "rules"})
@Builder
@Entity
@Table(name = "games")
public class Game implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private Category category;
    @Column(name = "hit")
    private String hit;
    @OneToMany(mappedBy = "game", cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH, CascadeType.MERGE})
    private Set<Rule> rules;
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.DETACH})
    @JoinTable(
            name = "games_players",
            joinColumns = @JoinColumn(name = "id_game", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_player", referencedColumnName = "id")
    )
    private List<Player> players;
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.DETACH})
    @JoinTable(
            name = "games_questions",
            joinColumns = @JoinColumn(name = "id_game", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_question", referencedColumnName = "id")
    )
    private List<Question> questions;

}
