package com.kry.codetest.controllers;

import com.kry.codetest.entities.Service;
import com.kry.codetest.entities.Status;
import com.kry.codetest.repositories.ServiceRepository;
import com.kry.codetest.repositories.StatusRepository;
import com.kry.codetest.utils.ServicePoller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/service")
public class ServiceController {
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private ServicePoller servicePoller;

    @GetMapping
    @CrossOrigin(origins = "http://localhost:4200")
    public List<Service> getAllServices() {
        return serviceRepository.findAll();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/{id}")
    public ResponseEntity<Service> getServiceById(@PathVariable(value = "id") long id) {
        Optional<Service> service = serviceRepository.findById(id);

        return service.map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping("/{id}")
    public void deleteService(@PathVariable(value = "id") UUID id) {
        List<Status> statuses = statusRepository.findStatusesByServiceIdOrderByCreatedAt(id);
        statusRepository.deleteAll(statuses);

        serviceRepository.delete(serviceRepository.findById(id));
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping
    public Service addOrUpdateService(@Validated @RequestBody Service service) {
        Service _service = serviceRepository.save(service);
        servicePoller.pollService(_service.getId());

        return _service;
    }
}
