package com.kry.codetest.controllers;

import com.kry.codetest.entities.Status;
import com.kry.codetest.repositories.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/status")
public class StatusController {
    @Autowired
    private StatusRepository statusRepository;

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/{serviceId}")
    public ResponseEntity<List<Status>> getAllStatusesByServiceId(@PathVariable(value = "serviceId") UUID id) {
        List<Status> statuses = statusRepository.findStatusesByServiceIdOrderByCreatedAt(id);

        if (!statuses.isEmpty())
            return ResponseEntity.ok().body(statuses);

        return ResponseEntity.notFound().build();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/{serviceId}/latest")
    public ResponseEntity<Status> getLatestStatusByServiceId(@PathVariable(value = "serviceId") UUID id) {
        Status status = statusRepository.findFirstByServiceIdOrderByCreatedAtDesc(id);

        if (status != null)
            return ResponseEntity.ok().body(status);

        return ResponseEntity.notFound().build();
    }
}
