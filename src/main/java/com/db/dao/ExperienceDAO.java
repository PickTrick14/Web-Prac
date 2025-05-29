package com.db.dao;

import com.db.entity.Company;
import com.db.entity.Experience;
import com.db.entity.Person;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Repository
@Transactional
public class ExperienceDAO extends GeneralDAO<Experience> {
    public ExperienceDAO() {
        super(Experience.class);
    }

    public List<Experience> findByPerson(Person person) {
        Session curSession = getCurrentSession();
        return curSession.createQuery(
                        "select e from Experience e join ExExpPerson ep on e.id = ep.experience.id where ep.person = :person",
                        Experience.class)
                .setParameter("person", person).getResultList();
    }

    public List<Experience> findByCompany(Company company) {
        Session curSession = getCurrentSession();
        return curSession.createQuery("from Experience where company = :company", Experience.class)
                .setParameter("company", company).getResultList();
    }

    public List<Experience> findByPosition(String position) {
        Session curSession = getCurrentSession();
        String pos = position.toLowerCase();
        return curSession.createQuery("from Experience where lower(position) like :position", Experience.class)
                .setParameter("position", '%' + pos + '%').getResultList();
    }
}