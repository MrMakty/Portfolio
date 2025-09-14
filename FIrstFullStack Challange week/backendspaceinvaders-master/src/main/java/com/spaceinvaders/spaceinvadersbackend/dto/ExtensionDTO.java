package com.spaceinvaders.spaceinvadersbackend.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Date;

public class ExtensionDTO {

    @NotNull(message = "Start timestamp is required")
    @Future(message = "Start timestamp must be in the future")
    private Date newStartTimestamp;

    // Getters and Setters

    public Date getNewStartTimestamp() {
        return newStartTimestamp;
    }

    public void setNewStartTimestamp(Date newStartTimestamp) {
        this.newStartTimestamp = newStartTimestamp;
    }
}
