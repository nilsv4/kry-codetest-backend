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
import java.net.*;
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
        URL url;
        try {
            url = new URL(host + ":" + port);
        } catch (MalformedURLException e) {
            return ServiceState.DOWN;
        }

        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection)url.openConnection();
        } catch (IOException e) {
            return ServiceState.DOWN;
        }

        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        try {
            connection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            return ServiceState.DOWN;
        }

        try {
            connection.connect();
        } catch (IOException e) {
            return ServiceState.DOWN;
        }

        try {
            if (connection.getResponseCode() != HttpURLConnection.HTTP_UNAVAILABLE) {
                return ServiceState.UP;
            }
        } catch (IOException e) {
            return ServiceState.DOWN;
        }

        return ServiceState.DOWN;
    }
}
