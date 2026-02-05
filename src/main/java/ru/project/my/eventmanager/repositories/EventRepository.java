package ru.project.my.eventmanager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.project.my.eventmanager.repositories.entity.EventEntity;
import ru.project.my.eventmanager.services.model.EventStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<EventEntity, Long> {

    @Query("SELECT COUNT(e.id) FROM EventEntity e WHERE e.location.id = :locationId")
    int eventsWithLocationCount(@Param("locationId") Long locationId);

    @Query("""
            SELECT e FROM EventEntity e
                WHERE (:name IS NULL OR e.name = :name)
                AND (:ownerId IS NULL OR e.owner.id = :ownerId)
                AND (:placesMin IS NULL OR e.maxPlaces >= :placesMin)
                AND (:placesMax IS NULL OR e.maxPlaces <= :placesMax)
                AND (cast(:dateStartAfter as timestamp) IS NULL OR e.date >= :dateStartAfter)
                AND (cast(:dateStartBefore as timestamp) IS NULL OR e.date <= :dateStartBefore)
                AND (:costMin IS NULL OR e.cost >= :costMin)
                AND (:costMax IS NULL OR e.cost <= :costMax)
                AND (:durationMin IS NULL OR e.duration >= :durationMin)
                AND (:durationMax IS NULL OR e.duration <= :durationMax)
                AND (:locationId IS NULL OR e.location.id = :locationId)
                AND (:eventStatus IS NULL OR e.status = :eventStatus)
    """)
    List<EventEntity> searchBySearchFilter(
            @Param("name") String name,
            @Param("ownerId") Long ownerId,
            @Param("placesMin") Integer placesMin,
            @Param("placesMax") Integer placesMax,
            @Param("dateStartAfter") LocalDateTime dateStartAfter,
            @Param("dateStartBefore") LocalDateTime dateStartBefore,
            @Param("costMin") Integer costMin,
            @Param("costMax") Integer costMax,
            @Param("durationMin") Integer durationMin,
            @Param("durationMax") Integer durationMax,
            @Param("locationId") Long locationId,
            @Param("eventStatus") EventStatus eventStatus
    );
}
