package com.kry.codetest.utils;

import com.kry.codetest.entities.Service;
import com.kry.codetest.entities.Status;
import com.kry.codetest.entities.enums.ServiceState;
import com.kry.codetest.repositories.ServiceRepository;
import com.kry.codetest.repositories.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;
import java.util.UUID;

@Component
public class ServicePoller {
    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Scheduled(fixedDelay = 60000)
    private void pollAllServices() {
        List<Service> services = serviceRepository.findAll();

        for (Service service : services) {
            ServiceState serviceState = pingHost(service.getUrl(), service.getPort());
            Status status = new Status();

            status.setService(service);
            status.setState(serviceState);

            statusRepository.save(status);
        }
    }

    public void pollService(UUID serviceId) {
        Service service = serviceRepository.findById(serviceId);

        ServiceState serviceState = pingHost(service.getUrl(), service.getPort());
        Status status = new Status();

        status.setService(service);
        status.setState(serviceState);

        statusRepository.save(status);
    }

    public static ServiceState pingHost(String host, int port) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), 5000);
            return ServiceState.UP;
        } catch (IOException e) {
            return ServiceState.DOWN;
        }
    }
}
