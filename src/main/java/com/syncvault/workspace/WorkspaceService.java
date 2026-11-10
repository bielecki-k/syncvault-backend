package com.syncvault.workspace;

import com.syncvault.storage.model.Workspace;
import com.syncvault.storage.repository.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class WorkspaceService {

    @Autowired
    private WorkspaceRepository workspaceRepository;

    public Optional<Workspace> saveWorkspace(){
        Workspace operatingWorkspace = Workspace.builder()
                .status("running")
                .name("win11-dev")
                .registration_datetime(LocalDateTime.now())
                .build();
        return Optional.of(workspaceRepository.save(operatingWorkspace));
    }

}
