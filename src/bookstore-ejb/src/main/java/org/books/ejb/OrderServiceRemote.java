package org.books.ejb;

import javax.ejb.Remote;

/**
 * The remote interface OrderService defines the operations of a bookstore's
 * order service.
 */
@Remote
public interface OrderServiceRemote extends OrderService {

}
