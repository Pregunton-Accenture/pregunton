package com.accenture.pregunton.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "rules")
public class Rules implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "hit_limit")
  private Integer hitLimit;

  @Column(name = "question_limit")
  private Integer questionLimit;

}
