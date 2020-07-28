package com.org.controllers.errorhandling;

import com.org.FuelAppException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.access.InvalidInvocationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.time.LocalDateTime;

@ControllerAdvice
public class ControllerErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = Throwable.class)
    public ResponseEntity<Object> handleErrors(Throwable throwable, WebRequest request, ServletWebRequest servletWebRequest) {
        Throwable realError = throwable;
        if (throwable instanceof UndeclaredThrowableException) {
            realError = ((UndeclaredThrowableException) throwable).getUndeclaredThrowable();
        }

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        if (realError instanceof FuelAppException || realError instanceof IOException || realError instanceof InvalidInvocationException) {
            httpStatus = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<>(FuelAppRestError
                .builder()
                .status(httpStatus.value())
                .message(realError.getMessage())
                .path(request.getDescription(false).replace("uri=", ""))
                .timestamp(LocalDateTime.now()).build(), httpStatus);
    }
}
