package com.spaceinvaders.spaceinvadersbackend.dao;

import com.spaceinvaders.spaceinvadersbackend.models.CustomUser;
import com.spaceinvaders.spaceinvadersbackend.models.Dock;
import com.spaceinvaders.spaceinvadersbackend.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;



import org.springframework.data.jpa.repository.Query;


import java.util.Date;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUser(CustomUser user);
    List<Reservation> findByUserAndStartTimestampAfter(CustomUser user, Date startTimestamp);
    List<Reservation> findAllByDock(Dock dock);

    @Query("SELECT r FROM Reservation r WHERE r.dock.id = :dockId AND :now BETWEEN r.startTimestamp AND r.endTimestamp")
    List<Reservation> findActiveReservations(@Param("dockId") Long dockId, @Param("now") Date now);


    @Query("SELECT r FROM Reservation r WHERE r.dock.id = :dockId AND r.startTimestamp > :now")
    List<Reservation> findFutureReservations(@Param("dockId") Long dockId, @Param("now") Date now);


    @Query("SELECT r FROM Reservation r WHERE r.dock.id = :dockId AND r.startTimestamp < :endTimestamp AND r.endTimestamp > :startTimestamp")
    List<Reservation> findByDockIdAndTimeRange(@Param("dockId") Long dockId,
                                               @Param("startTimestamp") LocalDateTime startTimestamp,
                                               @Param("endTimestamp") LocalDateTime endTimestamp);

    @Query("SELECT r FROM Reservation r WHERE r.spaceship.shipId = :spaceshipId AND r.startTimestamp < :endTimestamp AND r.endTimestamp > :startTimestamp")
    List<Reservation> findBySpaceshipIdAndTimeRange(@Param("spaceshipId") Long spaceshipId,
                                                    @Param("startTimestamp") LocalDateTime startTimestamp,
                                                    @Param("endTimestamp") LocalDateTime endTimestamp);
}