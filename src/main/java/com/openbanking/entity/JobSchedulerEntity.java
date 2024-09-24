package com.openbanking.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "job_scheduler")
public class JobSchedulerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "ref_id", nullable = false)
    private Long refId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "description")
    private String description;

    @Column(name = "operation_group")
    private String operationGroup;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "type")
    private String type;

    @Column(name = "time_start")
    private LocalDateTime timeStart;

    @Column(name = "note")
    private String note;

    @Column(name = "retry_times")
    private Integer retryTimes;
}

