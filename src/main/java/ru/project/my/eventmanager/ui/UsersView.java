package ru.project.my.eventmanager.ui;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.project.my.eventmanager.services.UserService;
import ru.project.my.eventmanager.services.model.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ViewScoped
@Component
public class UsersView implements Serializable {
    private List<User> users;
    private User selectedUser;

    @Autowired
    private UserService userService;

    @PostConstruct
    public void init() {
        refreshUsers();
    }

    private void refreshUsers() {
        users = new ArrayList<>(userService.getAllUsers());
    }

    public void createUser() {
        try {
            selectedUser = new User();

            PrimeFaces.current().executeScript("PF('itemDialog').show()");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
        }
    }

    public void editUser() {
        try {
            selectedUser = userService.getUser(selectedUser.getId());

            PrimeFaces.current().executeScript("PF('itemDialog').show()");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
        }
    }

    public void saveUser() {
        try {
            userService.createUser(selectedUser);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Создан новый Пользователь", null));

            refreshUsers();
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
        }
    }

    public List<User> getUsers() {
        return users;
    }

    public User getSelectedUser() {
        return selectedUser;
    }
    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }
}
