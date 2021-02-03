package com.accenture.pregunton.mapper;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class MapperList {

  public <E, R> List<R> mapToDtoList(List<E> entities, Function<E, R> mapper) {
    return Optional.ofNullable(entities)
        .orElse(Collections.emptyList())
        .stream()
        .map(mapper)
        .collect(Collectors.toList());
  }

}
