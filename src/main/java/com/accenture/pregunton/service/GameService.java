package com.accenture.pregunton.service;

import com.accenture.pregunton.pojo.GameDto;
import com.accenture.pregunton.repository.GameRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private ModelMapper mapper;

    public void create(GameDto gameDto, Long masterId) throws RuntimeException{

    }

}
