package com.clinicflow.bootstrap.api;

import java.time.OffsetDateTime;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bootstrap")
class BootstrapController {

    @GetMapping("/ping")
    Map<String, Object> ping() {
        return Map.of(
            "status", "ok",
            "service", "clinicflow",
            "timestamp", OffsetDateTime.now()
        );
    }
}
