package com.syncvault.storage.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="workspace")
public class Workspace {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String name;
    private String status; //todo enum
    private LocalDateTime connection_start;

}
