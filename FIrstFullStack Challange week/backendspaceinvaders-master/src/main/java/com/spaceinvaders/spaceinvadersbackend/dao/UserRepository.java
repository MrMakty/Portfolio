package com.spaceinvaders.spaceinvadersbackend.dao;

import com.spaceinvaders.spaceinvadersbackend.models.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<CustomUser, Integer> {
    public CustomUser findByEmail(String email);

    List<CustomUser> id(long id);
}
