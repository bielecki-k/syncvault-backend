package com.syncvault.workspace;

import com.syncvault.files.minio.MinIOService;
import com.syncvault.storage.SaveFileResponse;
import com.syncvault.storage.model.Workspace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
//@RequestMapping("/api/workspace")
public class WorkspaceController {

    @Autowired
    private WorkspaceService workspaceService;

    @Autowired
    private MinIOService minioService;

    public record FileResponse(String id, String name) {}
    public record WorkspaceResponse(Long id) {}

    @GetMapping("/api/files/{objectKey}/content")
    public ResponseEntity<StreamingResponseBody> getFile(@PathVariable("objectKey") String objectKey) {
        log.info("Getting file for object {}", objectKey);
        StreamingResponseBody stream = outputStream -> {
            try (InputStream inputStream =
                         minioService.getFile(objectKey)) {

                inputStream.transferTo(outputStream);
            } catch (Exception e) {
                throw new IOException("Failed to download file", e);
            }
        };

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename(objectKey, StandardCharsets.UTF_8)
                                .build()
                                .toString()
                )
                .body(stream);
    }

    @PostMapping("/api/workspace")
    public ResponseEntity<WorkspaceResponse> postWorkspace(){
        Workspace workspace = workspaceService.saveWorkspace();

        log.info("Workspace posted successfully");
        return ResponseEntity.ok(new WorkspaceResponse(workspace.getWorkspace_id()));
    }

    @PostMapping(
            value = "/api/workspace/{workspace_id}/file",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileResponse> postFile(@PathVariable("workspace_id") Long workspaceId,
                                                 @RequestParam("file") MultipartFile file)
    {
        if (workspaceService.getWorkspace(workspaceId).isPresent()) {
            log.info("workspace {} exists",workspaceId);

            SaveFileResponse saveFileResponse = workspaceService
                    .saveFileAndMetadata(workspaceService.getWorkspace(workspaceId).get(), file);

            log.info("File saved successfully");
            return ResponseEntity.ok(new FileResponse(saveFileResponse.file_id().toString(),file.getOriginalFilename()));
        } else {
            log.info("workspace {} doesn't exists! ",workspaceId);
            return ResponseEntity.notFound().build();
        }
    }
}
