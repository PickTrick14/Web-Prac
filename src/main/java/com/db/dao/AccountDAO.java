package com.db.dao;

import com.db.entity.Account;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class AccountDAO extends GeneralDAO<Account> {

    public AccountDAO() {
        super(Account.class);
    }

    public List<Account> findByEmail(String email) {
        Session cur_session = getCurrentSession();
        return cur_session.createQuery("from Account where email = :email", Account.class)
                .setParameter("email", email).getResultList();
    }

    public List<Account> findEmployer() {
        Session cur_session = getCurrentSession();
        return cur_session.createQuery("from Account where isEmployer = :Employer", Account.class)
                .setParameter("Employer", true).getResultList();
    }

    public List<Account> findWorker() {
        Session cur_session = getCurrentSession();
        return cur_session.createQuery("from Account where isEmployer = :Employer", Account.class)
                .setParameter("Employer", false).getResultList();
    }
}
