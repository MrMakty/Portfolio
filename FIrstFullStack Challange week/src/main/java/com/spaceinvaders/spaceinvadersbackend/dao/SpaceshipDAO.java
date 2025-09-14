package com.spaceinvaders.spaceinvadersbackend.dao;

import com.spaceinvaders.spaceinvadersbackend.config.JWTUtil;
import com.spaceinvaders.spaceinvadersbackend.dto.SpaceshipDTO;
import com.spaceinvaders.spaceinvadersbackend.models.CustomUser;
import com.spaceinvaders.spaceinvadersbackend.models.Spaceship;
import com.spaceinvaders.spaceinvadersbackend.services.UserFromTokenService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SpaceshipDAO {
    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;
    private final SpaceshipRepository spaceshipRepository;
    private UserFromTokenService userFromTokenService;

    public SpaceshipDAO(UserRepository userRepository, JWTUtil jwtUtil, SpaceshipRepository spaceshipRepository, UserFromTokenService userFromTokenService) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.spaceshipRepository = spaceshipRepository;
        this.userFromTokenService = userFromTokenService;
    }

    public List<Spaceship> getShipsByUser() {
        CustomUser user = this.userFromTokenService.getCurrentUser();
        if (user == null) {
            throw new RuntimeException("Invalid or expired token");
        }

        List<Spaceship> spaceships = spaceshipRepository.findByOwner(user);
        if (spaceships.isEmpty()) {
            throw new RuntimeException("No spaceships found for this user");
        }

        return spaceships;
    }

    public Spaceship createShip(SpaceshipDTO dto) {
        CustomUser user = this.userFromTokenService.getCurrentUser();
        if (user == null) {
            throw new RuntimeException("Invalid or expired token");
        }

        Spaceship ship = new Spaceship(
                dto.getShipName(),
                dto.getShipType(),
                dto.getSize(),
                dto.getImagePath()
        );

        ship.setOwner(user);

        return spaceshipRepository.save(ship);
    }

    public Spaceship changeShip(Long id, SpaceshipDTO dto) {
        CustomUser user = this.userFromTokenService.getCurrentUser();
        if (user == null) {
            throw new RuntimeException("Invalid or expired token");
        }

        Spaceship ship = spaceshipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Spaceship not found"));

        if (ship.getOwner() == null || ship.getOwner().getId() != user.getId()) {
            throw new RuntimeException("You are not authorized to update this spaceship");
        }

        ship.setShipName(dto.getShipName());
        ship.setShipType(dto.getShipType());
        ship.setSize(dto.getSize());
        ship.setImagePath(dto.getImagePath());

        return spaceshipRepository.save(ship);
    }

}
