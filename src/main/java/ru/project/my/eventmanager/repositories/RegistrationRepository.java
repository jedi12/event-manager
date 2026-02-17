package ru.project.my.eventmanager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.project.my.eventmanager.repositories.entity.RegistrationEntity;

import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<RegistrationEntity, Long> {
    boolean existsByUserIdAndEventId(Long userId, Long eventId);

    Optional<RegistrationEntity> findByUserIdAndEventId(Long userId, Long eventId);

    void deleteByEventId(Long eventId);
}
