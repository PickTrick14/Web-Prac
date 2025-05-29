package com.db.dao;

import com.db.entity.Company;
import com.db.entity.Vacancy;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Repository
@Transactional
public class VacancyDAO extends GeneralDAO<Vacancy> {
    public VacancyDAO() {
        super(Vacancy.class);
    }

    public List<Vacancy> findByCompany(Company company) {
        Session curSession = getCurrentSession();
        return curSession.createQuery("from Vacancy where company = :company", Vacancy.class)
                .setParameter("company", company).getResultList();
    }

    public List<Vacancy> findByPosition(String position) {
        Session curSession = getCurrentSession();
        String pos = position.toLowerCase();
        return curSession.createQuery("from Vacancy where lower(position) like :position", Vacancy.class)
                .setParameter("position", '%' + pos + '%').getResultList();
    }

    public List<Vacancy> findBySalaryRange(Integer minSalary, Integer maxSalary) {
        Session curSession = getCurrentSession();
        return curSession.createQuery("from Vacancy where salary between :minSalary and :maxSalary", Vacancy.class)
                .setParameter("minSalary", minSalary)
                .setParameter("maxSalary", maxSalary).getResultList();
    }

    public List<Vacancy> findBySalaryLess(Integer minSalary) {
        Session curSession = getCurrentSession();
        return curSession.createQuery("from Vacancy where salary <= :salary", Vacancy.class)
                .setParameter("salary", minSalary).getResultList();
    }

    public List<Vacancy> findBySalaryGreater(Integer maxSalary) {
        Session curSession = getCurrentSession();
        return curSession.createQuery("from Vacancy where salary >= :maxSalary", Vacancy.class)
                .setParameter("maxSalary", maxSalary).getResultList();
    }

    public List<Vacancy> findByRequirements(String requirements) {
        Session curSession = getCurrentSession();
        String req = requirements.toLowerCase();
        return curSession.createQuery("from Vacancy where lower(requirements) like :requirements", Vacancy.class)
                .setParameter("requirements", '%' + req + '%').getResultList();
    }
}