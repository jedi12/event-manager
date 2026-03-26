package ru.project.my.eventmanager.services.model;

import java.util.List;

public class LocationList {
    private List<Location> locations;

    public LocationList() {}

    public LocationList(List<Location> locations) {
        this.locations = locations;
    }

    public List<Location> getLocations() {
        return locations;
    }
    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }
}
