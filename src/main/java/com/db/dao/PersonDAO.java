package com.db.dao;

import  com.db.entity.Account;
import com.db.entity.City;
import com.db.entity.Person;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class PersonDAO extends GeneralDAO<Person> {
    public PersonDAO() {
        super(Person.class);
    }

    public List<Person> findByName(String name) {
        Session cur_session = getCurrentSession();
        return cur_session.createQuery("from Person where lower(name) like :name", Person.class)
                .setParameter("name", '%' + name.toLowerCase() + '%').getResultList();
    }

    public List<Person> findByAccount(Account account) {
        Session cur_session = getCurrentSession();
        return cur_session.createQuery("from Person where account = :account", Person.class)
                .setParameter("account", account).getResultList();
    }

    public List<Person> findIsSearching(boolean isSearching) {
        Session cur_session = getCurrentSession();
        return cur_session.createQuery("from Person where isSearching = :isSearching", Person.class)
                .setParameter("isSearching", isSearching).getResultList();
    }

    public List<Person> findByCity(City city) {
        Session cur_session = getCurrentSession();
        return cur_session.createQuery("from Person where city = :city", Person.class)
                .setParameter("city", city).getResultList();
    }

    public List<Person> findByAgeRange(Integer startAge, Integer endAge) {
        Session cur_session = getCurrentSession();
        return cur_session.createQuery("from Person where age between :startAge and :endAge", Person.class)
                .setParameter("startAge", startAge)
                .setParameter("endAge", endAge).getResultList();
    }

    public List<Person> findByGreaterAge(Integer startAge) {
        return findByAgeRange(startAge, 1000);
    }

    public List<Person> findByLessAge(Integer endAge) {
        return findByAgeRange(-1, endAge);
    }
}
