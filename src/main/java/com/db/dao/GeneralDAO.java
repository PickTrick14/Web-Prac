package com.db.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public abstract class GeneralDAO<T> {

    protected Class<T> classItem;
    protected SessionFactory sessionFactory;

    public GeneralDAO(Class<T> classItem) {
        this.classItem = classItem;
    }

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public T findById(Long id) {
        return getCurrentSession().get(classItem, id);
    }

    public List<T> findAll() {
        return getCurrentSession()
                .createQuery("FROM " + classItem.getName(), classItem).getResultList();
    }

    public void save(T entity) {
        getCurrentSession().persist(entity);
    }

    public void update(T entity) {
        getCurrentSession().update("Account", entity);
    }

    public void delete(T entity) {
        getCurrentSession().delete(entity);
    }

    public void deleteById(Long id) {
        T entity = findById(id);
        if (entity != null) {
            delete(entity);
        }
    }
}