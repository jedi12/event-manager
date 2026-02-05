package ru.project.my.eventmanager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.project.my.eventmanager.repositories.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByLogin(String login);
}
