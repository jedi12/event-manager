package ru.project.my.eventmanager.converters;

import org.springframework.stereotype.Component;
import ru.project.my.eventmanager.controllers.dto.LocationDto;
import ru.project.my.eventmanager.services.model.Location;

import java.util.List;

@Component
public class LocationDtoConverter {

    public Location toLocation(LocationDto locationDto, Long locationId) {
        if (locationDto == null) return null;

        Location location = new Location();
        location.setId(locationId);
        location.setName(locationDto.getName());
        location.setAddress(locationDto.getAddress());
        location.setCapacity(locationDto.getCapacity());
        location.setDescription(locationDto.getDescription());
        return location;
    }

    public LocationDto toDto(Location location) {
        if (location == null) return null;

        LocationDto locationDto = new LocationDto();
        locationDto.setId(location.getId());
        locationDto.setName(location.getName());
        locationDto.setAddress(location.getAddress());
        locationDto.setCapacity(location.getCapacity());
        locationDto.setDescription(location.getDescription());
        return locationDto;
    }

    public List<LocationDto> toDto(List<Location> locations) {
        return locations.stream().map(this::toDto).toList();
    }
}
