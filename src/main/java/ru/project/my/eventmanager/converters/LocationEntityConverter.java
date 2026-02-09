package ru.project.my.eventmanager.converters;

import org.springframework.stereotype.Component;
import ru.project.my.eventmanager.repositories.entity.LocationEntity;
import ru.project.my.eventmanager.services.model.Location;

import java.util.Collections;
import java.util.List;

@Component
public class LocationEntityConverter {

    public Location toLocation(LocationEntity locationEntity) {
        if (locationEntity == null) return null;

        Location location = new Location();
        location.setId(locationEntity.getId());
        location.setName(locationEntity.getName());
        location.setAddress(locationEntity.getAddress());
        location.setCapacity(locationEntity.getCapacity());
        location.setDescription(locationEntity.getDescription());
        return location;
    }

    public List<Location> toLocation(List<LocationEntity> locationEntityList) {
        if (locationEntityList == null) return Collections.emptyList();
        return locationEntityList.stream().map(this::toLocation).toList();
    }

    public LocationEntity toEntity(Location location) {
        if (location == null) return null;

        LocationEntity locationEntity = new LocationEntity();
        locationEntity.setId(location.getId());
        locationEntity.setName(location.getName());
        locationEntity.setAddress(location.getAddress());
        locationEntity.setCapacity(location.getCapacity());
        locationEntity.setDescription(location.getDescription());
        return locationEntity;
    }

    public List<LocationEntity> toEntity(List<Location> locationList) {
        if (locationList == null) return Collections.emptyList();
        return locationList.stream().map(this::toEntity).toList();
    }
}
