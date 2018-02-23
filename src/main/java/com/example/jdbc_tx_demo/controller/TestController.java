package com.example.jdbc_tx_demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jdbc_tx_demo.dao.H2DAOImpl;
import com.example.jdbc_tx_demo.service.TestService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class TestController {

    @Autowired
    private TestService service;

    @Autowired
    private H2DAOImpl dao;
    
    @GetMapping("/createSchema")
    public void createSchema() {
        dao.createSchema();
    }
    
    @GetMapping("/dropSchema")
    public void dropSchema() {
        dao.dropSchema();
    }
    
    @GetMapping("/cleanAll")
    public void cleanAll() {
        dao.cleanAll();
    }

    @GetMapping("/test")
    public String test() {
        long pid = Thread.currentThread().getId();
        log.info("\n\n\n\n==== Start PID {} ====\n", pid);
        
        service.randomInsert2();

        log.info("\n\n\n\n==== End PID {} ====\n", pid);
        return "PID = " + pid;
    }
}
