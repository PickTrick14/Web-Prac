package com.db.dao;

import com.db.entity.City;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class CityDAO extends GeneralDAO<City> {
    public CityDAO() {
        super(City.class);
    }

    public List<City> findByName(String name) {
        Session curSession = getCurrentSession();
        return curSession.createQuery("from City where lower(name) like :name", City.class)
                .setParameter("name", '%' + name.toLowerCase() + '%').getResultList();
    }
}