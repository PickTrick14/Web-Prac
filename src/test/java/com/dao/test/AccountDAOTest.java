package com.dao.test;

import com.db.dao.AccountDAO;
import com.db.entity.Account;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AccountDAOTest {

    @Autowired
    private AccountDAO accountDAO;

    @Nested
    class FindByIdTests {
        @Test
        void success() {
            Account result = accountDAO.findById(1L);
            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("company1@gmail.com", result.getEmail());
        }

        @Test
        void nothing() {
            Account result = accountDAO.findById(999L);
            assertNull(result);
        }

        @Test
        void neg() {
            Account result = accountDAO.findById(-1L);
            assertNull(result);
        }

        @Test
        void zero() {
            Account result = accountDAO.findById(0L);
            assertNull(result);
        }
    }

    @Nested
    class FindAllTests {
        @Test
        void success() {
            List<Account> result = accountDAO.findAll();
            assertNotNull(result);
            assertEquals(12, result.size());
        }

        @Test
        void first() {
            List<Account> result = accountDAO.findAll();
            assertNotNull(result);
            assertEquals("company1@gmail.com", result.getFirst().getEmail());
        }

        @Test
        void last() {
            List<Account> result = accountDAO.findAll();
            assertNotNull(result);
            assertEquals("george.miller@gmail.com", result.getLast().getEmail());
        }
    }

    @Nested
    class FindByEmailTests {
        @Test
        void successUser() {
            List<Account> result = accountDAO.findByEmail("diana.brown@gmail.com");
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("diana.brown@gmail.com", result.getFirst().getEmail());
            assertFalse(result.getFirst().isEmployer());
        }

        @Test
        void successCompany() {
            List<Account> result = accountDAO.findByEmail("company4@gmail.com");
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("company4@gmail.com", result.getFirst().getEmail());
            assertTrue(result.getFirst().isEmployer());
        }

        @Test
        void nothing() {
            List<Account> result = accountDAO.findByEmail("nonexistent@email.com");
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void empty() {
            List<Account> result = accountDAO.findByEmail("");
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void Null() {
            List<Account> result = accountDAO.findByEmail(null);
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    class FindEmployerTests {
        @Test
        void success() {
            List<Account> result = accountDAO.findEmployer();
            assertNotNull(result);
            assertEquals(5, result.size());
            result.forEach(account -> assertTrue(account.isEmployer()));
        }

        @Test
        void employerEmail() {
            List<Account> result = accountDAO.findEmployer();
            assertNotNull(result);
            result.forEach(account -> {
                String expectedEmail = String.format("company%d@gmail.com", account.getId());
                assertEquals(expectedEmail, account.getEmail());
            });
        }
    }

    @Nested
    class FindWorkerTests {
        @Test
        void success() {
            List<Account> result = accountDAO.findWorker();
            assertNotNull(result);
            assertEquals(7, result.size());
            result.forEach(account -> assertFalse(account.isEmployer()));
        }

        @Test
        void emails() {
            List<Account> result = accountDAO.findWorker();
            assertNotNull(result);
            assertEquals("alice.johnson@gmail.com", result.get(0).getEmail());
            assertEquals("bob.smith@gmail.com", result.get(1).getEmail());
            assertEquals("charlie.davis@gmail.com", result.get(2).getEmail());
            assertEquals("diana.brown@gmail.com", result.get(3).getEmail());
            assertEquals("edward.wilson@gmail.com", result.get(4).getEmail());
            assertEquals("fiona.clark@gmail.com", result.get(5).getEmail());
            assertEquals("george.miller@gmail.com", result.get(6).getEmail());
        }
    }

    @Nested
    class SaveTests {
        @Test
        void success() {
            Account account = new Account(null, "newuser@email.com", 54262114L, false);
            accountDAO.save(account);
            assertNotNull(account.getId());

            Account saved = accountDAO.findById(account.getId());
            assertNotNull(saved);
            assertEquals("newuser@email.com", saved.getEmail());
            assertFalse(saved.isEmployer());
        }

        @Test
        void duplicate() {
            Account account = new Account(null, "diana.brown@gmail.com", 634532124L, false);
            assertThrows(Exception.class, () -> accountDAO.save(account));
        }

        @Test
        void Null() {
            Account account = new Account(null, null, 463542423L, false);
            assertThrows(Exception.class, () -> accountDAO.save(account));
        }
    }

    @Nested
    class UpdateTests {
        @Test
        void success() {
            Account account = accountDAO.findById(1L);
            account.setEmail("updated@email.com");
            accountDAO.update(account);

            Account updated = accountDAO.findById(1L);
            assertEquals("updated@email.com", updated.getEmail());
        }

        @Test
        void isEmployer() {
            Account account = accountDAO.findById(1L);
            account.setEmployer(true);
            accountDAO.update(account);

            Account updated = accountDAO.findById(1L);
            assertTrue(updated.isEmployer());
        }

        @Test
        void almostDuplicate() {
            Account account = accountDAO.findById(1L);
            account.setEmail("fiona.clark1@gmail.com");
            account.setEmployer(false);
            account.setPassword(6603321L);

            accountDAO.update(account);

            Account newAccount = accountDAO.findById(1L);
            assertEquals("fiona.clark1@gmail.com", newAccount.getEmail());
            assertFalse(newAccount.isEmployer());
            assertEquals(6603321L, newAccount.getPassword());
        }
    }

    @Nested
    class DeleteByIdTests {
        @Test
        void success() {
            accountDAO.deleteById(1L);
            Account result = accountDAO.findById(1L);
            assertNull(result);
        }

        @Test
        void nothing() {
            int beforeSize = accountDAO.findAll().size();
            accountDAO.deleteById(999L);
            List<Account> result = accountDAO.findAll();
            assertEquals(beforeSize, result.size());
        }

        @Test
        void neg() {
            int beforeSize = accountDAO.findAll().size();
            accountDAO.deleteById(-1L);
            List<Account> result = accountDAO.findAll();
            assertEquals(beforeSize, result.size());
        }
    }
}