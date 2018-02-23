package com.example.jdbc_tx_demo.config;

import bitronix.tm.TransactionManagerServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

@Configuration
public class BitronixJtaConfiguration {

    @Bean
    public bitronix.tm.Configuration transactionManagerServices() {
        return TransactionManagerServices.getConfiguration();
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
