package com.dao.test;

import com.db.dao.PersonDAO;
import com.db.dao.ResponseDAO;
import com.db.dao.VacancyDAO;
import com.db.entity.Response;
import com.db.entity.Person;
import com.db.entity.Vacancy;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ResponseDAOTest {

    @Autowired
    private ResponseDAO ResponseDAO;

    @Autowired
    private PersonDAO personDAO;

    @Autowired
    private VacancyDAO vacancyDAO;

    @Nested
    class FindByIdTests {
        @Test
        void success() {
            Response result = ResponseDAO.findById(1L);
            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("Pending", result.getStatus());
            assertEquals(1L, result.getPerson().getId());
            assertEquals(1L, result.getVacancy().getId());
            assertEquals(LocalDate.parse("2024-01-10"), result.getResponseDate());
        }

        @Test
        void nothing() {
            Response result = ResponseDAO.findById(999L);
            assertNull(result);
        }

        @Test
        void neg() {
            Response result = ResponseDAO.findById(-1L);
            assertNull(result);
        }

        @Test
        void zero() {
            Response result = ResponseDAO.findById(0L);
            assertNull(result);
        }
    }

    @Nested
    class FindAllTests {
        @Test
        void success() {
            List<Response> result = ResponseDAO.findAll();
            assertNotNull(result);
            assertEquals(6, result.size());
        }

        @Test
        void first() {
            List<Response> result = ResponseDAO.findAll();
            assertNotNull(result);
            assertEquals(6, result.size());
            Response first = result.getFirst();
            assertEquals(1L, first.getId());
            assertEquals("Pending", first.getStatus());
            assertEquals(1L, first.getPerson().getId());
            assertEquals(1L, first.getVacancy().getId());
            assertEquals(LocalDate.parse("2024-01-10"), first.getResponseDate());
        }

        @Test
        void last() {
            List<Response> result = ResponseDAO.findAll();
            assertNotNull(result);
            assertEquals(6, result.size());
            Response last = result.getLast();
            assertEquals(6L, last.getId());
            assertEquals("Reviewed", last.getStatus());
            assertEquals(6L, last.getPerson().getId());
            assertEquals(6L, last.getVacancy().getId());
            assertEquals(LocalDate.parse("2024-01-15"), last.getResponseDate());
        }
    }

    @Nested
    class FindByPersonTests {
        @Test
        void success() {
            Person person = personDAO.findById(1L);
            List<Response> result = ResponseDAO.findByPerson(person);
            assertNotNull(result);
            Response resp = result.getFirst();
            assertEquals(2, result.size());
            assertEquals(1L, resp.getId());
            assertEquals("Pending", resp.getStatus());
            assertEquals(1L, resp.getPerson().getId());
            assertEquals(1L, resp.getVacancy().getId());
            assertEquals(LocalDate.parse("2024-01-10"), resp.getResponseDate());
        }

        @Test
        void empty() {
            Person person = personDAO.findById(2L);
            List<Response> result = ResponseDAO.findByPerson(person);
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void nullPerson() {
            List<Response> result = ResponseDAO.findByPerson(null);
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void unknownPerson() {
            Person person = new Person();
            person.setId(999L);
            List<Response> result = ResponseDAO.findByPerson(person);
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    class FindByVacancyTests {
        @Test
        void success() {
            Vacancy vacancy = vacancyDAO.findById(1L);
            List<Response> result = ResponseDAO.findByVacancy(vacancy);
            assertNotNull(result);
            Response resp = result.getFirst();
            assertEquals(1, result.size());
            assertEquals(1L, resp.getId());
            assertEquals("Pending", resp.getStatus());
            assertEquals(1L, resp.getPerson().getId());
            assertEquals(1L, resp.getVacancy().getId());
            assertEquals(LocalDate.parse("2024-01-10"), resp.getResponseDate());
        }

        @Test
        void unknownVacancy() {
            Vacancy vacancy = new Vacancy();
            vacancy.setId(999L); // Nonexistent vacancy
            List<Response> result = ResponseDAO.findByVacancy(vacancy);
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void nullVacancy() {
            List<Response> result = ResponseDAO.findByVacancy(null);
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    class FindByStatusTests {
        @Test
        void success() {
            List<Response> result = ResponseDAO.findByStatus("Pending");
            assertNotNull(result);
            assertEquals(3, result.size());
            result.forEach(response -> assertEquals("Pending", response.getStatus()));
        }

        @Test
        void emptyStatus() {
            List<Response> result = ResponseDAO.findByStatus("");
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void nullStatus() {
            List<Response> result = ResponseDAO.findByStatus(null);
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }
}