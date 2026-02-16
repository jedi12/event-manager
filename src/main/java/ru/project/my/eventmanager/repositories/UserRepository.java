package ru.project.my.eventmanager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.project.my.eventmanager.repositories.entity.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByLoginIgnoreCase(String login);

    Optional<UserEntity> findUseByLogin(String login);
}
