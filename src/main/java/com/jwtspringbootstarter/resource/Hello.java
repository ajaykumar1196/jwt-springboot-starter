package com.jwtspringbootstarter.resource;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.HttpStatus.OK;

@Controller
@RequestMapping("/api")
public class Hello {

    @GetMapping("/user")
    public ResponseEntity<String> getUser(){
        return new ResponseEntity<String>("Hello",
                OK);
    }

    @GetMapping("/admin")
    public ResponseEntity<String> getAllUsers(){
        return new ResponseEntity<String>("Hello Admin",
                OK);
    }
}
