package org.books.persistence;

import org.books.persistence.dao.LoginDao;
import org.books.persistence.entity.Login;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * @author micic
 */
public class LoginDaoTest extends AbstractTestBase {

    @Test
    public void testGetByUserName() {

        Login result = new LoginDao(getEm()).getByUserName("superuser@email.com");
       
        assertNotNull(result);
        assertEquals("superuser@email.com", result.getUserName());
    }
}
