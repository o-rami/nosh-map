package nosh.nosh_map_server.controllers;

import nosh.nosh_map_server.domain.ActionStatus;
import nosh.nosh_map_server.domain.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ErrorResponse {
    public static <T> ResponseEntity<Object> build(Result<T> result) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (result.getStatus() == null || result.getStatus() == ActionStatus.INVALID) {
            status = HttpStatus.BAD_REQUEST;
        } else if (result.getStatus() == ActionStatus.NOT_FOUND) {
            status = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(result.getMessages(), status);
    }
}
