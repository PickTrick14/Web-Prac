package com.db.dao;

import com.db.entity.Person;
import com.db.entity.Resume;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class ResumeDAO extends GeneralDAO<Resume> {
    public ResumeDAO() {
        super(Resume.class);
    }

    public List<Resume> findByPerson(Person person) {
        Session curSession = getCurrentSession();
        return curSession.createQuery("from Resume where person = :person", Resume.class)
                .setParameter("person", person).getResultList();
    }

    public List<Resume> findByWantedPosition(String position) {
        Session curSession = getCurrentSession();
        return curSession.createQuery("from Resume where lower(wantedPosition) like :position", Resume.class)
                .setParameter("position", '%' + position.toLowerCase() + '%').getResultList();
    }

    public List<Resume> findByWantedSalaryRange(Integer minSalary, Integer maxSalary) {
        Session curSession = getCurrentSession();
        return curSession.createQuery("from Resume where wantedSalary between :minSalary and :maxSalary", Resume.class)
                .setParameter("minSalary", minSalary)
                .setParameter("maxSalary", maxSalary).getResultList();
    }

    public List<Resume> findBySkills(String skills) {
        Session curSession = getCurrentSession();
        return curSession.createQuery("from Resume where lower(skills) like :skills", Resume.class)
                .setParameter("skills", '%' + skills.toLowerCase() + '%').getResultList();
    }
}
