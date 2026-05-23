package com.clinicflow.bootstrap.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.time.OffsetDateTime;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/system")
class SystemEchoController {

    @PostMapping("/echo")
    EchoResponse echo(@Valid @RequestBody EchoRequest request) {
        return new EchoResponse(request.message(), OffsetDateTime.now());
    }

    record EchoRequest(@NotBlank String message) {
    }

    record EchoResponse(String message, OffsetDateTime echoedAt) {
    }
}
