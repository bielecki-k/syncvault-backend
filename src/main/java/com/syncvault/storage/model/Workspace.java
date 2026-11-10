package com.syncvault.storage.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name="workspace")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Workspace {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String name;
    private String status; //todo @Enumerated
    private LocalDateTime registration_datetime;

}
