/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.ejb;

import java.util.List;
import org.books.ejb.exception.CustomerNotFoundException;
import org.books.ejb.exception.EmailAlreadyUsedException;
import org.books.ejb.exception.InvalidCredentialsException;
import org.books.persistence.dto.CustomerInfo;
import org.books.persistence.entity.Customer;

/**
 *
 * @author micic
 */
public interface CustomerService {
    
    /**
     * @param email not null, nor empty.
     * @param password not null, nor empty.
     * @throws InvalidCredentialsException when the given Credentials are not
     * correct or not complete.
     */
    public void authenticateCustomer(String email, String password) throws InvalidCredentialsException, IllegalStateException;

    public void changePassword(String email, String password) throws CustomerNotFoundException;

    public Customer findCustomer(Long customerId) throws CustomerNotFoundException;

    public Customer findCustomer(String email) throws CustomerNotFoundException;

    public Long registerCustomer(Customer customer, String password) throws EmailAlreadyUsedException;

    public List<CustomerInfo> searchCustomers(String name);

    public void updateCustomer(Customer customer) throws CustomerNotFoundException, EmailAlreadyUsedException;
}
