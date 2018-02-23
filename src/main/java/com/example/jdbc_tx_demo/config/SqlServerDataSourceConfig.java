package com.example.jdbc_tx_demo.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import bitronix.tm.resource.jdbc.PoolingDataSource;

@Configuration
public class SqlServerDataSourceConfig {

    @Value("${datasource.sqlserver.url}")
    private String url;

    private int maxPoolSize = 5;

    @Bean(name = "sqlServerDataSource", destroyMethod = "close")
    public DataSource getDataSource() {
        PoolingDataSource poolingDataSource = new PoolingDataSource();
        poolingDataSource.setClassName(bitronix.tm.resource.jdbc.lrc.LrcXADataSource.class.getName());
        poolingDataSource.setUniqueName("sqlServerDataSource");
        Properties props = new Properties();
        props.put("driverClassName", com.microsoft.sqlserver.jdbc.SQLServerDriver.class.getName());
        props.put("url", url);
        poolingDataSource.setDriverProperties(props);
        poolingDataSource.setAllowLocalTransactions(true);
        poolingDataSource.setMaxPoolSize(this.maxPoolSize);
        poolingDataSource.init();
        return poolingDataSource;
    }

//    @Bean(name = "sqlServerXADataSource", destroyMethod = "close")
//    public DataSource getXADataSource() {
//        PoolingDataSource poolingDataSource = new PoolingDataSource();
//        poolingDataSource.setClassName(com.microsoft.sqlserver.jdbc.SQLServerXADataSource.class.getName());
//        poolingDataSource.setUniqueName("sqlServerXADataSource");
//        Properties props = new Properties();
//        props.put("URL", url);
//        poolingDataSource.setDriverProperties(props);
//        poolingDataSource.setAllowLocalTransactions(true);
//        poolingDataSource.setMaxPoolSize(this.maxPoolSize);
//        poolingDataSource.init();
//        return poolingDataSource;
//    }
}
