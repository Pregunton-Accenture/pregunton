package com.accenture.pregunton.service;

import com.accenture.model.Rules;
import com.accenture.pregunton.repository.RulesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class RulesService {

  @Autowired
  private RulesRepository rulesRepository;

  /**
   * Save the rules from a current game.
   *
   * @param rule the rule data
   *
   * @return the rules saved on database.
   */
  public Rules save(Rules rule) {
    Objects.requireNonNull(rule);
    return rulesRepository.save(rule);
  }
}
