package ru.project.my.eventmanager.ui;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.project.my.eventmanager.security.AuthenticationService;
import ru.project.my.eventmanager.services.LocationService;
import ru.project.my.eventmanager.services.model.Location;
import ru.project.my.eventmanager.services.model.Role;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ViewScoped
@Component
public class LocationsView implements Serializable {
    private List<Location> locations;
    private Location selectedLocation;
    private boolean admin;

    @Autowired
    private LocationService locationService;

    @Autowired
    private AuthenticationService authenticationService;

    @PostConstruct
    public void init() {
        admin = Role.ADMIN.equals(authenticationService.getCurrentUser().getRole());

        refreshLocations();
    }

    private void refreshLocations() {
        locations = new ArrayList<>(locationService.getAllLocations());
    }

    public void createLocation() {
        try {
            selectedLocation = new Location();

            PrimeFaces.current().executeScript("PF('itemDialog').show()");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
        }
    }

    public void editLocation() {
        try {
            selectedLocation = locationService.getLocation(selectedLocation.getId());

            PrimeFaces.current().executeScript("PF('itemDialog').show()");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
        }
    }

    public void saveLocation() {
        try {
            if (selectedLocation.getId() == null) {
                locationService.createLocation(selectedLocation);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Создана новая Локация", null));
            } else {
                locationService.updateLocation(selectedLocation);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Изменения сохранены", null));
            }

            refreshLocations();
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
        }
    }

    public void deleteLocation() {
        try {
            if (selectedLocation == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Не выбрана Локация для удаления", null));
                return;
            }

            locationService.deleteLocation(selectedLocation.getId());

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Локация удалена", null));
            refreshLocations();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
        }
    }

    public List<Location> getLocations() {
        return locations;
    }

    public boolean isAdmin() {
        return admin;
    }

    public Location getSelectedLocation() {
        return selectedLocation;
    }
    public void setSelectedLocation(Location selectedLocation) {
        this.selectedLocation = selectedLocation;
    }
}
