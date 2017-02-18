package diabai.uw.tacoma.edu.hobbyfinder.user;

import java.io.Serializable;


/**
 * User class.
 * <p>
 * This class contains the functionality to create and manipulate a User object
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
     * Gets the user's email
     *
     * @return the user email
     */
    public String getmEmail() {
        return mEmail;
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
     * Gets the user's hometown
     *
     * @return the user's hometown
     */
    public String getmHomeTown() {
        return mHomeTown;
    }
}
