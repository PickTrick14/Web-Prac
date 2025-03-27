package com.db.dao;

import com.db.entity.Account;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource("classpath:application.properties")
public class AccountDAOTest {

    @Autowired
    private AccountDAO accountDAO;

    @Test
    public void test() {
        Account testAccount = new Account();
        testAccount.setId(15L);
        testAccount.setEmail("fiona.clark@gmail.com");
        testAccount.setPassword(123456L);
        testAccount.setEmployer(false);
        accountDAO.save(testAccount);

        List<Account> res = accountDAO.findAll();
        System.out.println(res.size());
        for (Account re : res) {
            System.out.println(re);
        }
    }
}
