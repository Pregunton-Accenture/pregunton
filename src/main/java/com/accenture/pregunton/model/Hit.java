package com.accenture.pregunton.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ToString(exclude = {"player"})
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
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime published;
    @OneToOne(cascade = CascadeType.ALL)
    private Player player;

}
