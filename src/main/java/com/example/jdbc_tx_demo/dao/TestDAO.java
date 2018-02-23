package com.example.jdbc_tx_demo.dao;

import java.sql.ResultSet;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class TestDAO {

    private JdbcTemplate jdbcTemplate;
    
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createSchema() {
        log.info("create test table on {}", jdbcTemplate.getDataSource());
        jdbcTemplate.update("CREATE TABLE JDBC_TEST ( NAME VARCHAR(50) PRIMARY KEY )");
    }

    public void dropSchema() {
        log.info("drop test table on {}", jdbcTemplate.getDataSource());
        jdbcTemplate.update("DROP TABLE JDBC_TEST");
    }

    /*
     * 1. select first row first column as string / int / float ...
     *    throw exception if not one row,
     *    return null if no row, throw exception if more than one row.
     * 2. select first row only and return bean
     *    throw exception if not one row,
     *    return null if no row, throw exception if more than one row.
     * 3. return list of bean
     * 
     * return bean or map
     * when return bean
     * 1. all bean properties should match
     * 2. allow some properties missing
     * 3. allow extra return
     * */

    public void cleanAll() {
        log.info("clean all on {}", jdbcTemplate.getDataSource());
        jdbcTemplate.update("DELETE FROM JDBC_TEST");
    }

    public int selectAll() {
        log.info("select all on {}", jdbcTemplate.getDataSource());
        List<Integer> list = jdbcTemplate.query("SELECT COUNT(*) FROM JDBC_TEST", (ResultSet rs, int rowNum) -> {
            return rs.getInt(1);
        });
        if (list.size() == 1) {
            return list.get(0).intValue();
        }
        throw new RuntimeException("select result incorrect");
    }

    @Transactional
    public int insertOneWithTransaction(String value) {
        log.info("insert one row in transaction on {}", jdbcTemplate.getDataSource());
        return jdbcTemplate.update("INSERT INTO JDBC_TEST VALUES ( ? )", value);
    }

    public int insertOne(String value) {
        log.info("insert one row on {}", jdbcTemplate.getDataSource());
        return jdbcTemplate.update("INSERT INTO JDBC_TEST VALUES ( ? )", value);
    }
}
