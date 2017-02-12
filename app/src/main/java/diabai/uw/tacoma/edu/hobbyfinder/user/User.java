package diabai.uw.tacoma.edu.hobbyfinder.user;

import java.io.Serializable;


public class User implements Serializable {
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String GENDER = "gender";
    public static final String HOMETOWN = "homeTown";

    private String mId;
    private String mName;
    private String mEmail;
    private String mGender;
    private String mHomeTown;

    public User(String mId, String mName, String mEmail, String mGender, String mHomeTown) {
        this.mId = mId;
        this.mName = mName;
        this.mEmail = mEmail;
        this.mGender = mGender;
        this.mHomeTown = mHomeTown;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmGender() {
        return mGender;
    }

    public void setmGender(String mGender) {
        this.mGender = mGender;
    }

    public String getmHomeTown() {
        return mHomeTown;
    }

    public void setmHomeTown(String mHomeTown) {
        this.mHomeTown = mHomeTown;
    }
}
