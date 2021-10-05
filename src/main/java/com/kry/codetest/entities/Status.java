package com.kry.codetest.entities;

import com.kry.codetest.entities.enums.ServiceState;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "status")
public class Status {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "service_id", referencedColumnName = "id")
    private Service service;

    private boolean state;

    @CreationTimestamp
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    public UUID getId() {
        return id;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public ServiceState getState() {
        if (this.state)
            return ServiceState.UP;

        return ServiceState.DOWN;
    }

    public void setState(ServiceState state) {
        if (state == ServiceState.UP) {
            this.state = Boolean.TRUE;
        } else {
            this.state = Boolean.FALSE;
        }
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getLocalCreatedAt() {
        return Instant.ofEpochMilli(createdAt.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
