package ru.project.my.eventmanager.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.my.eventmanager.kafka.EventChangeMessageCreator;
import ru.project.my.eventmanager.kafka.model.EventChangeMessage;
import ru.project.my.eventmanager.repositories.EventRepository;
import ru.project.my.eventmanager.repositories.RegistrationRepository;
import ru.project.my.eventmanager.repositories.entity.EventEntity;
import ru.project.my.eventmanager.repositories.entity.RegistrationEntity;
import ru.project.my.eventmanager.services.model.EventStatus;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScheduledTasksService {
    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;
    private final ScheduledTasksService self;
    private final KafkaTemplate<Long, EventChangeMessage> kafkaTemplate;
    private final EventChangeMessageCreator messageCreator;
    private final String eventChangeTopic;

    public ScheduledTasksService(EventRepository eventRepository, RegistrationRepository registrationRepository, @Lazy ScheduledTasksService scheduledTasksService, KafkaTemplate<Long, EventChangeMessage> kafkaTemplate, EventChangeMessageCreator messageCreator, @Value("${eventmanager.kafka.event-change-topic-name}") String eventChangeTopic) {
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
        this.self = scheduledTasksService;
        this.kafkaTemplate = kafkaTemplate;
        this.messageCreator = messageCreator;
        this.eventChangeTopic = eventChangeTopic;
    }

    @Scheduled(cron = "${eventmanager.switch-event-status-cron}")
    public void checkEventStatus() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        List<EventEntity> eventsCandidates = eventRepository.eventsToSwitchStatus(currentDateTime, List.of(EventStatus.WAIT_START, EventStatus.STARTED));

        for (EventEntity eventEntity: eventsCandidates) {
            if (EventStatus.WAIT_START.equals(eventEntity.getStatus())) {
                self.switchEventStatus(eventEntity.getId(), EventStatus.STARTED);
            }

            LocalDateTime eventFinishDate = eventEntity.getDate().plusMinutes(eventEntity.getDuration());
            if (EventStatus.STARTED.equals(eventEntity.getStatus()) && eventFinishDate.isBefore(currentDateTime)) {
                self.switchEventStatus(eventEntity.getId(), EventStatus.FINISHED);
            }
        }
    }

    @Transactional
    public void switchEventStatus(Long eventId, EventStatus eventStatus) {
        eventRepository.findByIdAndLock(eventId).ifPresent(eventEntity -> {
            eventEntity.setStatus(eventStatus);

            List<RegistrationEntity> registrations = registrationRepository.findByEventId(eventEntity.getId());
            EventChangeMessage message = messageCreator.create(eventEntity, eventStatus, registrations);
            kafkaTemplate.send(eventChangeTopic, eventId, message);
        });
    }
}
