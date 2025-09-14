package com.spaceinvaders.spaceinvadersbackend.dto;

import java.time.LocalDateTime;
import java.util.Date;

public class DockStatusDTO {

    private DockStatus status;
    private Date reservedUntil;
    private Date availableFrom;

    public DockStatusDTO(DockStatus status, Date reservedUntil, Date availableFrom) {
        this.status = status;
        this.reservedUntil = reservedUntil;
        this.availableFrom = availableFrom;
    }

    public DockStatus getStatus() {
        return status;
    }

    public void setStatus(DockStatus status) {
        this.status = status;
    }

    public Date getReservedUntil() {
        return reservedUntil;
    }

    public void setReservedUntil(Date reservedUntil) {
        this.reservedUntil = reservedUntil;
    }

    public Date getAvailableFrom() {
        return availableFrom;
    }

    public void setAvailableFrom(Date availableFrom) {
        this.availableFrom = availableFrom;
    }
}
