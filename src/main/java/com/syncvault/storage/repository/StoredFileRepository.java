package com.syncvault.storage.repository;

import com.syncvault.storage.model.StoredFile;
import com.syncvault.storage.model.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoredFileRepository extends JpaRepository<StoredFile,Long> {
}
