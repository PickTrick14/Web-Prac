package com.dao.test;

import com.db.dao.CompanyDAO;
import com.db.dao.VacancyDAO;
import com.db.entity.Company;
import com.db.entity.Vacancy;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class VacancyDAOTest {

    @Autowired
    private VacancyDAO vacancyDAO;

    @Autowired
    private CompanyDAO companyDAO;

    @Nested
    class FindByIdTests {
        @Test
        void success() {
            Vacancy result = vacancyDAO.findById(1L);
            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("Software Engineer", result.getPosition());
            assertEquals(90000, result.getSalary());
            assertEquals("Experience with Java, Python", result.getRequirements());
        }

        @Test
        void nothing() {
            Vacancy result = vacancyDAO.findById(999L);
            assertNull(result);
        }

        @Test
        void neg() {
            Vacancy result = vacancyDAO.findById(-1L);
            assertNull(result);
        }

        @Test
        void zero() {
            Vacancy result = vacancyDAO.findById(0L);
            assertNull(result);
        }
    }

    @Nested
    class FindAllTests {
        @Test
        void success() {
            List<Vacancy> result = vacancyDAO.findAll();
            assertNotNull(result);
            assertEquals(6, result.size());

            assertEquals(1L, result.getFirst().getId());
            assertEquals("Software Engineer", result.getFirst().getPosition());
            assertEquals(90000, result.getFirst().getSalary());
            assertEquals("Experience with Java, Python", result.getFirst().getRequirements());

            assertEquals(2L, result.get(1).getId());
            assertEquals("Graphic Designer", result.get(1).getPosition());
            assertEquals(50000, result.get(1).getSalary());
            assertEquals("Adobe Creative Suite experience", result.get(1).getRequirements());

            assertEquals(3L, result.get(2).getId());
            assertEquals("Project Manager", result.get(2).getPosition());
            assertEquals(80000, result.get(2).getSalary());
            assertEquals("PMP certification required", result.get(2).getRequirements());

            assertEquals(4L, result.get(3).getId());
            assertEquals("Data Analyst", result.get(3).getPosition());
            assertEquals(70000, result.get(3).getSalary());
            assertEquals("SQL and data visualization skills", result.get(3).getRequirements());

            assertEquals(5L, result.get(4).getId());
            assertEquals("Sales Manager", result.get(4).getPosition());
            assertEquals(60000, result.get(4).getSalary());
            assertEquals("Proven sales record", result.get(4).getRequirements());

            assertEquals(6L, result.get(5).getId());
            assertEquals("HR Specialist", result.get(5).getPosition());
            assertEquals(55000, result.get(5).getSalary());
            assertEquals("HR degree and experience", result.get(5).getRequirements());
        }

        @Test
        void first() {
            List<Vacancy> result = vacancyDAO.findAll();
            assertNotNull(result);
            assertEquals(6, result.size());
            assertEquals(1L, result.getFirst().getId());
            assertEquals("Software Engineer", result.getFirst().getPosition());
            assertEquals(90000, result.getFirst().getSalary());
            assertEquals("Experience with Java, Python", result.getFirst().getRequirements());
        }

        @Test
        void last() {
            List<Vacancy> result = vacancyDAO.findAll();
            assertNotNull(result);
            assertEquals(6, result.size());
            assertEquals(6L, result.get(5).getId());
            assertEquals("HR Specialist", result.get(5).getPosition());
            assertEquals(55000, result.get(5).getSalary());
            assertEquals("HR degree and experience", result.get(5).getRequirements());
        }
    }

    @Nested
    class FindByCompanyTests {
        @Test
        void success() {
            Company company = companyDAO.findById(1L);
            List<Vacancy> result = vacancyDAO.findByCompany(company);
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(1L, result.getFirst().getId());
            assertEquals("Software Engineer", result.getFirst().getPosition());
            assertEquals(90000, result.getFirst().getSalary());
            assertEquals("Experience with Java, Python", result.getFirst().getRequirements());
        }

        @Test
        void empty() {
            Company company = new Company();
            company.setId(999L);
            List<Vacancy> result = vacancyDAO.findByCompany(company);
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void nullCompany() {
            List<Vacancy> result = vacancyDAO.findByCompany(null);
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void newCompany() {
            Company newCompany = new Company();
            newCompany.setId(10L);
            List<Vacancy> result = vacancyDAO.findByCompany(newCompany);
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    class FindByPositionTests {
        @Test
        void success() {
            List<Vacancy> result = vacancyDAO.findByPosition("Engineer");
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(1L, result.getFirst().getId());
            assertEquals("Software Engineer", result.getFirst().getPosition());
            assertEquals(90000, result.getFirst().getSalary());
            assertEquals("Experience with Java, Python", result.getFirst().getRequirements());
        }

        @Test
        void empty() {
            List<Vacancy> result = vacancyDAO.findByPosition("Nonexistent");
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void emptyString() {
            List<Vacancy> result = vacancyDAO.findByPosition("");
            assertNotNull(result);
            assertEquals(6, result.size()); // Возвращает всех, так как like '' совпадает со всеми
        }

        @Test
        void caseInsensitive() {
            List<Vacancy> result = vacancyDAO.findByPosition("manager");
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("Project Manager", result.getFirst().getPosition());
            assertEquals("Sales Manager", result.get(1).getPosition());
        }
    }

    @Nested
    class FindBySalaryRangeTests {
        @Test
        void success() {
            List<Vacancy> result = vacancyDAO.findBySalaryRange(80000, 90000);
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals(1L, result.getFirst().getId());
            assertEquals(3L, result.get(1).getId());
        }

        @Test
        void empty() {
            List<Vacancy> result = vacancyDAO.findBySalaryRange(100000, 200000);
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void neg() {
            List<Vacancy> result = vacancyDAO.findBySalaryRange(-100, -50);
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void inverted() {
            List<Vacancy> result = vacancyDAO.findBySalaryRange(90000, 80000);
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void successLeft() {
            List<Vacancy> result = vacancyDAO.findBySalaryLess(60000);
            assertNotNull(result);
            assertEquals(3, result.size());
            assertEquals(2L, result.getFirst().getId());
            assertEquals(5L, result.get(1).getId());
            assertEquals(6L, result.get(2).getId());
        }

        @Test
        void successRight() {
            List<Vacancy> result = vacancyDAO.findBySalaryGreater(60000);
            assertNotNull(result);
            assertEquals(4, result.size());
            assertEquals(1L, result.getFirst().getId());
            assertEquals(3L, result.get(1).getId());
            assertEquals(4L, result.get(2).getId());
            assertEquals(5L, result.get(3).getId());
        }
    }

    @Nested
    class FindByRequirementsTests {
        @Test
        void success() {
            List<Vacancy> result = vacancyDAO.findByRequirements("Java");
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Software Engineer", result.getFirst().getPosition());
        }

        @Test
        void empty() {
            List<Vacancy> result = vacancyDAO.findByRequirements("NonexistentReq");
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void findByRequirements_EmptyString() {
            List<Vacancy> result = vacancyDAO.findByRequirements("");
            assertNotNull(result);
            assertEquals(6, result.size());
        }

        @Test
        void caseInsensitive() {
            List<Vacancy> result = vacancyDAO.findByRequirements("java");
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Software Engineer", result.getFirst().getPosition());
        }
    }
}
