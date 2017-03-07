package diabai.uw.tacoma.edu.hobbyfinder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import diabai.uw.tacoma.edu.hobbyfinder.user.User;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;


/**
 *
 * Tests for model class User.
 * @Author: Ibrahim Diabate
 * @version: 2.0
 */
public class UserTest {


    @Mock
    JSONArray mJsonArray;

    @Mock
    JSONObject jsonObject;

    private User mUser = null;
     @Before
    public void setUp() {
         mUser =  new User("0123456789", "Ibrahim", "diabai@uw.edu", "Male", "Kent, WA");
         MockitoAnnotations.initMocks(this);

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


    @Test
    public void testParseUserJSON() {
        try {
            when(mJsonArray.length()).thenReturn(0);
            when(mJsonArray.getJSONObject(Matchers.anyInt())).thenReturn(jsonObject);
            when(jsonObject.getString(User.ID)).thenReturn(mUser.getmId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
      //assertEquals("Success", mUser.parseUserJSON("[{\"id\":\"106272993238110\",\"name\":\"Homer Simpson\",\"email\":\"edgards@uw.edu\",\"gender\":\"male\",\"hometown\":\"Springfield\",\"hobbies\":\"Baking, Biking, Board games, Climbing, Coding, Coin collecting, Cooking, Cricket, Dancing, Deep web, Drawing, Fishing, Fishkeeping, Football, Hacking, Hunting, Insects, Jogging, Juggling, Kayaking, Laughing, Magic, Martial arts, Motor sports, Paintball, Painting, Parkour, Photography, Pottery, Singing, Skiing, Soccer, Surfing, Tennis, Traveling, Video gaming, Videophilia, Walking, Water sports, Web surfing, Wine tasting, Woodworking, Writing, Yo-yoing, \"}]", new ArrayList<User>()));
    }


}
