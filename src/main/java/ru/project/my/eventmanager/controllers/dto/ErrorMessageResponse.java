package ru.project.my.eventmanager.controllers.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class ErrorMessageResponse {
    private String message;
    private String detailedMessage;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateTime;

    public ErrorMessageResponse(String message, String detailedMessage, LocalDateTime dateTime) {
        this.message = message;
        this.detailedMessage = detailedMessage;
        this.dateTime = dateTime;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetailedMessage() {
        return detailedMessage;
    }
    public void setDetailedMessage(String detailedMessage) {
        this.detailedMessage = detailedMessage;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
