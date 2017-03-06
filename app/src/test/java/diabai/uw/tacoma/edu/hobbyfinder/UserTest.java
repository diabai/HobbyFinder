package diabai.uw.tacoma.edu.hobbyfinder;

import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import diabai.uw.tacoma.edu.hobbyfinder.user.User;

import static org.junit.Assert.*;


/**
 *
 * Tests for model class User.
 * @Author: Ibrahim Diabate
 * @version: 2.0
 */
public class UserTest {

    private User mUser = null;
     @Before
    public void setUp() {
         mUser =  new User("0123456789", "Ibrahim", "diabai@uw.edu", "Male", "Kent, WA");
    }
    @Test
    public void testAccountConstructor() {
        assertNotNull(mUser);
    }

    @Test
    public void testUserSetEmail() {
        mUser.setmEmail("davidDean@david.com");
        assertEquals("davidDean@david.com", mUser.getmEmail());
    }

    @Test
    public void testUserGetEmail() {
        assertEquals("diabai@uw.edu", mUser.getmEmail());
    }

    @Test
    public void testUserSetName() {

        mUser.setmName("Fat joe");
        assertEquals("Fat joe", mUser.getmName());
    }

    @Test
    public void testUserGetName() {
        assertEquals("Ibrahim", mUser.getmName());
    }

    @Test
    public void testUserSetGender() {

        mUser.setmGender("Man");
        assertEquals("Man", mUser.getmGender());
    }

    @Test
    public void testUserGetGender() {

        assertEquals("Male", mUser.getmGender());
    }

    @Test
    public void testUserSetHometown() {

        mUser.setmHometown("Bellevue, WA");
        assertEquals("Bellevue, WA", mUser.getmHomeTown());
    }

    @Test
    public void testUserGetHometown() {
        assertEquals("Kent, WA", mUser.getmHomeTown());
    }

}
