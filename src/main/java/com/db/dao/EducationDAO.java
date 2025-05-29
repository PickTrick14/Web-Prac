package com.db.dao;

import com.db.entity.Education;
import com.db.entity.Person;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Repository
@Transactional
public class EducationDAO extends GeneralDAO<Education> {
    public EducationDAO() {
        super(Education.class);
    }

    public List<Education> findByPerson(Person person) {
        Session curSession = getCurrentSession();
        return curSession.createQuery("from Education where person = :person", Education.class)
                .setParameter("person", person).getResultList();
    }

    public List<Education> findByInstitution(String institution) {
        Session curSession = getCurrentSession();
        String inst = institution.toLowerCase();
        return curSession.createQuery("from Education where lower(institution) like :institution", Education.class)
                .setParameter("institution", '%' + inst + '%').getResultList();
    }

    public List<Education> findBySpecialization(String specialization) {
        Session curSession = getCurrentSession();
        String spec = specialization.toLowerCase();
        return curSession.createQuery("from Education where lower(specialization) like :specialization", Education.class)
                .setParameter("specialization", '%' + spec + '%').getResultList();
    }
}