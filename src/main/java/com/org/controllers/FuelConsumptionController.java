package com.org.controllers;

import com.org.FuelAppException;
import com.org.model.Fuel;
import com.org.services.FuelConsumptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/consumption/register")
public class FuelConsumptionController {

    @Autowired
    private FuelConsumptionService fuelConsumptionService;

    @PostMapping
    public Fuel register(@RequestBody Fuel fuel) {
        return fuelConsumptionService.saveFuel(fuel);
    }

    @PostMapping(value = "/bulk", consumes = "multipart/form-data")
    public List<Fuel> bulkRegister(@RequestParam("file") MultipartFile file) throws IOException, FuelAppException {
        return fuelConsumptionService.saveFuelFromFile(file);
    }
}
