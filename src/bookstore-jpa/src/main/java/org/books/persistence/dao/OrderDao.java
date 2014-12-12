/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.persistence.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import org.books.persistence.entity.Customer;
import org.books.persistence.entity.Order;
import org.books.persistence.dto.OrderInfo;

/**
 *
 * @author micic
 */
public class OrderDao {

    private static final String ORDER_SEARCH_BY_NUMBER_SQL = "org.books.persistence.OrderByNumber";
    private static final String ORDER_SEARCH_BY_NUMBER_SQL_NAME_PARAM = "number";

    private static final String ORDER_SEARCH_BY_CUSTOMER_AND_DATE_SQL = "org.books.persistence.OrderByCustomerAndDate";
    private static final String ORDER_SEARCH_BY_CUSTOMER_AND_DATE_SQL_BEGIN_PARAM = "begin";
    private static final String ORDER_SEARCH_BY_CUSTOMER_AND_DATE_SQL_END_PARAM = "end";
    private static final String ORDER_SEARCH_BY_CUSTOMER_AND_DATE_SQL_CUSTOMER_PARAM = "customer";

    private final EntityManager mgr;

    public OrderDao(EntityManager mgr) {
        this.mgr = mgr;
    }

    public Order getByNumber(String number) {
        return this.mgr.createNamedQuery(ORDER_SEARCH_BY_NUMBER_SQL, Order.class)
                .setParameter(ORDER_SEARCH_BY_NUMBER_SQL_NAME_PARAM, number)
                .getSingleResult();
    }

    public List<OrderInfo> searchByCustomerAndYear(Customer customer, int year) {

        Calendar beginCal = Calendar.getInstance();
        beginCal.set(year, 0, 1, 0, 0, 0);
        Date beginDate = new Date(beginCal.getTimeInMillis());

        Calendar endCal = Calendar.getInstance();
        endCal.set(year, 11, 31, 0, 0, 0);
        Date endDate = new Date(endCal.getTimeInMillis());

        return this.mgr.createNamedQuery(ORDER_SEARCH_BY_CUSTOMER_AND_DATE_SQL, OrderInfo.class)
                .setParameter(ORDER_SEARCH_BY_CUSTOMER_AND_DATE_SQL_CUSTOMER_PARAM, customer)
                .setParameter(ORDER_SEARCH_BY_CUSTOMER_AND_DATE_SQL_BEGIN_PARAM, beginDate)
                .setParameter(ORDER_SEARCH_BY_CUSTOMER_AND_DATE_SQL_END_PARAM, endDate)
                .getResultList();
    }
}
