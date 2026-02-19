package ru.project.my.eventmanager.repositories;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.project.my.eventmanager.repositories.entity.EventEntity;
import ru.project.my.eventmanager.services.model.EventStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<EventEntity, Long> {
    @Override
    @EntityGraph(attributePaths = {"owner", "location"})
    List<EventEntity> findAll();

    @Override
    @EntityGraph(attributePaths = {"owner", "location"})
    Optional<EventEntity> findById(Long eventId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select e from EventEntity e where e.id = :eventId")
    Optional<EventEntity> findByIdAndLock(Long eventId);

    @Query("select e from EventEntity e LEFT JOIN RegistrationEntity r ON e.id = r.event.id where r.user.id = :userId")
    @EntityGraph(attributePaths = {"owner", "location"})
    List<EventEntity> findByUserRegistration(Long userId);

    @EntityGraph(attributePaths = {"owner", "location"})
    List<EventEntity> findByOwnerId(Long ownerId);

    @Query("SELECT e FROM EventEntity e WHERE e.date <= :date AND e.status IN :status")
    List<EventEntity> eventsToSwitchStatus(@Param("date") LocalDateTime date, @Param("status") List<EventStatus> status);

    @Query("SELECT COUNT(e.id) FROM EventEntity e WHERE e.location.id = :locationId")
    int eventsWithLocationCount(@Param("locationId") Long locationId);

    @Query("""
            SELECT e FROM EventEntity e
                WHERE (:name IS NULL OR e.name LIKE %:name%)
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
    @EntityGraph(attributePaths = {"owner", "location"})
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
