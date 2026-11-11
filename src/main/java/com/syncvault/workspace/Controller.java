package com.syncvault.workspace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @Autowired
    private WorkspaceService workspaceService;

    @PostMapping("/api/workspace")
    public ResponseEntity<String> postWorkspace(){

        workspaceService.saveWorkspace();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Posted", "true");
        return new ResponseEntity<String>("Workspace Posted", responseHeaders, HttpStatus.CREATED);
    }

}
