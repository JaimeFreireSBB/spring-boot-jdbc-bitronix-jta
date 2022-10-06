package com.example.jdbc_tx_demo;


import com.example.jdbc_tx_demo.dao.H2DAOImpl;
import com.example.jdbc_tx_demo.service.TestService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TransactionServiceTest {

    @Autowired
    private TestService service;

    @Autowired
    private H2DAOImpl dao;
    
    @BeforeEach
    public void before() {
        dao.createSchema();
    }

    @AfterEach
    public void after() {
        dao.dropSchema();
    }
    
    @Test
    public void action1() {
        assertEquals(0, dao.selectAll());
        service.action1();
        assertEquals(3, dao.selectAll());
    }

    @Test
    public void action2() {
        assertEquals(0, dao.selectAll());
        try {
            service.action2();
        } catch (DuplicateKeyException e) {
            // good
        }
        assertEquals(0, dao.selectAll());
    }
}
