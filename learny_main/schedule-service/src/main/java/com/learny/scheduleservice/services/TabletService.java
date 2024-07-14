package com.learny.scheduleservice.services;

import com.learny.scheduleservice.dtos.CasualTabletCreateDto;
import com.learny.scheduleservice.dtos.EventTabletCreateDto;
import com.learny.scheduleservice.entities.CasualTablet;
import com.learny.scheduleservice.entities.EventTablet;
import com.learny.scheduleservice.repositories.CasualTabletJpaRepository;
import com.learny.scheduleservice.repositories.EventTabletJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TabletService {
    private final CasualTabletJpaRepository casualTabletJpaRepository;
    private final EventTabletJpaRepository eventTabletJpaRepository;

    public List<?> getAllTablets() {
        return List.of();
    }

    public CasualTablet createNewCasualTablet(CasualTabletCreateDto casualTabletCreateDto) {
        try {
            CasualTablet casualTablet = new CasualTablet();
            casualTablet.setId(casualTabletCreateDto.getId());
            casualTablet.setLessonName(casualTabletCreateDto.getLessonName());
            casualTablet.setOfficeNumber(casualTabletCreateDto.getOfficeNumber());
            casualTablet.setCreatedAt(casualTabletCreateDto.getCreatedAt());
            casualTablet.setStartedAt(casualTabletCreateDto.getStartedAt());
            casualTablet.setEndedAt(casualTabletCreateDto.getEndedAt());
            return casualTablet;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public EventTablet createNewEventTablet(EventTabletCreateDto eventTabletCreateDto) {
        try {
            EventTablet eventTablet = new EventTablet();
            eventTablet.setId(eventTabletCreateDto.getId());
            eventTablet.setLessonName(eventTabletCreateDto.getLessonName());
            eventTablet.setCreatedAt(eventTabletCreateDto.getCreatedAt());
            eventTablet.setStartedAt(eventTabletCreateDto.getStartedAt());
            eventTablet.setEndedAt(eventTabletCreateDto.getEndedAt());
            return eventTablet;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public CasualTablet changeCasualTabletInformation(CasualTablet casualTablet) {
        return casualTabletJpaRepository.save(casualTablet);
    }

    public EventTablet changeEventTabletInformation(EventTablet eventTablet) {
        return eventTabletJpaRepository.save(eventTablet);
    }

    public void deleteTablet(Long id) {
        casualTabletJpaRepository.deleteById(id);
    }
}
