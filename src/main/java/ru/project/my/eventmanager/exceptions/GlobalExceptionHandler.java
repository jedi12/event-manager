package ru.project.my.eventmanager.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.project.my.eventmanager.controllers.dto.ErrorMessageResponse;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class.getName());

    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class, ConditionUnacceptableException.class})
    public ResponseEntity<ErrorMessageResponse> handleValidationException(Exception e) {
        String detail = "";
        if (e instanceof MethodArgumentNotValidException manve) {
            detail = manve.getBindingResult().getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
        } else if (e instanceof HttpMessageNotReadableException hmnre) {
            detail = hmnre.getMessage();
        } else if (e instanceof ConditionUnacceptableException cue) {
            detail = cue.getMessage();
        }
        log.warn("Некорректный запрос: {}", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessageResponse("Некорректный запрос", detail, LocalDateTime.now()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessageResponse> handleNotFoundException(NotFoundException e) {
        log.warn("Сущность не найдена: {}", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessageResponse("Сущность не найдена", e.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessageResponse> handleException(Exception e) {
        log.error("Внутренняя ошибка сервера: {}", e.getMessage(), e);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorMessageResponse("Внутренняя ошибка сервера", e.getMessage(), LocalDateTime.now()));
    }
}
