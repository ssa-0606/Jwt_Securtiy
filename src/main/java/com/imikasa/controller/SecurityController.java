package com.imikasa.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityController {

    @GetMapping("/test")
    public String test(){
        return "Hello World";
    }

    @GetMapping("/query")
    @PreAuthorize("hasAnyAuthority('query','all')")
    public String query(){
        return "query ";
    }

    @GetMapping("/save")
    @PreAuthorize("hasAnyAuthority('save','all')")
    public String save(){
        return "save";
    }

    @GetMapping("/del")
    @PreAuthorize("hasAnyAuthority('del','all')")
    public String del(){
        return "del";
    }

    @GetMapping("/update")
    @PreAuthorize("hasAnyAuthority('update','all')")
    public String update(){
        return "update";
    }

}
