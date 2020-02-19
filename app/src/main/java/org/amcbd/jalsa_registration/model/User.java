package org.amcbd.jalsa_registration.model;

import java.util.Date;

/**
 * Created by AndroidIgniter on 23 Mar 2019 020.
 */

public class User {
    String username;
    String fullName;
    String imagePath;
    Date sessionExpiryDate;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setSessionExpiryDate(Date sessionExpiryDate) {
        this.sessionExpiryDate = sessionExpiryDate;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public Date getSessionExpiryDate() {
        return sessionExpiryDate;
    }
}
