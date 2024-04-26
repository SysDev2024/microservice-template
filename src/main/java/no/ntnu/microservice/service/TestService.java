package no.ntnu.microservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestService {

    public void sendVerificationEmail(String test) {
        System.out.println("Hello World");
    }

}
