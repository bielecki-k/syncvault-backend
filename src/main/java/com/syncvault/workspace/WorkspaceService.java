package com.syncvault.workspace;

import com.syncvault.files.minio.MinIOService;
import com.syncvault.storage.SaveFileResponse;
import com.syncvault.storage.model.StoredFile;
import com.syncvault.storage.model.Workspace;
import com.syncvault.storage.repository.StoredFileRepository;
import com.syncvault.storage.repository.WorkspaceRepository;
import io.minio.ObjectWriteResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class WorkspaceService {

    @Autowired
    private MinIOService minioService;

    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Autowired
    private StoredFileRepository storedFileRepository;


    public SaveFileResponse saveFileAndMetadata(Workspace workspace, MultipartFile file){
        ObjectWriteResponse objectWriteResponse = minioService.saveFile(file);                  //minIO
        UUID file_id = saveMetadata(workspace, objectWriteResponse, file.getOriginalFilename());//postgreSQL
        return new SaveFileResponse(objectWriteResponse,file_id);
    }

    public Optional<StreamingResponseBody> getFileFromMinIO(UUID file_id) {

        Optional<StoredFile> optionalStoredFile = storedFileRepository.findById(file_id);
        if(optionalStoredFile.isPresent()){

            StreamingResponseBody stream = outputStream -> {
                try (InputStream inputStream =
                             minioService.getFile(optionalStoredFile.get().getObjectKey())) {

                    inputStream.transferTo(outputStream);
                } catch (Exception e) {
                    throw new IOException("Failed to download file", e);
                }
            };

            return Optional.of(stream);
        } else { return Optional.empty();}

    }


    private UUID saveMetadata(Workspace workspace, ObjectWriteResponse objectWriteResponse, @Nullable String originalFilename){
        StoredFile storedFile = StoredFile.builder().workspace(workspace)
                .objectKey(objectWriteResponse.object())
                .originalFileName(originalFilename)
                .objectETag(objectWriteResponse.etag())
                .createdAt(LocalDateTime.now())
                .build();
        return storedFileRepository.save(storedFile).getStored_file_id();
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
