package com.example.jdbc_tx_demo.service;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.jdbc_tx_demo.dao.H2DAOImpl;

@Service
@Transactional
public class TestService {

    @Autowired
    private H2DAOImpl dao;

    private SecureRandom rand = new SecureRandom();

    public void randomInsert() {
        long id = rand.nextLong();
        dao.insertOne("V_" + id);
    }

    public void action1() {
        dao.insertOne("value1");
        dao.insertOne("value2");
        dao.insertOne("value3");
    }
    
    public void action2() {
        dao.insertOne("value1");
        dao.insertOne("value2");
        dao.insertOne("value2"); // dup error...
    }
}
