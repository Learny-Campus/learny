package com.learny.scheduleservice.controllers;

import com.learny.scheduleservice.dtos.CasualTabletCreateDto;
import com.learny.scheduleservice.entities.CasualTablet;
import com.learny.scheduleservice.services.TabletService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class    CasualTabletController {
    private final TabletService tabletService;

    @GetMapping("/casual")
    public List<?> getAllCasualTablets() {
        return tabletService.getAllTablets();
    }

    @PostMapping("/creation")
    public CasualTablet createNewCasualTablet(@RequestBody CasualTabletCreateDto casualTabletCreateDto) {
        return tabletService.createNewCasualTablet(casualTabletCreateDto);
    }

    @PutMapping("/change")
    public CasualTablet changeCasualTablet(@RequestBody CasualTablet casualTablet) {
        return tabletService.changeCasualTabletInformation(casualTablet);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteCasualTablet(@PathVariable Long id) {
        tabletService.deleteTablet(id);
        return "Tablet deleted successfully";
    }
}
