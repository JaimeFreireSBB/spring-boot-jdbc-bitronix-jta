package com.example.jdbc_tx_demo;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.jdbc_tx_demo.dao.H2DAOImpl;
import com.example.jdbc_tx_demo.service.TestService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionServiceTest {

    @Autowired
    private TestService service;

    @Autowired
    private H2DAOImpl dao;
    
    @Before
    public void before() {
        dao.createSchema();
    }

    @After
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
