package com.spaceinvaders.spaceinvadersbackend.dto;

import com.spaceinvaders.spaceinvadersbackend.models.Size;
import com.spaceinvaders.spaceinvadersbackend.models.SpaceshipType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SpaceshipDTO {

    @NotBlank(message = "Ship name is required")
    private String shipName;

    @NotNull(message = "Function is required")
    private SpaceshipType shipType;

    @NotNull(message = "Size is required")
    private Size size;

    @NotBlank(message = "imagePath is required")
    private String imagePath;

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public SpaceshipType getShipType() {
        return shipType;
    }

    public void setFunction(SpaceshipType shipType) {
        this.shipType = shipType;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
