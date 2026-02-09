package ru.project.my.eventmanager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.project.my.eventmanager.repositories.entity.LocationEntity;

public interface LocationRepository extends JpaRepository<LocationEntity, Long> {
    boolean existsByNameIgnoreCase(String name);
}
