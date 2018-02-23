package com.example.jdbc_tx_demo.service;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.jdbc_tx_demo.dao.H2DAOImpl;
import com.example.jdbc_tx_demo.dao.SqlServerDAOImpl;

@Service
@Transactional
public class TestService {

    @Autowired
    private H2DAOImpl dao1;

    @Autowired
    private SqlServerDAOImpl dao2;

    private SecureRandom rand = new SecureRandom();

    public void randomInsert() {
        long id = rand.nextLong();
        dao1.insertOne("V_" + id);
    }
    
    @Transactional
    public void randomInsert2() {
        long id = rand.nextLong();
        dao1.insertOne("V_" + id);

        id = rand.nextLong();
        dao2.insertOne("V_" + id);
        
        id = rand.nextLong();
        dao1.insertOne("V_" + id);
    }

    public void action1() {
        dao1.insertOne("value1");
        dao1.insertOne("value2");
        dao1.insertOne("value3");
    }
    
    public void action2() {
        dao1.insertOne("value1");
        dao1.insertOne("value2");
        dao1.insertOne("value2"); // dup error...
    }
}
