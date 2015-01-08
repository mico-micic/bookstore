package org.books.ejb.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.books.ejb.CatalogService;
import org.books.ejb.CustomerService;
import org.books.ejb.OrderService;
import org.books.ejb.exception.BookNotFoundException;
import org.books.ejb.exception.CustomerNotFoundException;
import org.books.ejb.exception.InvalidOrderStatusException;
import org.books.ejb.exception.OrderNotFoundException;
import org.books.ejb.exception.PaymentFailedException;
import org.books.persistence.dao.OrderDao;
import org.books.persistence.dto.OrderInfo;
import org.books.persistence.dto.OrderItem;
import org.books.persistence.entity.Book;
import org.books.persistence.entity.CreditCard;
import org.books.persistence.entity.Customer;
import org.books.persistence.entity.LineItem;
import org.books.persistence.entity.Order;

/**
 * @author Sigi
 */
@Stateless(name = "OrderService")
public class OrderServiceBean implements OrderService {

    private static final Pattern CREDIT_CARD_PATTERN = Pattern.compile("\\d{16}");

    private OrderDao orderDao;

    @PersistenceContext
    private EntityManager em;

    @EJB
    private CustomerService customerService;

    @EJB
    private CatalogService catalogService;

    @PostConstruct
    public void init() {
        orderDao = new OrderDao(em);
    }

    @Override
    public void cancelOrder(Long orderId) throws OrderNotFoundException, InvalidOrderStatusException {
        Order order = findOrder(orderId);
        if (!order.isCanceable()) {
            throw new InvalidOrderStatusException("The Order is in Status " + order.getStatus().name() + " and thus cannot be canceled any more!");
        }
        order.setStatus(Order.Status.canceled);
        em.merge(order);
    }

    @Override
    public Order findOrder(Long orderId) throws OrderNotFoundException {
        Order order = em.find(Order.class, orderId);
        if (order == null) {
            throw new OrderNotFoundException("There is no order with the given id: " + orderId);
        }
        return order;
    }

    @Override
    public Order findOrder(String number) throws OrderNotFoundException {
        try {
            return orderDao.getByNumber(number);
        } catch (NoResultException ex) {
            throw new OrderNotFoundException("There is no order with the given numer: " + number);
        }
    }

    @Override
    public OrderInfo placeOrder(Long customerId, List<OrderItem> items) throws CustomerNotFoundException, BookNotFoundException, PaymentFailedException {
        Calendar calendar = Calendar.getInstance();

        Customer customer = customerService.findCustomer(customerId);
        List<LineItem> lineItems = toLineItems(items);
        BigDecimal amount = calculateAmount(lineItems);

        // CreditCard Validation:
        validatePaymentDetails(customer);

        // Create Order:
        Order order = new Order();
        order.setCustomer(customer);
        order.setNumber(calendar.get(Calendar.YEAR) + "-" + String.format("%05d", order.getId()));
        order.setDate(calendar.getTime());
        order.setStatus(Order.Status.accepted);
        order.setAmount(amount);
        order.setCustomer(customer);
        order.setAddress(clone(customer.getAddress()));
        order.setCreditCard(clone(customer.getCreditCard()));
        order.setItems(lineItems);
        em.persist(order);

        return toOrderInfo(order);

    }

    @Override
    public List<OrderInfo> searchOrders(Long customerId, Integer year) throws CustomerNotFoundException {
        Customer customer = findCustomer(customerId);
        return orderDao.searchByCustomerAndYear(customer, year);
    }

    //
    // ----------------------------------------------------------------------------
    // Private helpers:
    // ----------------------------------------------------------------------------
    //
    private Customer findCustomer(Long customerId) throws CustomerNotFoundException {
        return customerService.findCustomer(customerId);
    }

    private BigDecimal calculateAmount(List<LineItem> lineItems) {
        BigDecimal amount = BigDecimal.ZERO;
        for (LineItem item : lineItems) {
            BigDecimal itemAmount = item.getBook().getPrice().multiply(new BigDecimal(item.getQuantity()));
            amount = amount.add(itemAmount);
        }
        return amount;
    }

    private void validatePaymentDetails(Customer customer) throws PaymentFailedException {
        CreditCard card = customer.getCreditCard();
        Matcher matcher = CREDIT_CARD_PATTERN.matcher(card.getNumber());
        if (!matcher.matches()) {
            throw new PaymentFailedException("The given CreditCard-Numer is not valid!");
        }
        checkLuhnDigit(card.getNumber());
        Calendar calendar = Calendar.getInstance();
        if (card.getExpirationYear() < calendar.get(Calendar.YEAR)
                || (card.getExpirationYear() == calendar.get(Calendar.YEAR)
                && card.getExpirationMonth() < calendar.get(Calendar.MONTH) + 1)) {
            throw new PaymentFailedException("CreditCard is expired!");
        }
    }

    private void checkLuhnDigit(String number) throws PaymentFailedException {
        int sum = 0;
        for (int i = 0; i < number.length(); i++) {
            int d = Character.digit(number.charAt(i), 10);
            if (i % 2 == number.length() % 2) {
                d += d < 5 ? d : (d - 9);
            }
            sum += d;
        }
        if (sum % 10 != 0) {
            throw new PaymentFailedException();
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T clone(T object) {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            new ObjectOutputStream(os).writeObject(object);
            ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
            return (T) new ObjectInputStream(is).readObject();
        } catch (IOException | ClassNotFoundException ex) {
            return object;
        }
    }

    private OrderInfo toOrderInfo(Order order) {
        return new OrderInfo(
                order.getId(),
                order.getNumber(),
                order.getDate(),
                order.getAmount(),
                order.getStatus());
    }

    private List<LineItem> toLineItems(List<OrderItem> items) throws BookNotFoundException {
        List<LineItem> lineItems = new ArrayList<>();
        for (OrderItem item : items) {
            final Book book = catalogService.findBook(item.getIsbn());
            lineItems.add(new LineItem(book, item.getQuantity()));
        }
        return lineItems;
    }

}
