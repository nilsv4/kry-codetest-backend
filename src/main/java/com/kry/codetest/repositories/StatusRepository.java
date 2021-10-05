package com.kry.codetest.repositories;

import com.kry.codetest.entities.Status;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StatusRepository extends CrudRepository<Status, Long> {

    @Override
    List<Status> findAll();

    List<Status> findStatusesByServiceIdOrderByCreatedAt(UUID serviceId);

    Status findFirstByServiceIdOrderByCreatedAtDesc(UUID serviceId);

}
