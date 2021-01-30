package com.accenture.pregunton.service;

import com.accenture.pregunton.model.Rules;
import com.accenture.pregunton.repository.RulesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class RulesService {

  @Autowired
  private RulesRepository rulesRepository;

  public Rules save(Rules rule) {
    Objects.requireNonNull(rule);
    return rulesRepository.save(rule);
  }
}
