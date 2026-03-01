package ru.project.my.eventmanager.kafka.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import ru.project.my.eventmanager.services.model.EventStatus;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventChangeMessage {
    private Long eventId;
    private Long changedByUserId;
    private Long ownerId;

    private List<Long> users;

    private FieldChange<String> name;
    private FieldChange<Integer> maxPlaces;
    private FieldChange<Integer> occupiedPlaces;
    private FieldChange<LocalDateTime> date;
    private FieldChange<Integer> cost;
    private FieldChange<Integer> duration;
    private FieldChange<Long> locationId;
    private FieldChange<EventStatus> status;

    @JsonIgnore
    public boolean isContainsChanges() {
        return name != null || maxPlaces != null ||occupiedPlaces != null ||date != null ||cost != null ||duration != null ||locationId != null ||status != null;
    }

    public Long getEventId() {
        return eventId;
    }
    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getChangedByUserId() {
        return changedByUserId;
    }
    public void setChangedByUserId(Long changedByUserId) {
        this.changedByUserId = changedByUserId;
    }

    public Long getOwnerId() {
        return ownerId;
    }
    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public List<Long> getUsers() {
        return users;
    }
    public void setUsers(List<Long> users) {
        this.users = users;
    }

    public FieldChange<String> getName() {
        return name;
    }
    public void setName(FieldChange<String> name) {
        this.name = name;
    }

    public FieldChange<Integer> getMaxPlaces() {
        return maxPlaces;
    }
    public void setMaxPlaces(FieldChange<Integer> maxPlaces) {
        this.maxPlaces = maxPlaces;
    }

    public FieldChange<Integer> getOccupiedPlaces() {
        return occupiedPlaces;
    }
    public void setOccupiedPlaces(FieldChange<Integer> occupiedPlaces) {
        this.occupiedPlaces = occupiedPlaces;
    }

    public FieldChange<LocalDateTime> getDate() {
        return date;
    }
    public void setDate(FieldChange<LocalDateTime> date) {
        this.date = date;
    }

    public FieldChange<Integer> getCost() {
        return cost;
    }
    public void setCost(FieldChange<Integer> cost) {
        this.cost = cost;
    }

    public FieldChange<Integer> getDuration() {
        return duration;
    }
    public void setDuration(FieldChange<Integer> duration) {
        this.duration = duration;
    }

    public FieldChange<Long> getLocationId() {
        return locationId;
    }
    public void setLocationId(FieldChange<Long> locationId) {
        this.locationId = locationId;
    }

    public FieldChange<EventStatus> getStatus() {
        return status;
    }
    public void setStatus(FieldChange<EventStatus> status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "EventChangeMessage{" +
                "eventId=" + eventId +
                ", changedByUserId=" + changedByUserId +
                ", ownerId=" + ownerId +
                ", users=" + users +
                ", name=" + name +
                ", maxPlaces=" + maxPlaces +
                ", occupiedPlaces=" + occupiedPlaces +
                ", date=" + date +
                ", cost=" + cost +
                ", duration=" + duration +
                ", locationId=" + locationId +
                ", status=" + status +
                '}';
    }
}