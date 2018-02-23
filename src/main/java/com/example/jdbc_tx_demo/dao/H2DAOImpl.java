package com.example.jdbc_tx_demo.dao;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class H2DAOImpl extends TestDAO {

    @Autowired
    private ApplicationContext context;

    @PostConstruct
    public void init() {
        DataSource dataSource = context.getBean("h2DataSource", DataSource.class);
        setJdbcTemplate(new JdbcTemplate(dataSource));
    }
}
