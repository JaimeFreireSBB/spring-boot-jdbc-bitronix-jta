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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.example.jdbc_tx_demo.dao.TestDAO;

@RunWith(SpringRunner.class)
@SpringBootTest
public abstract class TransactionTest {

    @Autowired
    private ApplicationContext context;

    private TestDAO dao;

    public void setDao(TestDAO dao) {
        this.dao = dao;
    }

    @Before
    public void before() {
        dao.createSchema();
    }

    @After
    public void after() {
        dao.dropSchema();
    }

    @Test
    public void transactionRollbackTest() {

        PlatformTransactionManager transactionManager = context.getBean(PlatformTransactionManager.class);
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                status.setRollbackOnly();

                assertEquals(0, dao.selectAll());
                assertEquals(1, dao.insertOne("value1"));
                assertEquals(1, dao.selectAll());
                assertEquals(1, dao.insertOne("value2"));
                assertEquals(2, dao.selectAll());
            }
        });

        assertEquals("transaction not rollback", 0, dao.selectAll());
    }

    @Test
    public void duplicateInsertRollbackTest() {

        PlatformTransactionManager transactionManager = context.getBean(PlatformTransactionManager.class);
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                assertEquals(0, dao.selectAll());
                assertEquals(1, dao.insertOne("value1"));
                assertEquals(1, dao.selectAll());
                try {
                    assertEquals(1, dao.insertOne("value1"));
                    fail("primary key constraint not working.");
                } catch (DuplicateKeyException e) {
                    status.setRollbackOnly();
                }
            }
        });

        assertEquals("transaction not rollback", 0, dao.selectAll());
    }

    @Test
    public void commitTest() {

        PlatformTransactionManager transactionManager = context.getBean(PlatformTransactionManager.class);
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                assertEquals(0, dao.selectAll());
                assertEquals(1, dao.insertOne("value1"));
                assertEquals(1, dao.insertOne("value2"));
                assertEquals(2, dao.selectAll());
            }
        });

        assertEquals(2, dao.selectAll());
        
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                assertEquals(2, dao.selectAll());
                assertEquals(1, dao.insertOne("value3"));
                assertEquals(1, dao.insertOne("value4"));
                assertEquals(4, dao.selectAll());
            }
        });
        
        assertEquals(4, dao.selectAll());
        
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                status.setRollbackOnly();

                assertEquals(4, dao.selectAll());
                assertEquals(1, dao.insertOne("value5"));
                assertEquals(1, dao.insertOne("value6"));
                assertEquals(6, dao.selectAll());
            }
        });
        
        assertEquals(4, dao.selectAll());
    }
}
