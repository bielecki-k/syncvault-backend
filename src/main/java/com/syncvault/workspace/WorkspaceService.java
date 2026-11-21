package com.syncvault.workspace;

import com.syncvault.storage.model.StoredFile;
import com.syncvault.storage.model.Workspace;
import com.syncvault.storage.repository.StoredFileRepository;
import com.syncvault.storage.repository.WorkspaceRepository;
import io.minio.ObjectWriteResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class WorkspaceService {

    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Autowired
    private StoredFileRepository storedFileRepository;


    public void saveFileMetadata(Workspace workspace, ObjectWriteResponse objectWriteResponse){

        StoredFile storedFile = StoredFile.builder().workspace(workspace)
                .originalFileName(objectWriteResponse.object())
                .objectETag(objectWriteResponse.etag())
                .createdAt(LocalDateTime.now())
                .build();
        storedFileRepository.save(storedFile);
    }

    public void saveWorkspace(){
        Workspace operatingWorkspace = Workspace.builder()
                .status("running")
                .name("win11-dev")
                .registration_datetime(LocalDateTime.now())
                .build();
        workspaceRepository.save(operatingWorkspace);
    }

    public Optional<Workspace> getWorkspace(Long workspaceId){
        return workspaceRepository.findById(workspaceId);
    }

}
