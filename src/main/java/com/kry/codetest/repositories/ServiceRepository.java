package com.kry.codetest.repositories;

import com.kry.codetest.entities.Service;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ServiceRepository extends CrudRepository<Service, Long> {

    @Override
    List<Service> findAll();

    Service findById(UUID Id);

}
