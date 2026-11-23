package com.syncvault.storage.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="stored_file")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class StoredFile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "stored_file_id")
    private UUID stored_file_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable=false)
    private Workspace workspace;

    private String objectKey;
    private String originalFileName;
    private String objectETag;
    private LocalDateTime createdAt;


}
