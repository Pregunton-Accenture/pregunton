package com.accenture.pregunton.model;

import com.accenture.pregunton.pojo.Answer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString(exclude = {"player"})
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "questions")
public class Question implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "question")
    private String question;

    @Column(name = "published")
    private LocalDateTime published;

    @Column(name = "answer")
    @Enumerated(EnumType.STRING)
    private Answer answer;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_player")
    @JsonIgnore
    private Player player;

}
