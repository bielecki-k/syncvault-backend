package com.syncvault.workspace.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @PostMapping("/api/workspace")
    public HttpStatus postWorkspace(){
        return HttpStatus.OK;
    }

}
