package com.example.jdbc_tx_demo;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.example.jdbc_tx_demo.dao.SqlServerDAOImpl;
import com.example.jdbc_tx_demo.dao.H2DAOImpl;
import com.example.jdbc_tx_demo.dao.OracleDAOImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CrossResourceTest {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private OracleDAOImpl oracleDao;
    
    @Autowired
    private SqlServerDAOImpl mssqlDao;

    @Autowired
    private H2DAOImpl h2Dao;

    @Before
    public void before() {
        mssqlDao.createSchema();
        h2Dao.createSchema();
    }

    @After
    public void after() {
        mssqlDao.dropSchema();
        h2Dao.dropSchema();
    }

    @Test
    public void enlistTwoNonXAResources() {

        PlatformTransactionManager transactionManager = context.getBean(PlatformTransactionManager.class);
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                
                // enlist one non-XA resource
                assertEquals(0, mssqlDao.selectAll());
                
                try {
                    // enlist second non-XA resource
                    oracleDao.selectAll();
                    
                    fail("allowing two non-XA resource in one transaction.");
                } catch (UncategorizedSQLException e) {
                    // cannot have two non-XA resources.
                }
            }
        });
    }

    @Test
    public void crossDbRollbackTest1() {

        PlatformTransactionManager transactionManager = context.getBean(PlatformTransactionManager.class);
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                
                assertEquals(0, mssqlDao.selectAll());
                assertEquals(0, h2Dao.selectAll());

                assertEquals(1, mssqlDao.insertOne("value1"));
                assertEquals(1, mssqlDao.selectAll());
                
                assertEquals(1, h2Dao.insertOne("value1"));
                assertEquals(1, h2Dao.selectAll());
            }
        });
        
        assertEquals(1, mssqlDao.selectAll());
        assertEquals(1, h2Dao.selectAll());

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                
                assertEquals(1, mssqlDao.selectAll());
                assertEquals(1, h2Dao.selectAll());

                assertEquals(1, mssqlDao.insertOne("value2"));
                assertEquals(2, mssqlDao.selectAll());

                try {
                    assertEquals(1, h2Dao.insertOne("value1"));
                    fail("primary key constraint not working.");
                } catch (DuplicateKeyException e) {
                    status.setRollbackOnly();
                }
            }
        });
        
        assertEquals(1, mssqlDao.selectAll());
        assertEquals(1, h2Dao.selectAll());
    }

    @Test
    public void crossDbRollbackTest2() {

        PlatformTransactionManager transactionManager = context.getBean(PlatformTransactionManager.class);
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                
                assertEquals(0, mssqlDao.selectAll());
                assertEquals(0, h2Dao.selectAll());

                assertEquals(1, mssqlDao.insertOne("value1"));
                assertEquals(1, mssqlDao.selectAll());
                
                assertEquals(1, h2Dao.insertOne("value1"));
                assertEquals(1, h2Dao.selectAll());
            }
        });
        
        assertEquals(1, mssqlDao.selectAll());
        assertEquals(1, h2Dao.selectAll());

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                
                assertEquals(1, mssqlDao.selectAll());
                assertEquals(1, h2Dao.selectAll());

                assertEquals(1, h2Dao.insertOne("value2"));
                assertEquals(2, h2Dao.selectAll());

                try {
                    assertEquals(1, mssqlDao.insertOne("value1"));
                    fail("primary key constraint not working.");
                } catch (DuplicateKeyException e) {
                    status.setRollbackOnly();
                }
            }
        });
        
        assertEquals(1, mssqlDao.selectAll());
        assertEquals(1, h2Dao.selectAll());
    }
}
