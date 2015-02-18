package org.books.presentation.admin;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

@Named
@SessionScoped
public class AdminBean implements Serializable {

    private static final Logger logger = Logger.getLogger(AdminBean.class.getName());

    private String userName;
    private String password;
    @PersistenceContext
    private EntityManager entityManager;
    @Resource
    private UserTransaction transaction;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void registerEmployee() {
        try {
            Query query = entityManager.createNamedQuery(Login.findByUserNameQuery);
            query.setParameter("userName", userName);
            try {
                query.getSingleResult();
                MessageFactory.info("org.books.Bookstore.EMPLOYEE_ALREADY_REGISTERED");
            } catch (NoResultException ex) {
                transaction.begin();
                entityManager.persist(new Login(userName, hashString(password)));
                transaction.commit();
                userName = password = null;
                MessageFactory.info("org.books.Bookstore.EMPLOYEE_REGISTERED");
            }
        } catch (Exception ex) {
            logger.log(Level.INFO, "Error during employee registration", ex);
            MessageFactory.info("org.books.Bookstore.SYSTEM_ERROR");
        }
    }

    private String hashString(String password) {
        return Base64.getEncoder().encodeToString(hash(password));
    }

    private byte[] hash(String password) {
        String saltedPwd = new StringBuilder()
                .append(password)
                .toString();
        try {
            return MessageDigest
                    .getInstance("SHA-256")
                    .digest(saltedPwd.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            throw new IllegalStateException("Error in the static setup of the Hashing-Algorithm");
        }
    }
}
