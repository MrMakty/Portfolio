package com.spaceinvaders.spaceinvadersbackend.dao;

import com.spaceinvaders.spaceinvadersbackend.config.JWTUtil;
import com.spaceinvaders.spaceinvadersbackend.dto.ExtensionDTO;
import com.spaceinvaders.spaceinvadersbackend.dto.ReservationDTO;
import com.spaceinvaders.spaceinvadersbackend.models.*;
import com.spaceinvaders.spaceinvadersbackend.services.UserFromTokenService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReservationDAO {
    private ReservationRepository reservationRepository;
    private DockRepository dockRepository;
    private DockDAO dockDAO;
    private SpaceshipRepository spaceshipRepository;
    private UserRepository userRepository;
    private JWTUtil jwtUtil;
    private UserFromTokenService userFromTokenService;

    public ReservationDAO(ReservationRepository reservationRepository, DockRepository dockRepository, DockDAO dockDAO, SpaceshipRepository spaceshipRepository, UserRepository userRepository, JWTUtil jwtUtil, UserFromTokenService userFromTokenService) {
        this.reservationRepository = reservationRepository;
        this.dockRepository = dockRepository;
        this.dockDAO = dockDAO;
        this.spaceshipRepository = spaceshipRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.userFromTokenService = userFromTokenService;
    }

    public Reservation createReservation(ReservationDTO reservationDTO) {
        CustomUser user = this.userFromTokenService.getCurrentUser();
        if (user == null) {
            throw new RuntimeException("Invalid or expired token");
        }

        Dock dock = dockRepository.findById(reservationDTO.getDockId())
                .orElseThrow(() -> new RuntimeException("Dock not found"));

        Spaceship spaceship = spaceshipRepository.findById(reservationDTO.getSpaceshipId())
                .orElseThrow(() -> new RuntimeException("Spaceship not found"));

        if (spaceship.getSize() != dock.getSpaceSize()){
            throw new RuntimeException("Spaceship doesn't have the correct space size");
        }

        if (checkShipReservationTime(spaceship, reservationDTO.getStartTimestamp(), reservationDTO.getEndTimestamp())){
            throw new RuntimeException("Ship already has a reservation");
        }

        if (dock.isInMaintenance()){
            throw new RuntimeException("Dock is in maintenance");
        }

        if (checkReservationTime(dock.getId(), reservationDTO.getStartTimestamp(), reservationDTO.getEndTimestamp())) {
            throw new RuntimeException("Dock is already reserved for the specified time period.");
        }

        if (spaceship.getShipType() == SpaceshipType.TRADER_DANGEROUS){
            List<Long> dockIDs = new ArrayList<>();
            Long dockId = dock.getId();

            Long dockIdLeft = dockId - 1;
            if (dockIdLeft == 0) {
                dockIdLeft = 12L;
            } else if (dockIdLeft == 12) {
                dockIdLeft = 24L;
            }
            dockIDs.add(dockIdLeft);

            Long dockIdRight = dockId + 1;
            if (dockIdRight == 13) {
                dockIdRight = 1L;
            } else if (dockIdRight == 25) {
                dockIdRight = 13L;
            }
            dockIDs.add(dockIdRight);

            for (Long dockID : dockIDs) {
                dock = dockRepository.findById(dockID).orElse(null);
                if (dock.isInMaintenance()) {
                    throw new RuntimeException("Dock is in maintenance");
                }

                if (checkReservationTime(dockID, reservationDTO.getStartTimestamp(), reservationDTO.getEndTimestamp())) {
                    throw new RuntimeException("Adjacent dock " + dockID + " is already reserved for the specified time period.");
                }
            }

            for (Long dockID : dockIDs) {
                Reservation reservation = new Reservation(
                        spaceship,
                        dockRepository.findById(dockID).get(),
                        reservationDTO.getStartTimestamp(),
                        reservationDTO.getEndTimestamp(),
                        false
                );
                reservation.setUser(user);
                reservationRepository.save(reservation);
            }
        }

        Reservation reservation = new Reservation(
                spaceship,
                dockRepository.findById(dock.getId()-1).get(),
                reservationDTO.getStartTimestamp(),
                reservationDTO.getEndTimestamp(),
                true
        );
        reservation.setUser(user);
        return reservationRepository.save(reservation);
    }

    public List<Reservation> getAllCurrentReservationsByUser() {
        CustomUser user = this.userFromTokenService.getCurrentUser();
        if (user == null) {
            throw new RuntimeException("Invalid or expired token");
        }

        List<Reservation> reservations = reservationRepository.findByUser(user);

        Date now = new Date();

        List<Reservation> futureReservations = reservations.stream()
                .filter(reservation -> reservation.getStartTimestamp() != null && !reservation.getStartTimestamp().before(now))
                .collect(Collectors.toList());

        List<Reservation> standingReservations = futureReservations.stream()
                .filter(reservation -> reservation.getStandingHere() != null && reservation.getStandingHere())
                .collect(Collectors.toList());

        return standingReservations;
    }

    public boolean checkReservationTime(long dockId, Date startTimestamp, Date endTimestamp) {
        LocalDateTime startDateTime = startTimestamp.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        LocalDateTime endDateTime = endTimestamp.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        List<Reservation> existingReservations = reservationRepository.findByDockIdAndTimeRange(dockId, startDateTime, endDateTime);

        return !existingReservations.isEmpty();
    }

    public boolean checkShipReservationTime(Spaceship spaceship, Date startTimestamp, Date endTimestamp) {
        LocalDateTime startDateTime = startTimestamp.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        LocalDateTime endDateTime = endTimestamp.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        // Haal de bestaande reserveringen van het schip op
        List<Reservation> existingReservations = reservationRepository.findBySpaceshipIdAndTimeRange(spaceship.getShipId(), startDateTime, endDateTime);

        // Controleer of er overlapping is met de nieuwe reservering
        return !existingReservations.isEmpty();
    }

    public Reservation getReservationById(Long id) {
        CustomUser user = this.userFromTokenService.getCurrentUser();
        if (user == null) {
            throw new RuntimeException("Invalid or expired token");
        }
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
    }

    public List<Reservation> getReservationsByUser() {
        CustomUser user = this.userFromTokenService.getCurrentUser();
        if (user == null) {
            throw new RuntimeException("Invalid or expired token");
        }
        return reservationRepository.findByUser(user);
    }

    public List<Reservation> getActiveReservations() {
        CustomUser user = this.userFromTokenService.getCurrentUser();
        if (user == null) {
            throw new RuntimeException("Invalid or expired token");
        }
        return reservationRepository.findByUserAndStartTimestampAfter(user, new Date());
    }

    public Reservation extendReservation(Long id, ExtensionDTO extensionDTO) {
        CustomUser user = this.userFromTokenService.getCurrentUser();
        if (user == null) {
            throw new RuntimeException("Invalid or expired token");
        }

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        reservation.setStartTimestamp(extensionDTO.getNewStartTimestamp());

        return reservationRepository.save(reservation);
    }

    public void cancelReservation(Long id) {
        CustomUser user = this.userFromTokenService.getCurrentUser();
        if (user == null) {
            throw new RuntimeException("Invalid or expired token");
        }

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        reservationRepository.delete(reservation);
    }

    public void cancelMyReservation(Long id) {
        CustomUser user = this.userFromTokenService.getCurrentUser();
        if (user == null) {
            throw new RuntimeException("Invalid or expired token");
        }
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new RuntimeException("Reservation not found"));

        if (reservation.getUser().getId()!=(user.getId())) {
            throw new RuntimeException("You are not authorized to cancel this reservation");
        }

        if (reservation.getStandingHere() == false) {
            throw new RuntimeException("You are not standing here or allowed to make this place free");
        }

        if (reservation.getSpaceship().getShipType()==SpaceshipType.TRADER_DANGEROUS){
            deleteReservationIfExists(id + 1L);
            deleteReservationIfExists(id - 1L);
        }

        reservationRepository.delete(reservation);
    }

    private void deleteReservationIfExists(Long reservationId) {
        reservationRepository.findById(reservationId).ifPresent(reservation -> {
            reservationRepository.delete(reservation);
        });
    }
}
