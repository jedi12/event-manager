package ru.project.my.eventmanager.ui;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ViewScoped
@Component
public class MainMenuView implements Serializable {
    private List<MenuItem> menuItems;

    @PostConstruct
    public void init() {
        menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("Пользователи", "pi pi-users", "#bc1e22", "/users.xhtml"));
        menuItems.add(new MenuItem("Локации", "pi pi-building", "#5c64d7", "/locations.xhtml"));
        menuItems.add(new MenuItem("События", "pi pi-megaphone", "#13ddb0", "/events.xhtml"));
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }
}
