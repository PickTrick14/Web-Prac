package com.dao.test;

import com.db.dao.CompanyDAO;
import com.db.dao.ExperienceDAO;
import com.db.dao.PersonDAO;
import com.db.entity.Company;
import com.db.entity.Experience;
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
class ExperienceDAOTest {

    @Autowired
    private ExperienceDAO experienceDAO;

    @Autowired
    private PersonDAO personDAO;

    @Autowired
    private CompanyDAO companyDAO;

    @Nested
    class FindByIdTests {
        @Test
        void success() {
            Experience result = experienceDAO.findById(1L);
            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("Junior Developer", result.getPosition());
        }

        @Test
        void nothing() {
            Experience result = experienceDAO.findById(999L);
            assertNull(result);
        }

        @Test
        void neg() {
            Experience result = experienceDAO.findById(-1L);
            assertNull(result);
        }

        @Test
        void zero() {
            Experience result = experienceDAO.findById(0L);
            assertNull(result);
        }
    }

    @Nested
    class FindAllTests {
        @Test
        void success() {
            List<Experience> result = experienceDAO.findAll();
            assertNotNull(result);
            assertEquals(7, result.size());
        }

        @Test
        void first() {
            List<Experience> result = experienceDAO.findAll();
            assertNotNull(result);
            assertEquals(7, result.size());
            assertEquals("Junior Developer", result.getFirst().getPosition());
        }

        @Test
        void last() {
            List<Experience> result = experienceDAO.findAll();
            assertNotNull(result);
            assertEquals(7, result.size());
            assertEquals("Marketing Intern", result.getLast().getPosition());
        }
    }

    @Nested
    class FindByPersonTests {
        @Test
        void success() {
            Person person = personDAO.findById(1L);
            List<Experience> result = experienceDAO.findByPerson(person);
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Junior Developer", result.get(0).getPosition());
        }

        @Test
        void empty() {
            Person person = personDAO.findById(11L);
            List<Experience> result = experienceDAO.findByPerson(person);
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void nullPerson() {
            List<Experience> result = experienceDAO.findByPerson(null);
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void notPerson() {
            Person person = new Person();
            person.setId(999L);
            List<Experience> result = experienceDAO.findByPerson(person);
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    class FindByCompanyTests {
        @Test
        void success() {
            Company company = companyDAO.findById(3L);
            List<Experience> result = experienceDAO.findByCompany(company);
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Coordinator", result.getFirst().getPosition());
        }

        @Test
        void empty() {
            Company company = new Company();
            company.setId(999L);
            List<Experience> result = experienceDAO.findByCompany(company);
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void nullCompany() {
            List<Experience> result = experienceDAO.findByCompany(null);
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void newCompany() {
            Company company = new Company();
            company.setId(11L);
            List<Experience> result = experienceDAO.findByCompany(company);
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    class FindByPositionTests {
        @Test
        void success() {
            List<Experience> result = experienceDAO.findByPosition("Developer");
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(1L, result.getFirst().getId());
            assertEquals("Junior Developer", result.getFirst().getPosition());
        }

        @Test
        void empty() {
            List<Experience> result = experienceDAO.findByPosition("Nonexistent");
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void emptyString() {
            List<Experience> result = experienceDAO.findByPosition("");
            assertNotNull(result);
            assertEquals(7, result.size());
        }

        @Test
        void caseInsensitive() {
            List<Experience> result = experienceDAO.findByPosition("developer");
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Junior Developer", result.getFirst().getPosition());
        }
    }
}
