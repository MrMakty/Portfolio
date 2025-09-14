package com.spaceinvaders.spaceinvadersbackend.controller;



import com.spaceinvaders.spaceinvadersbackend.dao.DockDAO;
import com.spaceinvaders.spaceinvadersbackend.dto.DockStatusDTO;
import com.spaceinvaders.spaceinvadersbackend.dto.MaintenanceDTO;
import com.spaceinvaders.spaceinvadersbackend.models.Dock;
import com.spaceinvaders.spaceinvadersbackend.models.Reservation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/docks")
@EnableMethodSecurity(prePostEnabled = true)
public class DockController {

    private final DockDAO dockDAO;

    public DockController(DockDAO dockDAO) {
        this.dockDAO = dockDAO;
    }

    @GetMapping()
    public ResponseEntity<List<Dock>> getAllDocks() {
        return ResponseEntity.ok(dockDAO.getAllDocks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dock> getDock(@PathVariable Long id) {
        return ResponseEntity.ok(dockDAO.getDock(id));
    }

    @GetMapping("/{id}/reservations")
    public ResponseEntity<List<Reservation>> getReservationsForDock(@PathVariable Long id) {
        return ResponseEntity.ok(dockDAO.getReservationsForDock(id));
    }

    @PutMapping("/{id}/maintenance")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> updateMaintenanceStatus(@PathVariable Long id,
                                                        @RequestBody @Valid MaintenanceDTO dto) {
        Dock updatedDock = dockDAO.updateMaintenanceStatus(id, dto.isInMaintenance());
        return ResponseEntity.ok("Maintenance status updated for dock with ID: " + id);
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<DockStatusDTO> getDockStatus(@PathVariable Long id) {
        return ResponseEntity.ok(dockDAO.getDockStatus(id));
    }


}
