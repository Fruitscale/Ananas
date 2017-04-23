package nl.ictrek.ananas;

import org.apache.commons.lang3.text.WordUtils;

/**
 * Created by wouter on 18-4-17.
 *
 * Contact class that contains details about a contact
 */
public class Contact {
    private String mUsername;
    private String mEmail;
    private String mPhoneNumer; // TODO: add a class for phone numbers.
    private String mFullName;

    /**
     * Constructor with all fields
     *
     * @param username The Matrix.org username for this contact
     * @param email The email for this contact
     * @param phoneNumer The phone number for this contact
     * @param fullName The full name for this user
     */
    public Contact(String username, String email, String phoneNumer, String fullName) {
        this.mUsername = username;
        this.mEmail = email;
        this.mPhoneNumer = phoneNumer;
        this.mFullName = fullName;
    }

    /**
     * Constructor with only the username field.
     *
     * Note that you can always set the other fields at a later point.
     *
     * @param username The Matrix.org username for this contact
     */
    public Contact(String username) {
        this.mUsername = username;
    }

    public String getBestName() {
        if(mFullName != null)
            return WordUtils.capitalize(getFullName());
        else
            return getUsername();
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getPhoneNumer() {
        return mPhoneNumer;
    }

    public void setPhoneNumer(String mPhoneNumer) {
        this.mPhoneNumer = mPhoneNumer;
    }

    public String getFullName() {
        return mFullName;
    }

    public void setFullName(String mFullName) {
        this.mFullName = mFullName;
    }
}
