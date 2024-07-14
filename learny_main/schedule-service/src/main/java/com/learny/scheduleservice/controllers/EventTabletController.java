package com.learny.scheduleservice.controllers;

import com.learny.scheduleservice.dtos.EventTabletCreateDto;
import com.learny.scheduleservice.entities.EventTablet;
import com.learny.scheduleservice.services.TabletService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventTabletController {
    private final TabletService tabletService;

    @GetMapping()
    public List<?> getAllEventTablets() {
        return tabletService.getAllTablets();
    }

    @PostMapping("/creation")
    public EventTablet createNewEventTablet(@RequestBody EventTabletCreateDto eventTabletCreateDto) {
        return tabletService.createNewEventTablet(eventTabletCreateDto);
    }

    @PutMapping("/change")
    public EventTablet changeEventTablet(@RequestBody EventTablet eventTablet) {
        return tabletService.changeEventTabletInformation(eventTablet);
    }

    @DeleteMapping("/delete{id}")
    public String deleteEventTablet(@PathVariable Long id) {
        tabletService.deleteTablet(id);
        return "Event tablet deleted successfully";
    }
}
