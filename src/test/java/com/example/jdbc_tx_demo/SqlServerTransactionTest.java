package com.example.jdbc_tx_demo;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.jdbc_tx_demo.dao.SqlServerDAOImpl;

public class SqlServerTransactionTest extends TransactionTest {
    
    @Autowired
    public void setDao(SqlServerDAOImpl dao) {
        super.setDao(dao);
    }

}
