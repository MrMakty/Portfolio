package com.spaceinvaders.spaceinvadersbackend;

import com.spaceinvaders.spaceinvadersbackend.dao.DockRepository;
import com.spaceinvaders.spaceinvadersbackend.dao.ReservationDAO;
import com.spaceinvaders.spaceinvadersbackend.dao.ReservationRepository;
import com.spaceinvaders.spaceinvadersbackend.dao.SpaceshipRepository;
import com.spaceinvaders.spaceinvadersbackend.dto.ReservationDTO;
import com.spaceinvaders.spaceinvadersbackend.models.*;
import com.spaceinvaders.spaceinvadersbackend.services.UserFromTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationDAOTest {
    @Mock
    private SpaceshipRepository spaceshipRepository;

    @Mock
    private DockRepository dockRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private UserFromTokenService userFromTokenService;

    @InjectMocks
    private ReservationDAO reservationDAO;

    private CustomUser user;
    private Dock dock;
    private Spaceship spaceship;
    private ReservationDTO reservationDTO;

    @BeforeEach
    public void setUp() {
        user = new CustomUser ();
        dock = new Dock();
        dock.setId(1L);
        dock.setSpaceSize(Size.L);
        dock.setInMaintenance(false);

        spaceship = new Spaceship();
        spaceship.setShipId(1L);
        spaceship.setSize(Size.L);
        spaceship.setShipType(SpaceshipType.MILITARY);

        reservationDTO = new ReservationDTO();
        reservationDTO.setDockId(dock.getId());
        reservationDTO.setSpaceshipId(spaceship.getShipId());

        Calendar calendarStart = Calendar.getInstance();
        calendarStart.set(2025, Calendar.APRIL, 17, 15, 0, 0);
        calendarStart.set(Calendar.MILLISECOND, 0);
        Date startDate = calendarStart.getTime();

        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.set(2025, Calendar.APRIL, 17, 16, 0, 0);
        calendarEnd.set(Calendar.MILLISECOND, 0);
        Date endDate = calendarEnd.getTime();

        reservationDTO.setStartTimestamp(startDate);
        reservationDTO.setEndTimestamp(endDate);
    }

    @Test
    public void should_create_reservation_when_conditions_are_met() {
        when(userFromTokenService.getCurrentUser ()).thenReturn(user);
        when(dockRepository.findById(dock.getId())).thenReturn(Optional.of(dock));
        when(spaceshipRepository.findById(spaceship.getShipId())).thenReturn(Optional.of(spaceship));
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Reservation result = reservationDAO.createReservation(reservationDTO);

        assertNotNull(result);
        assertEquals(spaceship.getShipId(), result.getSpaceship().getShipId());
        assertEquals(dock.getId(), result.getDock().getId());
        assertEquals(reservationDTO.getStartTimestamp(), result.getStartTimestamp());
        assertEquals(reservationDTO.getEndTimestamp(), result.getEndTimestamp());
        assertEquals(user, result.getUser ());
    }

    @Test
    public void should_throw_exception_when_dock_not_found() {
        when(userFromTokenService.getCurrentUser ()).thenReturn(user);
        when(dockRepository.findById(dock.getId())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reservationDAO.createReservation(reservationDTO);
        });
        assertEquals("Dock not found", exception.getMessage());
    }

    @Test
    public void should_throw_exception_when_spaceship_not_found() {
        when(userFromTokenService.getCurrentUser ()).thenReturn(user);
        when(dockRepository.findById(dock.getId())).thenReturn(Optional.of(dock));
        when(spaceshipRepository.findById(spaceship.getShipId())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reservationDAO.createReservation(reservationDTO);
        });
        assertEquals("Spaceship not found", exception.getMessage());
    }
}
