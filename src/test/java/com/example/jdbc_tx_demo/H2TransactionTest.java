package com.example.jdbc_tx_demo;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.jdbc_tx_demo.dao.H2DAOImpl;

public class H2TransactionTest extends TransactionTest {

    @Autowired
    public void setDao(H2DAOImpl dao) {
        super.setDao(dao);
    }

}
