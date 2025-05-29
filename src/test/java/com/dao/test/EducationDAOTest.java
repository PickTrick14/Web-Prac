package com.dao.test;

import com.db.dao.EducationDAO;
import com.db.dao.PersonDAO;
import com.db.entity.Education;
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
class EducationDAOTest {

    @Autowired
    private EducationDAO educationDAO;

    @Autowired
    private PersonDAO personDAO;

    @Nested
    class FindByIdTests {
        @Test
        void success() {
            Education result = educationDAO.findById(1L);
            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("MSU", result.getInstitution());
            assertEquals("Computer Science", result.getSpecialization());
            assertEquals(2015, result.getEndYear());
        }

        @Test
        void nothing() {
            Education result = educationDAO.findById(999L);
            assertNull(result);
        }

        @Test
        void neg() {
            Education result = educationDAO.findById(-1L);
            assertNull(result);
        }

        @Test
        void zero() {
            Education result = educationDAO.findById(0L);
            assertNull(result);
        }
    }

    @Nested
    class FindAllTests {
        @Test
        void success() {
            List<Education> result = educationDAO.findAll();
            assertNotNull(result);
            assertEquals(7, result.size());
            assertEquals(1L, result.getFirst().getId());
            assertEquals("MSU", result.getFirst().getInstitution());
            assertEquals("Computer Science", result.get(0).getSpecialization());
            assertEquals(2015, result.get(0).getEndYear());

            assertEquals(2L, result.get(1).getId());
            assertEquals("Stanford University", result.get(1).getInstitution());
            assertEquals("Design", result.get(1).getSpecialization());
            assertEquals(2012, result.get(1).getEndYear());

            assertEquals(3L, result.get(2).getId());
            assertEquals("MIT", result.get(2).getInstitution());
            assertEquals("Engineering", result.get(2).getSpecialization());
            assertEquals(2018, result.get(2).getEndYear());

            assertEquals(4L, result.get(3).getId());
            assertEquals("Ranhigs", result.get(3).getInstitution());
            assertEquals("Business Administration", result.get(3).getSpecialization());
            assertEquals(2010, result.get(3).getEndYear());

            assertEquals(5L, result.get(4).getId());
            assertEquals("HSE", result.get(4).getInstitution());
            assertEquals("Economics", result.get(4).getSpecialization());
            assertEquals(2008, result.get(4).getEndYear());

            assertEquals(6L, result.get(5).getId());
            assertEquals("HSE", result.get(5).getInstitution());
            assertEquals("Marketing", result.get(5).getSpecialization());
            assertEquals(2016, result.get(5).getEndYear());

            assertEquals(7L, result.get(6).getId());
            assertEquals("MSU", result.get(6).getInstitution());
            assertEquals("Psychology", result.get(6).getSpecialization());
            assertEquals(2005, result.get(6).getEndYear());
        }

        @Test
        void first() {
            List<Education> result = educationDAO.findAll();
            assertNotNull(result);
            assertEquals(1L, result.getFirst().getId());
            assertEquals("MSU", result.getFirst().getInstitution());
            assertEquals("Computer Science", result.getFirst().getSpecialization());
            assertEquals(2015, result.getFirst().getEndYear());
        }

        @Test
        void last() {
            List<Education> result = educationDAO.findAll();
            assertNotNull(result);
            assertEquals(7L, result.get(6).getId());
            assertEquals("MSU", result.get(6).getInstitution());
            assertEquals("Psychology", result.get(6).getSpecialization());
            assertEquals(2005, result.get(6).getEndYear());
        }
    }

    @Nested
    class FindByPersonTests {
        @Test
        void success() {
            Person person = personDAO.findById(1L);
            List<Education> result = educationDAO.findByPerson(person);
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(1L, result.getFirst().getId());
            assertEquals("MSU", result.getFirst().getInstitution());
            assertEquals("Computer Science", result.getFirst().getSpecialization());
            assertEquals(2015, result.getFirst().getEndYear());
        }

        @Test
        void empty() {
            Person person = new Person();
            person.setId(999L);
            List<Education> result = educationDAO.findByPerson(person);
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void Null() {
            List<Education> result = educationDAO.findByPerson(null);
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    class FindByInstitutionTests {
        @Test
        void success() {
            List<Education> result = educationDAO.findByInstitution("MSU");
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("MSU", result.getFirst().getInstitution());
        }

        @Test
        void empty() {
            List<Education> result = educationDAO.findByInstitution("Nonexistent");
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void emptyString() {
            List<Education> result = educationDAO.findByInstitution("");
            assertNotNull(result);
            assertEquals(7, result.size());
        }

        @Test
        void caseInsensitive() {
            List<Education> result = educationDAO.findByInstitution("msu");
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("MSU", result.getFirst().getInstitution());
        }
    }

    @Nested
    class FindBySpecializationTests {
        @Test
        void success() {
            List<Education> result = educationDAO.findBySpecialization("Computer Science");
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(1L, result.getFirst().getId());
            assertEquals("MSU", result.getFirst().getInstitution());
            assertEquals("Computer Science", result.getFirst().getSpecialization());
            assertEquals(2015, result.getFirst().getEndYear());
        }

        @Test
        void empty() {
            List<Education> result = educationDAO.findBySpecialization("Nonexistent");
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void emptyString() {
            List<Education> result = educationDAO.findBySpecialization("");
            assertNotNull(result);
            assertEquals(7, result.size());
        }

        @Test
        void caseInsensitive() {
            List<Education> result = educationDAO.findBySpecialization("computer science");
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Computer Science", result.getFirst().getSpecialization());
        }
    }
}
