package com.example.UserMicroServiceProject.infrastructure.config;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        log.error("Error occurred while calling {}: {}", methodKey, response.reason());

        return switch (response.status()) {
            case 400 -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request");
            case 401 -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            case 403 -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden");
            case 404 -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found");
            case 500 -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
            default -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown Error");
        };
    }
}