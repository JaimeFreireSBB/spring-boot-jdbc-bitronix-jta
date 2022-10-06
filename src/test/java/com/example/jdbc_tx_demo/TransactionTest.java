package com.example.jdbc_tx_demo;

import com.example.jdbc_tx_demo.dao.TestDAO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public abstract class TransactionTest {

    @Autowired
    private ApplicationContext context;

    private static TestDAO dao;

    public void setDao(TestDAO dao) {
        TransactionTest.dao = dao;
    }

    @BeforeEach
    public void before() {
        dao.createSchema();
    }

    @AfterEach
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

        assertEquals(0, dao.selectAll(), "transaction not rollback");
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

        assertEquals(0, dao.selectAll(), "transaction not rollback");
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
