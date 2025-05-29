package com.dao.test;

import com.db.dao.AccountDAO;
import com.db.dao.PersonDAO;
import com.db.entity.Account;
import com.db.entity.City;
import com.db.entity.Person;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PersonDAOTest {

    @Autowired
    private PersonDAO personDAO;

    @Autowired
    private AccountDAO accountDAO;

    @Nested
    class FindByIdTests {
        @Test
        void success() {
            Person result = personDAO.findById(2L);
            assertNotNull(result);
            assertEquals(2L, result.getId());
            assertEquals("Bob Smith", result.getName());
        }

        @Test
        void nothing() {
            Person result = personDAO.findById(999L);
            assertNull(result);
        }

        @Test
        void neg() {
            Person result = personDAO.findById(-1L);
            assertNull(result);
        }

        @Test
        void zero() {
            Person result = personDAO.findById(0L);
            assertNull(result);
        }
    }

    @Nested
    class FindAllTests {
        @Test
        void success() {
            List<Person> result = personDAO.findAll();
            assertNotNull(result);
            assertEquals(7, result.size());

            assertEquals("Alice Johnson", result.get(0).getName());
            assertEquals("Bob Smith", result.get(1).getName());
            assertEquals("Charlie Davis", result.get(2).getName());
            assertEquals("Diana Brown", result.get(3).getName());
            assertEquals("Edward Wilson", result.get(4).getName());
            assertEquals("Fiona Clark", result.get(5).getName());
            assertEquals("George Miller", result.get(6).getName());
        }

        @Test
        void first() {
            List<Person> result = personDAO.findAll();
            assertNotNull(result);
            assertEquals("Alice Johnson", result.getFirst().getName());
        }

        @Test
        void last() {
            List<Person> result = personDAO.findAll();
            assertNotNull(result);
            assertEquals("George Miller", result.get(6).getName());
        }
    }

    @Nested
    class FindByNameTests {
        @Test
        void success() {
            List<Person> result = personDAO.findByName("John");
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Alice Johnson", result.getFirst().getName());
        }

        @Test
        void nothing() {
            List<Person> result = personDAO.findByName("Nonexistent");
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void emptyString() {
            List<Person> result = personDAO.findByName("");
            assertNotNull(result);
            assertEquals(7, result.size());
        }

        @Test
        void caseInsensitive() {
            List<Person> result = personDAO.findByName("john");
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Alice Johnson", result.getFirst().getName());
        }
    }

    @Nested
    class FindByAccountTests {
        @Test
        void success() {
            Account account = accountDAO.findById(1L);
            List<Person> result = personDAO.findByAccount(account);
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Edward Wilson", result.getFirst().getName());
        }

        @Test
        void nothing() {
            Account account = accountDAO.findById(11L);
            List<Person> result = personDAO.findByAccount(account);
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void nullAccount() {
            List<Person> result = personDAO.findByAccount(null);
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void newAccount() {
            Account newAccount = new Account(null, "new@email.com", 123324214214L, false);
            accountDAO.save(newAccount);
            List<Person> result = personDAO.findByAccount(newAccount);
            assertNotNull(result);
            assertTrue(result.isEmpty());
            accountDAO.delete(newAccount);
        }
    }

    @Nested
    class FindIsSearchingTests {
        @Test
        void success() {
            List<Person> result = personDAO.findIsSearching(true);
            assertNotNull(result);
            assertEquals(5, result.size());
            result.forEach(person -> assertTrue(person.isSearching()));
        }

        @Test
        void successNot() {
            List<Person> result = personDAO.findIsSearching(false);
            assertNotNull(result);
            assertEquals(2, result.size());
            result.forEach(person -> assertFalse(person.isSearching()));
        }
    }

    @Nested
    class FindByCityTests {
        @Test
        void success() {
            City city = new City();
            city.setId(1L);
            List<Person> result = personDAO.findByCity(city);
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(1L, result.getFirst().getCity().getId());
            assertEquals("Moscow", result.getFirst().getCity().getName());
        }

        @Test
        void empty() {
            City city = new City();
            city.setId(999L);
            List<Person> result = personDAO.findByCity(city);
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void nullCity() {
            List<Person> result = personDAO.findByCity(null);
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    class FindByAgeRangeTests {
        @Test
        void success() {
            List<Person> result = personDAO.findByAgeRange(20, 30);
            assertNotNull(result);
            assertEquals(4, result.size());
        }

        @Test
        void empty() {
            List<Person> result = personDAO.findByAgeRange(41, 50);
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void neg() {
            List<Person> result = personDAO.findByAgeRange(-10, -5);
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void inverted() {
            List<Person> result = personDAO.findByAgeRange(30, 20);
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    class FindByGreaterAgeTests {
        @Test
        void success() {
            List<Person> result = personDAO.findByGreaterAge(25);
            assertNotNull(result);
            assertEquals(7, result.size());
        }

        @Test
        void neg() {
            List<Person> result = personDAO.findByGreaterAge(-1);
            assertNotNull(result);
            assertEquals(7, result.size());
        }
    }

    @Nested
    class FindByLessAgeTests {
        @Test
        void success() {
            List<Person> result = personDAO.findByLessAge(30);
            assertNotNull(result);
            assertEquals(4, result.size());
        }

        @Test
        void findByLessAge_NegativeAge() {
            List<Person> result = personDAO.findByLessAge(-1);
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }
}