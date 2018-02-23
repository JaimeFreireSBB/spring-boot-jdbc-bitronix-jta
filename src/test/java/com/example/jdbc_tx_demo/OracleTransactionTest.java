package com.example.jdbc_tx_demo;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.jdbc_tx_demo.dao.OracleDAOImpl;

public class OracleTransactionTest extends TransactionTest {
    
    @Autowired
    public void setDao(OracleDAOImpl dao) {
        super.setDao(dao);
    }

}
