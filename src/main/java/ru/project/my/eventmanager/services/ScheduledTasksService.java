package ru.project.my.eventmanager.services;

import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.my.eventmanager.repositories.EventRepository;
import ru.project.my.eventmanager.repositories.entity.EventEntity;
import ru.project.my.eventmanager.services.model.EventStatus;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScheduledTasksService {
    private final EventRepository eventRepository;
    private final ScheduledTasksService self;

    public ScheduledTasksService(EventRepository eventRepository, @Lazy ScheduledTasksService scheduledTasksService) {
        this.eventRepository = eventRepository;
        this.self = scheduledTasksService;
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
        eventRepository.findByIdAndLock(eventId)
                .ifPresent(event -> event.setStatus(eventStatus));
    }
}
