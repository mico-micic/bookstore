/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.ejb.impl;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.books.ejb.CustomerService;
import org.books.ejb.exception.CustomerNotFoundException;
import org.books.ejb.exception.EmailAlreadyUsedException;
import org.books.ejb.exception.InvalidCredentialsException;
import org.books.persistence.dao.CustomerDao;
import org.books.persistence.dao.LoginDao;
import org.books.persistence.dto.CustomerInfo;
import org.books.persistence.entity.Customer;
import org.books.persistence.entity.Login;

/**
 * @author micic
 */
@Stateless(name = "CustomerService")
public class CustomerServiceBean implements CustomerService {

    @PersistenceContext
    private EntityManager mgr;

    private Customer getCustomerByEMail(String email) throws CustomerNotFoundException {
        Customer ret = new CustomerDao(mgr).getByEmail(email);
        if (ret == null) {
            throw new CustomerNotFoundException("There is no customer with the given EMail: " + email);
        }

        return ret;
    }

    private boolean existsCustomerWithEMail(String email) {
        Customer ret = new CustomerDao(mgr).getByEmail(email);
        return ret != null;
    }

    private Customer getCustomerById(Long id) throws CustomerNotFoundException {
        Customer ret = new CustomerDao(mgr).getById(id);
        if (ret == null) {
            throw new CustomerNotFoundException("There is no customer with the given id: " + id);
        }
        return ret;
    }

    private Login getLoginByEMail(String email) throws CustomerNotFoundException {
        Login ret = new LoginDao(mgr).getByUserName(email);
        if (ret == null) {
            throw new CustomerNotFoundException("There is no customer with the given EMail: " + email);
        }
        return ret;
    }

    @Override
    public void authenticateCustomer(String email, String password) throws InvalidCredentialsException {

        if (email == null) {
            throw new InvalidCredentialsException("EMail is empty.");
        }

        if (password == null) {
            throw new InvalidCredentialsException("Password is empty.");
        }

        try {
            Login login = getLoginByEMail(email);
            if (!login.isPasswordValid(password)) {
                throw new InvalidCredentialsException("The password is invalid.");
            }
        } catch (CustomerNotFoundException ex) {
            throw new InvalidCredentialsException("EMail invalid.");
        }
    }

    @Override
    public void changePassword(String email, String password) throws CustomerNotFoundException {

        BeanHelper.validateInput(email);
        BeanHelper.validateInput(password);

        Login login = getLoginByEMail(email);
        login.setPassword(password);

        this.mgr.merge(login);
    }

    @Override
    public Customer findCustomer(Long customerId) throws CustomerNotFoundException {

        BeanHelper.validateInput(customerId);
        return getCustomerById(customerId);
    }

    @Override
    public Customer findCustomer(String email) throws CustomerNotFoundException {
        BeanHelper.validateInput(email);
        return getCustomerByEMail(email);
    }

    @Override
    public Long registerCustomer(Customer customer, String password) throws EmailAlreadyUsedException {

        BeanHelper.validateInput(customer);
        BeanHelper.validateInput(password);

        // Check if there is already a customer with the same email address
        if (!existsCustomerWithEMail(customer.getEmail())) {

            try {
                mgr.persist(customer);
                mgr.persist(new Login(customer.getEmail(), password));

                // Make sure that we will have a customer id
                mgr.flush();

            } catch (EntityExistsException ex) {
                throw new EmailAlreadyUsedException();
            }
        } else {
            throw new EmailAlreadyUsedException();
        }

        return customer.getId();
    }

    @Override
    public List<CustomerInfo> searchCustomers(String name) {
        BeanHelper.validateInput(name);
        return new CustomerDao(mgr).searchByNamePart(name);
    }

    @Override
    public void updateCustomer(Customer customer) throws CustomerNotFoundException, EmailAlreadyUsedException {

        BeanHelper.validateInput(customer);

        // Customer Id must be set
        if (customer.getId() == null) {
            throw new CustomerNotFoundException("There is no customer with the given id: <null>");
        }

        Customer currentCustomer = getCustomerById(customer.getId());
        boolean emailChanged = !currentCustomer.getEmail().equals(customer.getEmail());

        try {

            // Change username if required
            if (emailChanged) {

                // Check if there is already a customer with the same email address
                if (!existsCustomerWithEMail(customer.getEmail())) {
                    Login currentLogin = getLoginByEMail(currentCustomer.getEmail());
                    currentLogin.setUserName(customer.getEmail());
                } else {
                    throw new EmailAlreadyUsedException();
                }
            }

            mgr.merge(customer);

        } catch (EntityExistsException ex) {
            throw new EmailAlreadyUsedException();
        }
    }
}
