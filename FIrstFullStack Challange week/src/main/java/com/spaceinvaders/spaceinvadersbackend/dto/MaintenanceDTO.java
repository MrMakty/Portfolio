package com.spaceinvaders.spaceinvadersbackend.dto;

import jakarta.validation.constraints.NotNull;

public class MaintenanceDTO {
    @NotNull(message = "Maintenance status must be specified")
    private Boolean inMaintenance;

    public boolean isInMaintenance() {
        return inMaintenance;
    }

    public void setInMaintenance(boolean inMaintenance) {
        this.inMaintenance = inMaintenance;
    }
}
