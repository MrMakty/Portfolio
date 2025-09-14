package com.spaceinvaders.spaceinvadersbackend.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;
import java.util.Date;

public class ReservationDTO {
    @NotNull(message = "Dock ID is required")
    private Long dockId;

    @NotNull(message = "Spaceship ID is required")
    private Long spaceshipId;
    @NotNull(message = "Start timestamp is required")
    @Future(message = "Start timestamp must be in the future")
    private Date startTimestamp;

    @NotNull(message = "End timestamp is required")
    @Future(message = "End timestamp must be in the future")
    private Date endTimestamp;

    // Getters and Setters


    public Long getDockId() {
        return dockId;
    }

    public void setDockId(Long dockId) {
        this.dockId = dockId;
    }

    public Long getSpaceshipId() {
        return spaceshipId;
    }

    public void setSpaceshipId(Long spaceshipId) {
        this.spaceshipId = spaceshipId;
    }

    public Date getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(Date startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public Date getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(Date endTimestamp) {
        this.endTimestamp = endTimestamp;
    }
}
