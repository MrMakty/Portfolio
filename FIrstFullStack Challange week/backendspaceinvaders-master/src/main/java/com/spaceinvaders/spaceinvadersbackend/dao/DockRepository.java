package com.spaceinvaders.spaceinvadersbackend.dao;

import com.spaceinvaders.spaceinvadersbackend.models.Dock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DockRepository extends JpaRepository<Dock, Long> {
}
