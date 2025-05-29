package com.db.dao;

import com.db.entity.Account;
import com.db.entity.Company;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Repository
@Transactional
public class CompanyDAO extends GeneralDAO<Company> {
    public CompanyDAO() {
        super(Company.class);
    }

    public List<Company> findByName(String name) {
        Session curSession = getCurrentSession();
        return curSession.createQuery("from Company where lower(name) like :name", Company.class)
                .setParameter("name", '%' + name.toLowerCase() + '%').getResultList();
    }

    public List<Company> findByAccount(Account account) {
        List<Company> temp = new ArrayList<Company>();
        if (account == null || !account.isEmployer()) {
            return temp;
        }

        Session curSession = getCurrentSession();
        return curSession.createQuery(
                        "from Company c where c.account = :account and c.account.isEmployer = true",
                        Company.class)
                .setParameter("account", account)
                .getResultList();
    }
}