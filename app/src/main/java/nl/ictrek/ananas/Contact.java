package nl.ictrek.ananas;

/**
 * Created by wouter on 18-4-17.
 */

public class Contact {
    private String mUsername;
    private String mEmail;
    private String mPhoneNumer; // TODO: add a class for phone numbers.
    private String mFullName;

    public Contact(String mUsername, String mEmail, String mPhoneNumer, String mFullName) {
        this.mUsername = mUsername;
        this.mEmail = mEmail;
        this.mPhoneNumer = mPhoneNumer;
        this.mFullName = mFullName;
    }

    public Contact(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getBestName() {
        if(mFullName != null)
            return getmFullName();
        else
            return getmUsername();
    }

    public String getmUsername() {
        return mUsername;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmPhoneNumer() {
        return mPhoneNumer;
    }

    public void setmPhoneNumer(String mPhoneNumer) {
        this.mPhoneNumer = mPhoneNumer;
    }

    public String getmFullName() {
        return mFullName;
    }

    public void setmFullName(String mFullName) {
        this.mFullName = mFullName;
    }
}
