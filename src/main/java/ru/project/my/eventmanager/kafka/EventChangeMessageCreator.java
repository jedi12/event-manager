package ru.project.my.eventmanager.kafka;

import org.springframework.stereotype.Component;
import ru.project.my.eventmanager.kafka.model.EventChangeMessage;
import ru.project.my.eventmanager.kafka.model.FieldChange;
import ru.project.my.eventmanager.repositories.entity.EventEntity;
import ru.project.my.eventmanager.repositories.entity.RegistrationEntity;
import ru.project.my.eventmanager.services.model.EventStatus;

import java.util.List;
import java.util.Objects;

@Component
public class EventChangeMessageCreator {

    public EventChangeMessage create(EventEntity oldEvent, EventEntity newEvent, Long userId, List<RegistrationEntity> registrations) {
        if (oldEvent == null || newEvent == null) return null;

        return createMessage(oldEvent, newEvent, userId, registrations);
    }

    public EventChangeMessage create(EventEntity oldEvent, EventStatus newStatus, List<RegistrationEntity> registrations) {
        EventChangeMessage message = createMessage(oldEvent, oldEvent, null, registrations);
        message.setStatus(new FieldChange<>(oldEvent.getStatus(), newStatus));

        return message;
    }

    public EventChangeMessage createMessage(EventEntity oldEvent, EventEntity newEvent, Long userId, List<RegistrationEntity> registrations) {
        EventChangeMessage message = new EventChangeMessage();
        message.setEventId(oldEvent.getId());
        message.setChangedByUserId(userId);
        message.setOwnerId(oldEvent.getOwner().getId());

        message.setUsers(registrations.stream().map(RegistrationEntity::getId).toList());

        if (!Objects.equals(oldEvent.getName(), newEvent.getName())) {
            message.setName(new FieldChange<>(oldEvent.getName(), newEvent.getName()));
        }

        if (!Objects.equals(oldEvent.getMaxPlaces(), newEvent.getMaxPlaces())) {
            message.setMaxPlaces(new FieldChange<>(oldEvent.getMaxPlaces(), newEvent.getMaxPlaces()));
        }

        if (!Objects.equals(oldEvent.getOccupiedPlaces(), newEvent.getOccupiedPlaces())) {
            message.setOccupiedPlaces(new FieldChange<>(oldEvent.getOccupiedPlaces(), newEvent.getOccupiedPlaces()));
        }

        if (!Objects.equals(oldEvent.getDate(), newEvent.getDate())) {
            message.setDate(new FieldChange<>(oldEvent.getDate(), newEvent.getDate()));
        }

        if (!Objects.equals(oldEvent.getCost(), newEvent.getCost())) {
            message.setCost(new FieldChange<>(oldEvent.getCost(), newEvent.getCost()));
        }

        if (!Objects.equals(oldEvent.getDuration(), newEvent.getDuration())) {
            message.setDuration(new FieldChange<>(oldEvent.getDuration(), newEvent.getDuration()));
        }

        if (!Objects.equals(oldEvent.getLocation().getId(), newEvent.getLocation().getId())) {
            message.setLocationId(new FieldChange<>(oldEvent.getLocation().getId(), newEvent.getLocation().getId()));
        }

        if (!Objects.equals(oldEvent.getStatus(), newEvent.getStatus())) {
            message.setStatus(new FieldChange<>(oldEvent.getStatus(), newEvent.getStatus()));
        }

        return message;
    }
}
