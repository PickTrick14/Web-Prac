package com.dao.test;

import com.db.dao.ResumeDAO;
import com.db.dao.PersonDAO;
import com.db.entity.Resume;
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
class ResumeDAOTest {

    @Autowired
    private ResumeDAO resumeDAO;

    @Autowired
    private PersonDAO personDAO;

    @Nested
    class FindByIdTests {
        @Test
        void success() {
            Resume result = resumeDAO.findById(1L);
            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("Software Engineer", result.getWantedPosition());
        }

        @Test
        void nothing() {
            Resume result = resumeDAO.findById(999L);
            assertNull(result);
        }
    }

    @Nested
    class FindAllTests {
        @Test
        void success() {
            List<Resume> result = resumeDAO.findAll();
            assertNotNull(result);
            assertEquals(7, result.size());
        }
    }

    @Nested
    class FindByPersonTests {
        @Test
        void success() {
            Person person = personDAO.findById(2L);
            List<Resume> result = resumeDAO.findByPerson(person);
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Senior Graphic Designer", result.getFirst().getWantedPosition());
        }

        @Test
        void empty() {
            Person person = new Person();
            person.setId(999L);
            List<Resume> result = resumeDAO.findByPerson(person);
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    class FindByWantedPositionTests {
        @Test
        void success() {
            List<Resume> result = resumeDAO.findByWantedPosition("Senior");
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Senior Graphic Designer", result.getFirst().getWantedPosition());
        }

        @Test
        void empty() {
            List<Resume> result = resumeDAO.findByWantedPosition("Nonexistent");
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    class FindByWantedSalaryTests {
        @Test
        void success() {
            List<Resume> result = resumeDAO.findByWantedSalaryRange(70000, 90000);
            assertNotNull(result);
            assertEquals(4, result.size());
        }

        @Test
        void empty() {
            List<Resume> result = resumeDAO.findByWantedSalaryRange(100000, 200000);
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    class FindBySkillTests {
        @Test
        void success() {
            List<Resume> result = resumeDAO.findBySkills("Java");
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Software Engineer", result.getFirst().getWantedPosition());
        }

        @Test
        void empty() {
            List<Resume> result = resumeDAO.findBySkills("NonexistentSkill");
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }
}
