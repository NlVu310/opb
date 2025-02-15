package com.openbanking.entity;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.openbanking.comon.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "job_scheduler")
public class JobSchedulerEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "ref_id", nullable = false)
    private Long refId;

    @Column(name = "description")
    private String description;

    @Column(name = "operation_group")
    @JsonRawValue
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

