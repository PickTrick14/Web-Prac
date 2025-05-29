package com.db.dao;

import com.db.entity.Response;
import com.db.entity.Person;
import com.db.entity.Vacancy;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class ResponseDAO extends GeneralDAO<Response> {
    public ResponseDAO() {
        super(Response.class);
    }

    public List<Response> findByPerson(Person person) {
        Session curSession = getCurrentSession();
        return curSession.createQuery("from Response where person = :person", Response.class)
                .setParameter("person", person).getResultList();
    }

    public List<Response> findByVacancy(Vacancy vacancy) {
        Session curSession = getCurrentSession();
        return curSession.createQuery("from Response where vacancy = :vacancy", Response.class)
                .setParameter("vacancy", vacancy).getResultList();
    }

    public List<Response> findByStatus(String status) {
        Session curSession = getCurrentSession();
        return curSession.createQuery("from Response where status = :status", Response.class)
                .setParameter("status", status).getResultList();
    }
}
