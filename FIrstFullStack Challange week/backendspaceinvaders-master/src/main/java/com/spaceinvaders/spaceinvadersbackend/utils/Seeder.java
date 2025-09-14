package com.spaceinvaders.spaceinvadersbackend.utils;

import com.spaceinvaders.spaceinvadersbackend.dao.*;
import com.spaceinvaders.spaceinvadersbackend.models.*;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Seeder {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SpaceshipRepository spaceshipRepository;
    private final DockRepository dockRepository;
    private final LevelRepository levelRepository;
    private final SpaceStationRepository spaceStationRepository;

    public Seeder(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            SpaceshipRepository spaceshipRepository,
            DockRepository dockRepository,
            LevelRepository levelRepository,
            SpaceStationRepository spaceStationRepository
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.spaceshipRepository = spaceshipRepository;
        this.dockRepository = dockRepository;
        this.levelRepository = levelRepository;
        this.spaceStationRepository = spaceStationRepository;
    }

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        Role user = new Role(ERole.ROLE_USER);
        roleRepository.save(user);

        Role admin = new Role(ERole.ROLE_ADMIN);
        roleRepository.save(admin);

        //----

        CustomUser adminUser = new CustomUser("email@gmail.com", passwordEncoder.encode("wachtwoord"), "Renzo", "Steller", "Personeel");
        adminUser.setRole(admin);
        userRepository.save(adminUser);

        // Star Wars spaceships
        List<Spaceship> spaceships = new ArrayList<>();
        Spaceship falcon = new Spaceship(
                "Millennium Falcon",
                SpaceshipType.CITIZEN,
                Size.L,
                "/images/millennium-falcon.png"
        );
        spaceships.add(falcon);


        Spaceship xWing = new Spaceship(
                "X-Wing",
                SpaceshipType.MILITARY,
                Size.M,
                "/images/x-wing.png"
        );
        spaceships.add(xWing);

        Spaceship tieFighter = new Spaceship(
                "TIE Fighter",
                SpaceshipType.MILITARY,
                Size.S,
                "/images/tie-fighter.png"
        );
        spaceships.add(tieFighter);

        adminUser.setSpaceships(spaceships);


        SpaceStation station = new SpaceStation("TestNaam", "TestDescription");

        Level level1 = new Level(1, station);
        Level level2 = new Level(2, station);

        Size[] sizes = {Size.S, Size.M, Size.L};
        List<Dock> docks = new ArrayList<>();

        for (int i = 0; i < 24; i++) {
            Size size = sizes[i % 3]; // Cyclisch: S, M, L, S, M, L, ...
            Level level = i < 12 ? level1 : level2;
            Dock dock = new Dock(size, false, level);
            docks.add(dock);
        }

        spaceStationRepository.save(station);
        levelRepository.save(level1);
        levelRepository.save(level2);
        dockRepository.saveAll(docks);

        spaceshipRepository.save(falcon);
        spaceshipRepository.save(xWing);
        spaceshipRepository.save(tieFighter);
    }
}
