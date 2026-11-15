package com.syncvault.workspace;

import com.syncvault.files.minio.MinIOService;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
//@RequestMapping("/api/workspace")
public class Controller {

    @Autowired
    private WorkspaceService workspaceService;

    @Autowired
    private MinIOService minioService;

    @PostMapping("/api/workspace")
    public ResponseEntity<String> postWorkspace(){

        workspaceService.saveWorkspace();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Posted", "true");
        return new ResponseEntity<>("Workspace Posted", responseHeaders, HttpStatus.CREATED);
    }

    @PostMapping(value = "/api/workspace/{workspace_id}/file",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> postFile(@RequestParam("workspace_id") Long workspace_id) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        minioService.saveFile();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("file_id", "!fill_me!");
        return new ResponseEntity<>("File Posted", responseHeaders, HttpStatus.CREATED);
    }

}
