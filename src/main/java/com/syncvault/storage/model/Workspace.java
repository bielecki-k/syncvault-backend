package com.syncvault.storage.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="workspace")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Workspace {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "workspace_id")
    private Long workspace_id;
    private String name;
    private String status; //todo @Enumerated
    private LocalDateTime registration_datetime;

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL)
    private List<StoredFile> file;

}
