package diabai.uw.tacoma.edu.hobbyfinder.user;

import java.io.Serializable;


public class User implements Serializable {
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String EMAIL = "email";

    private String mId, mName, mEmail;

    public User(String mId, String mName, String mEmail) {
        this.mId = mId;
        this.mName = mName;
        this.mEmail = mEmail;
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
}
