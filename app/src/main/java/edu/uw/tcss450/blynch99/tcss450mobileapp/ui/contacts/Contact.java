package edu.uw.tcss450.blynch99.tcss450mobileapp.ui.contacts;

import java.io.Serializable;

public class Contact implements Serializable {

    private final String mNickname, mFirstname, mLastname, mEmail;
    private FriendStatus mStatus;

    public Contact(String nickname, String firstname, String lastname, String email, FriendStatus status){
        this.mNickname = nickname;
        this.mFirstname = firstname;
        this.mLastname = lastname;
        this.mEmail = email;
        this.mStatus = status;
    }

    public String getNickname() {
        return mNickname;
    }

    public String getFirstname() {
        return mFirstname;
    }

    public String getLastname() {
        return mLastname;
    }

    public String getEmail() {
        return mEmail;
    }

    public FriendStatus getStatus() {
        return mStatus;
    }
}
