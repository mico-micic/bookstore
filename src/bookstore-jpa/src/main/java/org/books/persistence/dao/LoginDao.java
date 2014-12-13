/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.persistence.dao;

import javax.persistence.EntityManager;
import org.books.persistence.entity.Login;

/**
 *
 * @author micic
 */
public class LoginDao {

    private static final String LOGIN_SEARCH_BY_USER_NAME = "org.books.persistence.LoginByUserName";
    private static final String LOGIN_SEARCH_BY_USER_NAME_USERNAME_PARAM = "userName";

    private final EntityManager mgr;

    public LoginDao(EntityManager mgr) {
        this.mgr = mgr;
    }

    public Login getByUserName(String userName) {
        return this.mgr.createNamedQuery(LOGIN_SEARCH_BY_USER_NAME, Login.class)
                .setParameter(LOGIN_SEARCH_BY_USER_NAME_USERNAME_PARAM, userName)
                .getSingleResult();
    }
}
