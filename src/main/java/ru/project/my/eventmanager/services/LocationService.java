package ru.project.my.eventmanager.services;

import org.springframework.stereotype.Service;
import ru.project.my.eventmanager.converters.LocationEntityConverter;
import ru.project.my.eventmanager.exceptions.ConditionUnacceptableException;
import ru.project.my.eventmanager.exceptions.NotFoundException;
import ru.project.my.eventmanager.repositories.EventRepository;
import ru.project.my.eventmanager.repositories.LocationRepository;
import ru.project.my.eventmanager.repositories.entity.LocationEntity;
import ru.project.my.eventmanager.services.model.Location;

import java.util.List;

@Service
public class LocationService {
    private final LocationRepository locationRepository;
    private final LocationEntityConverter converter;
    private final EventRepository eventRepository;

    public LocationService(LocationRepository locationRepository, LocationEntityConverter converter, EventRepository eventRepository) {
        this.locationRepository = locationRepository;
        this.converter = converter;
        this.eventRepository = eventRepository;
    }

    public List<Location> getAllLocations() {
        List<LocationEntity> locationEntity = locationRepository.findAll();

        return converter.toLocation(locationEntity);
    }

    public Location createLocation(Location location) {
        if (locationRepository.existsByName(location.getName())) {
            throw new ConditionUnacceptableException("Локация с таким названием уже существует. Измените значение атрибута 'name'");
        }

        LocationEntity locationEntity = converter.toEntity(location);
        locationEntity = locationRepository.save(locationEntity);

        return converter.toLocation(locationEntity);
    }

    public void deleteLocation(Long locationId) {
        LocationEntity locationEntity = locationRepository.findById(locationId)
                .orElseThrow(() -> new NotFoundException("Локация с locationId=%s отсутствует в системе".formatted(locationId)));

        if (eventRepository.eventsWithLocationCount(locationId) > 0) {
            throw new ConditionUnacceptableException("Локация с locationId=%s зарезервирована мероприятиями. Удаление запрещено.".formatted(locationId));
        }

        locationRepository.delete(locationEntity);
    }

    public Location getLocation(Long locationId) {
        LocationEntity locationEntity = locationRepository.findById(locationId)
                .orElseThrow(() -> new NotFoundException("Локация с locationId=%s отсутствует в системе".formatted(locationId)));

        return converter.toLocation(locationEntity);
    }

    public Location updateLocation(Location updatedLocation) {
        LocationEntity existsLocation = locationRepository.findById(updatedLocation.getId())
                .orElseThrow(() -> new NotFoundException("Локация с locationId=%s отсутствует в системе".formatted(updatedLocation.getId())));

        if (updatedLocation.getCapacity().compareTo(existsLocation.getCapacity()) < 0) {
            if (eventRepository.eventsWithLocationCount(updatedLocation.getId()) > 0) {
                throw new ConditionUnacceptableException("Нельзя уменьшать вместительность локации, так как она уже используется мероприятиями. Измените значение 'capacity'");
            }
        }

        LocationEntity locationEntity = converter.toEntity(updatedLocation);
        locationEntity = locationRepository.save(locationEntity);

        return converter.toLocation(locationEntity);
    }
}
