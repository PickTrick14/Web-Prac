package com.dao.test;

import com.db.dao.AccountDAO;
import com.db.dao.CompanyDAO;
import com.db.entity.Company;
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
class CompanyDAOTest {

    @Autowired
    private CompanyDAO companyDAO;

    @Autowired
    private AccountDAO accountDAO;

    @Nested
    class FindByIdTests {
        @Test
        void success() {
            Company result = companyDAO.findById(1L);
            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("Tech Solutions Inc.", result.getName());
        }

        @Test
        void nothing() {
            Company result = companyDAO.findById(999L);
            assertNull(result);
        }

        @Test
        void neg() {
            Company result = companyDAO.findById(-1L);
            assertNull(result);
        }

        @Test
        void zero() {
            Company result = companyDAO.findById(0L);
            assertNull(result);
        }
    }

    @Nested
    class FindAllTests {
        @Test
        void success() {
            List<Company> result = companyDAO.findAll();
            assertNotNull(result);
            assertEquals(7, result.size());
            assertEquals("Tech Solutions Inc.", result.get(0).getName());
            assertEquals("Sberbank", result.get(1).getName());
            assertEquals("Yandex", result.get(2).getName());
            assertEquals("Positive Technologies", result.get(3).getName());
            assertEquals("OZON", result.get(4).getName());
            assertEquals("Gazprom", result.get(5).getName());
            assertEquals("X5 Group", result.get(6).getName());

        }

        @Test
        void first() {
            List<Company> result = companyDAO.findAll();
            assertNotNull(result);
            assertEquals("Tech Solutions Inc.", result.getFirst().getName());
        }

        @Test
        void last() {
            List<Company> result = companyDAO.findAll();
            assertNotNull(result);
            assertEquals(7, result.size());
            assertEquals("X5 Group", result.get(6).getName());
        }
    }

    @Nested
    class FindByNameTests {
        @Test
        void success() {
            List<Company> result = companyDAO.findByName("Tech");
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("Tech Solutions Inc.", result.getFirst().getName());
            assertEquals("Positive Technologies", result.get(1).getName());
        }

        @Test
        void empty() {
            List<Company> result = companyDAO.findByName("Nonexistent");
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void emptyString() {
            List<Company> result = companyDAO.findByName("");
            assertNotNull(result);
            assertEquals(7, result.size());
        }

        @Test
        void caseInsensitive() {
            List<Company> result = companyDAO.findByName("tech");
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("Tech Solutions Inc.", result.getFirst().getName());
            assertEquals("Positive Technologies", result.get(1).getName());
        }
    }

    @Nested
    class FindByAccountTests {
        @Test
        void success() {
            Account account = accountDAO.findById(3L);
            List<Company> result = companyDAO.findByAccount(account);
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Yandex", result.getFirst().getName());
        }

        @Test
        void notEmployer() {
            Account account = accountDAO.findById(6L);
            List<Company> result = companyDAO.findByAccount(account);
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void nothing() {
            Account account = new Account();
            account.setId(999L);
            List<Company> result = companyDAO.findByAccount(account);
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void nullAccount() {
            List<Company> result = companyDAO.findByAccount(null);
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }
}
