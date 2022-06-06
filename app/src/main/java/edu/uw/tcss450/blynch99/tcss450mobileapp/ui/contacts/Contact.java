package edu.uw.tcss450.blynch99.tcss450mobileapp.ui.contacts;

import java.io.Serializable;

/**
 * Data class for contacts
 */
public class Contact implements Serializable {

    private final String mId, mNickname, mFirstname, mLastname, mEmail;
    private FriendStatus mStatus;

    /**
     * constructor
     * @param id Id
     * @param nickname Nickname
     * @param firstname First Name
     * @param lastname Last Name
     * @param email Email
     * @param status Status
     */
    public Contact(String id, String nickname, String firstname, String lastname, String email, FriendStatus status){
        this.mId = id;
        this.mNickname = nickname;
        this.mFirstname = firstname;
        this.mLastname = lastname;
        this.mEmail = email;
        this.mStatus = status;
    }

    /**
     * @return Id
     */
    public String getId() {
        return mId;
    }

    /**
     * @return Nickname
     */
    public String getNickname() {
        return mNickname;
    }

    /**
     *
     * @return First Name
     */
    public String getFirstname() {
        return mFirstname;
    }

    /**
     *
     * @return Last Name
     */
    public String getLastname() {
        return mLastname;
    }

    /**
     *
     * @return Email
     */
    public String getEmail() {
        return mEmail;
    }

    /**
     *
     * @return Status
     */
    public FriendStatus getStatus() {
        return mStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this.getClass() != o.getClass()) return false;

        Contact other = (Contact) o;
        return this.mEmail.equals(other.getEmail());
    }
}
