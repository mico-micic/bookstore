package org.books.persistence;

import org.books.persistence.testdata.AbstractTestBase;
import org.books.persistence.dao.LoginDao;
import org.books.persistence.entity.Login;
import org.books.persistence.testdata.CustomerData;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * @author micic
 */
public class LoginDaoTest extends AbstractTestBase {

    @Test
    public void testGetByUserName() {

        Login result = new LoginDao(getEm()).getByUserName(CustomerData.SUPER_USER.email());
       
        assertNotNull(result);
        assertEquals(CustomerData.SUPER_USER.email(), result.getUserName());
    }
}
