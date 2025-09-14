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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationAvailabilityTest {

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
        user = new CustomUser();

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

        Calendar startCal = Calendar.getInstance();
        startCal.set(2025, Calendar.APRIL, 18, 14, 0, 0);
        startCal.set(Calendar.MILLISECOND, 0);
        Date start = startCal.getTime();

        Calendar endCal = Calendar.getInstance();
        endCal.set(2025, Calendar.APRIL, 18, 15, 0, 0);
        endCal.set(Calendar.MILLISECOND, 0);
        Date end = endCal.getTime();

        reservationDTO.setStartTimestamp(start);
        reservationDTO.setEndTimestamp(end);
    }

    @Test
    public void should_create_reservation_when_dock_is_free() {
        when(userFromTokenService.getCurrentUser()).thenReturn(user);
        when(dockRepository.findById(dock.getId())).thenReturn(Optional.of(dock));
        when(spaceshipRepository.findById(spaceship.getShipId())).thenReturn(Optional.of(spaceship));

        LocalDateTime start = reservationDTO.getStartTimestamp().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime end = reservationDTO.getEndTimestamp().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        when(reservationRepository.findByDockIdAndTimeRange(eq(dock.getId()), eq(start), eq(end)))
                .thenReturn(Collections.emptyList());

        when(reservationRepository.save(any(Reservation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Reservation result = reservationDAO.createReservation(reservationDTO);

        assertNotNull(result);
        assertEquals(user, result.getUser());
        assertEquals(dock.getId(), result.getDock().getId());
        assertEquals(spaceship.getShipId(), result.getSpaceship().getShipId());
    }

    @Test
    public void should_throw_exception_when_dock_is_occupied() {
        when(userFromTokenService.getCurrentUser()).thenReturn(user);
        when(dockRepository.findById(dock.getId())).thenReturn(Optional.of(dock));
        when(spaceshipRepository.findById(spaceship.getShipId())).thenReturn(Optional.of(spaceship));

        LocalDateTime start = reservationDTO.getStartTimestamp().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime end = reservationDTO.getEndTimestamp().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        // Simuleer dat er al een reservering bestaat op deze tijd
        Reservation existingReservation = new Reservation();
        existingReservation.setDock(dock);
        existingReservation.setStartTimestamp(reservationDTO.getStartTimestamp());
        existingReservation.setEndTimestamp(reservationDTO.getEndTimestamp());

        when(reservationRepository.findByDockIdAndTimeRange(eq(dock.getId()), eq(start), eq(end)))
                .thenReturn(List.of(existingReservation));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reservationDAO.createReservation(reservationDTO);
        });

        assertEquals("Dock is already reserved for the specified time period.", exception.getMessage());
    }
}
