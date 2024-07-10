package com.learny.scheduleservice.dtos;

import lombok.Value;

import java.io.Serializable;
import java.util.Date;

@Value
public class CasualTabletCreateDto implements Serializable {
    Long id;
    String lessonName;
    String groupNumber;
    String officeNumber;
    Date createdAt;
    Date startedAt;
    Date endedAt;
}
