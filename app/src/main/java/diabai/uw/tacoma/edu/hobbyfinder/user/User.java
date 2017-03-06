package diabai.uw.tacoma.edu.hobbyfinder.user;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;


/**
 * User class.
 * <p>
 * This class contains the functionality to create and manipulate a User object
 *
 *
 * @Author: Ibrahim Diabate, Edgard Solorzano
 * @version: 2.0
 */
public class User implements Serializable {
    //The user's id
    private String mId;
    //The user's name
    private String mName;
    //The user's email
    private String mEmail;
    //The user's gender
    private String mGender;
    //The user's hometown
    private String mHomeTown;

    private String mHobbies;

    public static final String ID = "id", NAME= "name"
            , EMAIL = "email", GENDER = "gender", HOMETOWN = "hometown";
    /**
     * Constructor for the user object
     *
     * @param mId       the user's id
     * @param mName     the user's name
     * @param mEmail    the user's email
     * @param mGender   the user's email
     * @param mHomeTown the user's hometown
     */
    public User(String mId, String mName, String mEmail, String mGender, String mHomeTown) {
        this.mId = mId;
        this.mName = mName;
        this.mEmail = mEmail;
        this.mGender = mGender;
        this.mHomeTown = mHomeTown;
    }

    /**
     * Gets the user ID
     *
     * @return the user id
     */
    public String getmId() {
        return mId;
    }

    /**
     * Gets the user name
     *
     * @return the user name
     */
    public String getmName() {
        return mName;
    }

    /**
     * Sets the user name
     */
    public void setmName(String name) {
        this.mName = name;
    }

    /**
     * Gets the user's email
     *
     * @return the user email
     */
    public String getmEmail() {
        return mEmail;
    }

    /**
     * Set the users email
     */
    public void setmEmail(String email) {
        this.mEmail = email;
    }


    /**
     * Gets the user gender
     *
     * @return the user's gender
     */
    public String getmGender() {
        return mGender;
    }

    /**
     * Set users gender
     */
    public void setmGender(String gender) {
        this.mGender = gender;
    }


    /**
     * Set users gender
     */
    public void setmHometown(String hometown) {
        this.mHomeTown = hometown;
    }

    /**
     * Gets the user's hometown
     *
     * @return the user's hometown
     */
    public String getmHomeTown() {
        return mHomeTown;
    }

    /**
     * Parses the json string, returns an error message if unsuccessful.
     * Returns course list if success.
     * @param userJSON
     * @return reason or null if successful.
     */
    public static String parseUserJSON(String userJSON, List<User> userList) {
        String reason = null;
        if (userJSON != null) {
            try {
                JSONArray arr = new JSONArray(userJSON);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    User user = new User(obj.getString(User.ID), obj.getString(User.NAME)
                            , obj.getString(User.EMAIL), obj.getString(User.GENDER),obj.getString(User.HOMETOWN));
                    userList.add(user);
                }
            } catch (JSONException e) {
                reason =  "No user found with the hobby selected" ;
            }
        }
        return reason;
    }
}
