package com.spaceinvaders.spaceinvadersbackend.dao;

import com.spaceinvaders.spaceinvadersbackend.dto.DockStatus;
import com.spaceinvaders.spaceinvadersbackend.dto.DockStatusDTO;
import com.spaceinvaders.spaceinvadersbackend.models.Dock;
import com.spaceinvaders.spaceinvadersbackend.models.Reservation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;


import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Component
public class DockDAO {
    private final DockRepository dockRepository;
    private final ReservationRepository reservationRepository;

    public DockDAO(DockRepository dockRepository, ReservationRepository reservationRepository) {
        this.dockRepository = dockRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<Dock> getAllDocks() {
        return dockRepository.findAll();
    }

    public Dock getDock(Long id) {
        return dockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dock not found with id: " + id));
    }

    public List<Reservation> getReservationsForDock(Long dockId) {
        Dock dock = getDock(dockId);
        return reservationRepository.findAllByDock(dock);
    }

    public Dock updateMaintenanceStatus(Long dockId, boolean inMaintenance) {
        Dock dock = dockRepository.findById(dockId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dock not found with ID: " + dockId));

        dock.setInMaintenance(inMaintenance);
        return dockRepository.save(dock);
    }

    public DockStatusDTO getDockStatus(Long dockId) {
        Dock dock = dockRepository.findById(dockId)
                .orElseThrow(() -> new RuntimeException("Dock not found"));

        if (dock.isInMaintenance()) {
            return new DockStatusDTO(DockStatus.MAINTENANCE, null, null);
        }

        Date now = new Date();

        List<Reservation> activeResList = reservationRepository.findActiveReservations(dockId, now);
        if (!activeResList.isEmpty()) {
            activeResList.sort(Comparator.comparing(Reservation::getStartTimestamp));
            Reservation activeRes = activeResList.getFirst();
            Date reservedUntil = activeRes.getEndTimestamp();
            return new DockStatusDTO(DockStatus.RESERVED, reservedUntil, null);
        }


        List<Reservation> futureResList = reservationRepository.findFutureReservations(dockId, now);
        if (!futureResList.isEmpty()) {
            futureResList.sort(Comparator.comparing(Reservation::getStartTimestamp));
            Date availableUntil = futureResList.getFirst().getStartTimestamp();
            return new DockStatusDTO(DockStatus.AVAILABLE, null, availableUntil);
        }

        return new DockStatusDTO(DockStatus.AVAILABLE, null, null);
    }
}
