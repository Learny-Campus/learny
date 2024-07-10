package com.learny.scheduleservice.dtos;

import lombok.Value;

import java.io.Serializable;
import java.util.Date;

@Value
public class EventTabletCreateDto implements Serializable {
    Long id;
    String lessonName;
    String eventName;
    Date createdAt;
    Date startedAt;
    Date endedAt;
}
