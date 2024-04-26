package no.ntnu.microservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceClient {

    private final String authServiceUrl = "http://auth-service/validateToken";

    public boolean validateToken(String token) {
        return true;

    }

}
