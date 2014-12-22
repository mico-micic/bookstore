/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.persistence.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.books.persistence.entity.Customer;
import org.books.persistence.dto.CustomerInfo;

/**
 *
 * @author micic
 */
public class CustomerDao {

    private static final String CUSTOMER_SEARCH_BY_MAIL_SQL = "org.books.persistence.CustomerByEmail";
    private static final String CUSTOMER_SEARCH_BY_MAIL_SQL_MAIL_PARAM = "email";

    private static final String CUSTOMER_SEARCH_BY_NAME_PART_SQL = "org.books.persistence.CustomerByNamePart";
    private static final String CUSTOMER_SEARCH_BY_NAME_PART_SQL_PART_PARAM = "part";
    
    private final EntityManager mgr;

    public CustomerDao(EntityManager mgr) {
        this.mgr = mgr;
    }
    
    public Customer getByEmail(String email) {
        try {
            return this.mgr.createNamedQuery(CUSTOMER_SEARCH_BY_MAIL_SQL, Customer.class)
                    .setParameter(CUSTOMER_SEARCH_BY_MAIL_SQL_MAIL_PARAM, email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public Customer getById(Long id) {
        return this.mgr.find(Customer.class, id);
    }
    
    public List<CustomerInfo> searchByNamePart(String part) {
        return this.mgr.createNamedQuery(CUSTOMER_SEARCH_BY_NAME_PART_SQL, CustomerInfo.class)
                .setParameter(CUSTOMER_SEARCH_BY_NAME_PART_SQL_PART_PARAM, part)
                .getResultList();
    }
}
