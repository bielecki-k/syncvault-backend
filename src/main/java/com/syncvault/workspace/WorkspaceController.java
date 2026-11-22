package com.syncvault.workspace;

import com.syncvault.files.minio.MinIOService;
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
import java.util.Optional;

@Slf4j
@RestController
//@RequestMapping("/api/workspace")
public class WorkspaceController {

    @Autowired
    private WorkspaceService workspaceService;

    @Autowired
    private MinIOService minioService;

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
    public ResponseEntity<String> postWorkspace(){
        workspaceService.saveWorkspace();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Posted", "true");
        log.info("Workspace posted successfully");
        return new ResponseEntity<>("Workspace Posted", responseHeaders, HttpStatus.CREATED);
    }

    @PostMapping(
            value = "/api/workspace/{workspace_id}/file",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> postFile(@PathVariable("workspace_id") Long workspaceId,
                                           @RequestParam("file") MultipartFile file)
    {
        log.debug("postFile()");
        Optional<Workspace> workspace = workspaceService.getWorkspace(workspaceId);
        if (workspace.isPresent()) {
            log.info("workspace {} exists",workspaceId);
            workspaceService.saveFileAndMetadata(workspace.get(),file);

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("file_posted", "true");
//            responseHeaders.set("file_etag", objectWriteResponse.etag()); //todo zwrocic id
            log.info("File saved successfully");
            return new ResponseEntity<>("FILE_POSTED", responseHeaders, HttpStatus.CREATED);
        } else {
            log.info("workspace {} doesn't exists! ",workspaceId);
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("file_posted", "false");
            return new ResponseEntity<>("FILE_NOT_POSTED!", responseHeaders, HttpStatus.BAD_REQUEST);
        }
    }
}
