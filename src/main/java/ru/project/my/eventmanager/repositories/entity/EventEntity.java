package ru.project.my.eventmanager.repositories.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import ru.project.my.eventmanager.services.model.EventStatus;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "events")
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private UserEntity owner;
    @Column(name = "maxPlaces")
    private Integer maxPlaces;
    @Column(name = "occupiedPlaces")
    private Integer occupiedPlaces;
    @Column(name = "date")
    private LocalDateTime date;
    @Column(name = "cost")
    private Integer cost;
    @Column(name = "duration")
    private Integer duration;
    @ManyToOne
    @JoinColumn(name = "location_id")
    private LocationEntity location;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EventStatus status;
    @ManyToMany
    @JoinTable(name = "events_link_users",
            joinColumns=@JoinColumn(name="event_id", referencedColumnName="id"),
            inverseJoinColumns=@JoinColumn(name="user_id", referencedColumnName="id"))
    private List<UserEntity> users;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public UserEntity getOwner() {
        return owner;
    }
    public void setOwner(UserEntity owner) {
        this.owner = owner;
    }

    public Integer getMaxPlaces() {
        return maxPlaces;
    }
    public void setMaxPlaces(Integer maxPlaces) {
        this.maxPlaces = maxPlaces;
    }

    public Integer getOccupiedPlaces() {
        return occupiedPlaces;
    }
    public void setOccupiedPlaces(Integer occupiedPlaces) {
        this.occupiedPlaces = occupiedPlaces;
    }

    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Integer getCost() {
        return cost;
    }
    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getDuration() {
        return duration;
    }
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public LocationEntity getLocation() {
        return location;
    }
    public void setLocation(LocationEntity location) {
        this.location = location;
    }

    public EventStatus getStatus() {
        return status;
    }
    public void setStatus(EventStatus status) {
        this.status = status;
    }

    public List<UserEntity> getUsers() {
        return users;
    }
    public void setUsers(List<UserEntity> users) {
        this.users = users;
    }
}
