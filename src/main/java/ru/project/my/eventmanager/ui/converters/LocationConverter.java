package ru.project.my.eventmanager.ui.converters;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.ConverterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.project.my.eventmanager.services.LocationService;
import ru.project.my.eventmanager.services.model.Location;

@Component
public class LocationConverter implements Converter<Location> {
    @Autowired
    private LocationService locationService;

    @Override
    public Location getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.trim().isEmpty()) return null;

        try {
            return locationService.getLocation(Long.parseLong(value));
        }
        catch (NumberFormatException e) {
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid location."));
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Location value) {
        if (value == null || value.getId() == null) return null;

        return String.valueOf(value.getId());
    }
}
