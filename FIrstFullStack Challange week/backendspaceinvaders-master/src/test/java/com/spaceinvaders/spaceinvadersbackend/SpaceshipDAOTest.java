package com.spaceinvaders.spaceinvadersbackend;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.spaceinvaders.spaceinvadersbackend.models.CustomUser;
import com.spaceinvaders.spaceinvadersbackend.models.Spaceship;
import com.spaceinvaders.spaceinvadersbackend.dao.SpaceshipDAO;
import com.spaceinvaders.spaceinvadersbackend.services.UserFromTokenService;
import com.spaceinvaders.spaceinvadersbackend.dao.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

public class SpaceshipDAOTest {

    @Mock
    private SpaceshipRepository spaceshipRepository;

    @Mock
    private UserFromTokenService userFromTokenService;

    @InjectMocks
    private SpaceshipDAO spaceshipDAO;

    private CustomUser user;
    private Spaceship spaceship;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Simuleer een gebruiker
        user = new CustomUser();
        user.setId(1L);

        // Simuleer een schip
        spaceship = new Spaceship();
        spaceship.setShipId(1L);
        spaceship.setShipName("Galaxy Cruiser");
        spaceship.setOwner(user);
    }

    @Test
    public void should_return_ships_when_user_has_ships() {
        // Mock de afhankelijkheden
        when(userFromTokenService.getCurrentUser()).thenReturn(user);
        when(spaceshipRepository.findByOwner(user)).thenReturn(List.of(spaceship));

        // Test of de lijst met schepen correct wordt geretourneerd
        List<Spaceship> ships = spaceshipDAO.getShipsByUser();
        assertNotNull(ships);
        assertEquals(1, ships.size());
        assertEquals("Galaxy Cruiser", ships.get(0).getShipName());
    }

    @Test
    public void should_throw_exception_when_user_has_no_ships() {
        // Mock de afhankelijkheden
        when(userFromTokenService.getCurrentUser()).thenReturn(user);
        when(spaceshipRepository.findByOwner(user)).thenReturn(List.of());

        // Test of de juiste uitzondering wordt gegooid als er geen schepen zijn
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            spaceshipDAO.getShipsByUser();
        });

        assertEquals("No spaceships found for this user", exception.getMessage());
    }

    @Test
    public void should_throw_exception_when_token_is_invalid() {
        // Mock de afhankelijkheden
        when(userFromTokenService.getCurrentUser()).thenReturn(null);

        // Test of de juiste uitzondering wordt gegooid bij een ongeldige token
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            spaceshipDAO.getShipsByUser();
        });

        assertEquals("Invalid or expired token", exception.getMessage());
    }
}

