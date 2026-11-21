package com.syncvault.storage.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name="stored_file")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoredFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stored_file_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable=false)
    private Workspace workspace;

    private String originalFileName;
    private String objectETag;
    private LocalDateTime createdAt;


}
