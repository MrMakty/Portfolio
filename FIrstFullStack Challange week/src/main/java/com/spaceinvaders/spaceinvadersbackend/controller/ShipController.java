package com.spaceinvaders.spaceinvadersbackend.controller;


import com.spaceinvaders.spaceinvadersbackend.dao.SpaceshipDAO;
import com.spaceinvaders.spaceinvadersbackend.dto.SpaceshipDTO;
import com.spaceinvaders.spaceinvadersbackend.models.Spaceship;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/ships")
@RestController
public class ShipController {
    //get - alle schepen van gebruiker
    //post - voegt nieuw schip toe gebruiker
    //put - wijzigt schip gebruiker

    private final SpaceshipDAO spaceshipDAO;

    public ShipController(SpaceshipDAO spaceshipDAO) {
        this.spaceshipDAO = spaceshipDAO;
    }

    @GetMapping()
    public ResponseEntity<List<Spaceship>> getAllShips() {
        return ResponseEntity.ok(spaceshipDAO.getShipsByUser());
    }

    @PostMapping()
    public ResponseEntity<Spaceship> createSpaceship(
            @RequestBody @Valid SpaceshipDTO spaceshipDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(spaceshipDAO.createShip(spaceshipDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Spaceship> changeShip(
            @PathVariable Long id,
            @RequestBody @Valid SpaceshipDTO spaceshipDTO) {
        return ResponseEntity.ok(spaceshipDAO.changeShip(id, spaceshipDTO));
    }
}
