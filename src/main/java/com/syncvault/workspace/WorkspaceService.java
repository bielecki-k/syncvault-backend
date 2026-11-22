package com.syncvault.workspace;

import com.syncvault.files.minio.MinIOService;
import com.syncvault.storage.model.StoredFile;
import com.syncvault.storage.model.Workspace;
import com.syncvault.storage.repository.StoredFileRepository;
import com.syncvault.storage.repository.WorkspaceRepository;
import io.minio.ObjectWriteResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class WorkspaceService {

    @Autowired
    private MinIOService minioService;

    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Autowired
    private StoredFileRepository storedFileRepository;


    public void saveFileAndMetadata(Workspace workspace,MultipartFile file){
        ObjectWriteResponse objectWriteResponse = minioService.saveFile(file);  //minIO
        saveMetadata(workspace,objectWriteResponse);                            //postgreSQL
    }


    private void saveMetadata(Workspace workspace, ObjectWriteResponse objectWriteResponse){

        log.info("saveMetadata objectKey: {}",objectWriteResponse.object());
        StoredFile storedFile = StoredFile.builder().workspace(workspace)
                .originalFileName(objectWriteResponse.object())
                .objectETag(objectWriteResponse.etag())
                .createdAt(LocalDateTime.now())
                .build();
        storedFileRepository.save(storedFile);
    }

    public Workspace saveWorkspace(){
        Workspace operatingWorkspace = Workspace.builder()
                .status("running")
                .name("win11-dev")
                .registration_datetime(LocalDateTime.now())
                .build();
        return workspaceRepository.save(operatingWorkspace);
    }

    public Optional<Workspace> getWorkspace(Long workspaceId){
        return workspaceRepository.findById(workspaceId);
    }

}
