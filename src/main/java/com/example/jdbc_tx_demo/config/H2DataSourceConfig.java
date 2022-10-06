package com.example.jdbc_tx_demo.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import bitronix.tm.resource.jdbc.PoolingDataSource;

@Configuration
public class H2DataSourceConfig {

    @Value("${datasource.h2.url}")
    private String url;
    
    @Value("${datasource.h2.username}")
    private String username;
    
    @Value("${datasource.h2.password}")
    private String password;

    private int maxPoolSize = 5;

    @Primary
    @Bean(name = "h2DataSource", destroyMethod = "close")
    public DataSource getDataSource() {
        PoolingDataSource poolingDataSource = new PoolingDataSource();
        poolingDataSource.setClassName(JdbcDataSource.class.getName());
        poolingDataSource.setUniqueName("h2DataSource");

        Properties props = new Properties();
        props.put("URL", url);
        props.put("user", username);
        props.put("password", password);
        poolingDataSource.setDriverProperties(props);
        
        poolingDataSource.setAllowLocalTransactions(true);
        
        poolingDataSource.setMaxPoolSize(this.maxPoolSize);
        poolingDataSource.init();

        return poolingDataSource;
    }

}
