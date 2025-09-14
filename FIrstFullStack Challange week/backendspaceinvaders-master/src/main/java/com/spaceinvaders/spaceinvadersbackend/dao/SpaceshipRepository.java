package com.spaceinvaders.spaceinvadersbackend.dao;

import com.spaceinvaders.spaceinvadersbackend.models.CustomUser;
import com.spaceinvaders.spaceinvadersbackend.models.Spaceship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpaceshipRepository extends JpaRepository<Spaceship, Long> {
    List<Spaceship> findByOwner(CustomUser user);
}
