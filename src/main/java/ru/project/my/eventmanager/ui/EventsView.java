package ru.project.my.eventmanager.ui;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.project.my.eventmanager.services.EventService;
import ru.project.my.eventmanager.services.LocationService;
import ru.project.my.eventmanager.services.model.Event;
import ru.project.my.eventmanager.services.model.Location;
import ru.project.my.eventmanager.ui.converters.LocationConverter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ViewScoped
@Component
public class EventsView implements Serializable {
    private List<Event> events;
    private Event selectedEvent;
    private List<Location> locations;

    @Autowired
    private EventService eventService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private LocationConverter locationConverter;

    @PostConstruct
    public void init() {
        locations = locationService.getAllLocations();
        refreshEvents();
    }

    private void refreshEvents() {
        events = new ArrayList<>(eventService.getAllEvents());
    }

    public void createEvent() {
        try {
            selectedEvent = new Event();

            PrimeFaces.current().executeScript("PF('itemDialog').show()");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
        }
    }

    public void editEvent() {
        try {
            selectedEvent = eventService.getEvent(selectedEvent.getId());

            PrimeFaces.current().executeScript("PF('itemDialog').show()");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
        }
    }

    public void saveEvent() {
        try {
            if (selectedEvent.getId() == null) {
                eventService.createEvent(selectedEvent, selectedEvent.getLocation().getId());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Создано новое Событие", null));
            } else {
                eventService.updateEvent(selectedEvent, selectedEvent.getLocation().getId());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Изменения сохранены", null));
            }

            refreshEvents();
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
        }
    }

    public void deleteEvent() {
        try {
            if (selectedEvent == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Не выбрано Событие для удаления", null));
                return;
            }

            eventService.deleteEvent(selectedEvent.getId());

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Локация удалена", null));
            refreshEvents();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
        }
    }

    public List<Event> getEvents() {
        return events;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public LocationConverter getLocationConverter() {
        return locationConverter;
    }

    public Event getSelectedEvent() {
        return selectedEvent;
    }
    public void setSelectedEvent(Event selectedEvent) {
        this.selectedEvent = selectedEvent;
    }
}
