package com.shulpov.spots_app.controllers;

import com.shulpov.spots_app.dto.SpotDto;
import com.shulpov.spots_app.dto.SpotTypeDto;
import com.shulpov.spots_app.models.SpotType;
import com.shulpov.spots_app.services.SpotService;
import com.shulpov.spots_app.services.SpotTypeService;
import com.shulpov.spots_app.utils.DtoConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/spot-types/")
public class SpotTypeController {
    private final SpotTypeService spotTypeService;
    private final static Logger logger = LoggerFactory.getLogger(SpotTypeController.class);

    @Autowired
    public SpotTypeController(SpotTypeService spotTypeService) {
        this.spotTypeService = spotTypeService;
    }

    //Получить все типы спотов
    @GetMapping("/get-all")
    public List<SpotTypeDto> getAllSpots() {
        logger.atInfo().log("/get-all");
        return spotTypeService.getAll().stream().map(DtoConverter::spotTypeToDto).toList();
    }
}