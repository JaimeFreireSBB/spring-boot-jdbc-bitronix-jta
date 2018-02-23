package com.example.jdbc_tx_demo.config;

import java.util.Properties;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import bitronix.tm.resource.jdbc.PoolingDataSource;

@Configuration
public class OracleDataSourceConfig {

    @Value("${datasource.oracle.url}")
    private String url;

    @Value("${datasource.oracle.username}")
    private String username;

    @Value("${datasource.oracle.password}")
    private String password;

    private int maxPoolSize = 5;

    @Bean(name = "oracleDataSource", destroyMethod = "close")
    public DataSource getDataSource() {
         PoolingDataSource poolingDataSource = new PoolingDataSource();
         poolingDataSource.setClassName(bitronix.tm.resource.jdbc.lrc.LrcXADataSource.class.getName());
         poolingDataSource.setUniqueName("oracleDataSource");
         Properties props = new Properties();
         props.put("driverClassName", oracle.jdbc.OracleDriver.class.getName());
         props.put("url", url);
         props.put("user", username);
         props.put("password", password);
         poolingDataSource.setDriverProperties(props);
         poolingDataSource.setAllowLocalTransactions(true);
         poolingDataSource.setTestQuery("SELECT 1 FROM DUAL");
         poolingDataSource.setMaxPoolSize(this.maxPoolSize);
         poolingDataSource.init();
         return poolingDataSource;
    }

//    @Bean(name = "oracleXADataSource", destroyMethod = "close")
    public DataSource getXADataSource() {
         PoolingDataSource poolingDataSource = new PoolingDataSource();
         poolingDataSource.setClassName(oracle.jdbc.xa.client.OracleXADataSource.class.getName());
         poolingDataSource.setUniqueName("oracleXADataSource");
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
