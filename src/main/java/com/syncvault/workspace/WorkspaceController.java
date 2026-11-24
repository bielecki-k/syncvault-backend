package com.syncvault.workspace;

import com.syncvault.storage.SaveFileResponse;
import com.syncvault.storage.model.Workspace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
//@RequestMapping("/api/workspace")
public class WorkspaceController {

    @Autowired
    private WorkspaceService workspaceService;

    public record FileResponse(String id, String name) {}
    public record WorkspaceResponse(Long id) {}

    @GetMapping("/api/files/{file_id}/content")
    public ResponseEntity<StreamingResponseBody> getFile(@PathVariable("file_id") UUID file_id) {
        log.info("Getting file_id: {}", file_id);

        Optional<StreamingResponseBody> fileFromMinIO = workspaceService.getFileFromMinIO(file_id);

        return fileFromMinIO.map(streamingResponseBody -> ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
//                                    .filename(objectKey, StandardCharsets.UTF_8)
                                .build()
                                .toString()
                ).body(streamingResponseBody)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/api/workspace")
    public ResponseEntity<WorkspaceResponse> postWorkspace(){
        Workspace workspace = workspaceService.saveWorkspace();

        log.info("Workspace {} posted successfully", workspace.getWorkspace_id());
        return ResponseEntity.ok(new WorkspaceResponse(workspace.getWorkspace_id()));
    }

    @PostMapping(
            value = "/api/workspace/{workspace_id}/file",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileResponse> postFile(@PathVariable("workspace_id") Long workspaceId,
                                                 @RequestParam("file") MultipartFile file)
    {
        if (workspaceService.getWorkspace(workspaceId).isPresent()) {
            SaveFileResponse saveFileResponse = workspaceService
                    .saveFileAndMetadata(workspaceService.getWorkspace(workspaceId).get(), file);

            log.info("File {} saved successfully",file.getOriginalFilename());
            return ResponseEntity.ok(new FileResponse(saveFileResponse.file_id().toString(),file.getOriginalFilename()));
        } else {
            log.info("workspace {} doesn't exists! ",workspaceId);
            return ResponseEntity.notFound().build();
        }
    }
}
