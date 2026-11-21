package com.syncvault.workspace;

import com.syncvault.files.minio.MinIOService;
import com.syncvault.storage.model.Workspace;
import io.minio.ObjectWriteResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Slf4j
@RestController
//@RequestMapping("/api/workspace")
public class WorkspaceController {

    @Autowired
    private WorkspaceService workspaceService;

    @Autowired
    private MinIOService minioService;

    @PostMapping("/api/workspace")
    public ResponseEntity<String> postWorkspace(){
        log.info("postWorkspace()");
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

            ObjectWriteResponse objectWriteResponse = minioService.saveFile(file);  //minIO
            workspaceService.saveFileMetadata(workspace.get(),objectWriteResponse);     //postgreSQL

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("file_posted", "true");
            responseHeaders.set("file_etag", objectWriteResponse.etag());
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
