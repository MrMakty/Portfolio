package com.spaceinvaders.spaceinvadersbackend.dao;

import com.spaceinvaders.spaceinvadersbackend.models.SpaceStation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpaceStationRepository extends JpaRepository<SpaceStation, Long> {
}
