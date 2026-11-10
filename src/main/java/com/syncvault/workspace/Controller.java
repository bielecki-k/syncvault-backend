package com.syncvault.workspace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @Autowired
    private WorkspaceService workspaceService;

    @PostMapping("/api/workspace")
    public HttpStatus postWorkspace(){
        return workspaceService.saveWorkspace().isEmpty()
                ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
    }

}
