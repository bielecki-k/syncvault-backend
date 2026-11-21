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

    public void saveWorkspace(){
        Workspace operatingWorkspace = Workspace.builder()
                .status("running")
                .name("win11-dev")
                .registration_datetime(LocalDateTime.now())
                .build();
        workspaceRepository.save(operatingWorkspace);
    }

    public Boolean checkWorkspaceExists(Long workspaceId){
        return workspaceRepository.findById(workspaceId).isPresent();
    }

}
