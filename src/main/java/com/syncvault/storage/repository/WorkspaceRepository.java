package com.syncvault.storage.repository;

import com.syncvault.storage.model.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.jar.JarEntry;

public interface WorkspaceRepository extends JpaRepository<Workspace,Long> {
}
