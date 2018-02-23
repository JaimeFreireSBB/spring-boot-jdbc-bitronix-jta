package com.example.jdbc_tx_demo.config;

import bitronix.tm.TransactionManagerServices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

@Configuration
public class BitronixJtaConfiguration {

    private static final Logger log = LoggerFactory.getLogger(BitronixJtaConfiguration.class);

    @Value("${bitronix.tm.serverId}")
    private String serverId;

    @Value("${bitronix.tm.journal.disk.logPart1Filename:}")
    private String logPart1Filename;

    @Value("${bitronix.tm.journal.disk.logPart2Filename:}")
    private String logPart2Filename;

    @Bean
    public bitronix.tm.Configuration transactionManagerServices() {
        bitronix.tm.Configuration configuration = TransactionManagerServices.getConfiguration();
        configuration.setServerId(serverId);
        if ("".equals(logPart1Filename) && "".equals(logPart2Filename)) {
            configuration.setJournal(null);
            log.info("Disable journal for testing.");
        } else {
            configuration.setLogPart1Filename(logPart1Filename);
            configuration.setLogPart2Filename(logPart2Filename);
        }
        return configuration;
    }

    @Bean
    public TransactionManager transactionManager() {
        return TransactionManagerServices.getTransactionManager();
    }

    @Bean
    public UserTransaction userTransaction() {
        return TransactionManagerServices.getTransactionManager();
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager() throws Throwable {
        UserTransaction userTransaction = userTransaction();
        TransactionManager transactionManager = transactionManager();
        return new JtaTransactionManager(userTransaction, transactionManager);
    }
}
