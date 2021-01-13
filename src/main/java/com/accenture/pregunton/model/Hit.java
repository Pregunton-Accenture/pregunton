package com.accenture.pregunton.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString(exclude = {"player"})
@Builder
@Entity
@Table(name = "hits")
public class Hit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "is_correct")
    private Boolean isCorrect;
    @Column(name = "published")
    private LocalDateTime published;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_player")
    private Player player;

}
