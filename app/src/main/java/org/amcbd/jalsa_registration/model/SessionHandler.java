package org.amcbd.jalsa_registration.model;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

/**
 * Created by AndroidIgniter on 23 Mar 2019 020.
 */

public class SessionHandler {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EXPIRES = "expires";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_PROFILE_IMAGE = "profile_img";
    private static final String KEY_EMPTY = "";


    private static final String KEY_NAME = "name";
    private static final String KEY_FATHER_NAME = "father_name";
    private static final String KEY_MOTHER_NAME = "mother_name";
    private static final String KEY_MOB = "mob";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_DOB = "DOB";
    private static final String KEY_JAMAT = "jamat";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_MAJLIS = "majlis";
    private static final String KEY_BY_BIRTHAHMADI = "by_birth_ahmadi";
    private static final String KEY_BUDGET = "budget";
    private static final String KEY_BLOOD_GROUP = "blood_group";
    private static final String KEY_MARITAL_STATUS = "maritial_status";
    private static final String KEY_OCCUPATION = "occupation";
    private static final String KEY_PROFILE_IMG = "profile_img";

    private Context mContext;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPreferences;

    public SessionHandler(Context mContext) {
        this.mContext = mContext;
        mPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.mEditor = mPreferences.edit();
    }

    /**
     * Logs in the user by saving user details and setting session
     *
     * @param username
     * @param fullName
     */
    public void loginUser(String username, String fullName) {
        mEditor.putString(KEY_USERNAME, username);
        mEditor.putString(KEY_FULL_NAME, fullName);
        Date date = new Date();

        //Set user session for next 7 days
        long millis = date.getTime() + (7 * 24 * 60 * 60 * 1000);
        mEditor.putLong(KEY_EXPIRES, millis);
        mEditor.commit();
    }

    /**
     * Logs in the user by saving user details and setting session
     *
     * @param username
     * @param fullName
     */
    public void loginUser(String username, String fullName, String imagePath) {
        mEditor.putString(KEY_USERNAME, username);
        mEditor.putString(KEY_FULL_NAME, fullName);
        mEditor.putString(KEY_PROFILE_IMAGE, imagePath);
        Date date = new Date();

        //Set user session for next 7 days
        long millis = date.getTime() + (7 * 24 * 60 * 60 * 1000);
        mEditor.putLong(KEY_EXPIRES, millis);
        mEditor.commit();
    }

    public void loginUser(String username, String name, String father_name, String mother_name, String mob, String email, String DOB, String jamat, String gender,
                          String majlis, String by_birth_ahmadi, String budget, String blood_group, String maritial_status, String occupation, String profile_img) {
        mEditor.putString(KEY_USERNAME, username);
        mEditor.putString(KEY_NAME, name);
        mEditor.putString(KEY_FATHER_NAME, father_name);
        mEditor.putString(KEY_MOTHER_NAME, mother_name);
        mEditor.putString(KEY_MOB, mob);
        mEditor.putString(KEY_EMAIL, email);
        mEditor.putString(KEY_DOB, DOB);
        mEditor.putString(KEY_JAMAT, jamat);
        mEditor.putString(KEY_GENDER, gender);
        mEditor.putString(KEY_MAJLIS, majlis);
        mEditor.putString(KEY_BY_BIRTHAHMADI, by_birth_ahmadi);
        mEditor.putString(KEY_BUDGET, budget);
        mEditor.putString(KEY_BLOOD_GROUP, blood_group);
        mEditor.putString(KEY_MARITAL_STATUS, maritial_status);
        mEditor.putString(KEY_OCCUPATION, occupation);
        mEditor.putString(KEY_PROFILE_IMAGE, profile_img);
        Date date = new Date();

        //Set user session for next 7 days
        long millis = date.getTime() + (7 * 24 * 60 * 60 * 1000);
        mEditor.putLong(KEY_EXPIRES, millis);
        mEditor.commit();
    }

    /**
     * Checks whether user is logged in
     *
     * @return
     */
    public boolean isLoggedIn() {
        Date currentDate = new Date();

        long millis = mPreferences.getLong(KEY_EXPIRES, 0);

        /* If shared preferences does not have a value
         then user is not logged in
         */
        if (millis == 0) {
            return false;
        }
        Date expiryDate = new Date(millis);

        /* Check if session is expired by comparing
        current date and Session expiry date
        */
        return currentDate.before(expiryDate);
    }

    /**
     * Fetches and returns user details
     *
     * @return user details
     */
    public User getUserDetails() {
        //Check if user is logged in first
        if (!isLoggedIn()) {
            return null;
        }
        User user = new User();
        user.setUsername(mPreferences.getString(KEY_USERNAME, KEY_EMPTY));
        user.setName(mPreferences.getString(KEY_NAME, KEY_EMPTY));
        user.setFather_name(mPreferences.getString(KEY_FATHER_NAME, KEY_EMPTY));
        user.setMother_name(mPreferences.getString(KEY_MOTHER_NAME, KEY_EMPTY));
        user.setMob(mPreferences.getString(KEY_MOB, KEY_EMPTY));
        user.setEmail(mPreferences.getString(KEY_EMAIL, KEY_EMPTY));
        user.setDOB(mPreferences.getString(KEY_DOB, KEY_EMPTY));
        user.setJamat(mPreferences.getString(KEY_JAMAT, KEY_EMPTY));
        user.setGender(mPreferences.getString(KEY_GENDER, KEY_EMPTY));
        user.setMajlis(mPreferences.getString(KEY_MAJLIS, KEY_EMPTY));
        user.setBy_birth_ahmadi(mPreferences.getString(KEY_BY_BIRTHAHMADI, KEY_EMPTY));
        user.setBudget(mPreferences.getString(KEY_BUDGET, KEY_EMPTY));
        user.setBlood_group(mPreferences.getString(KEY_BLOOD_GROUP, KEY_EMPTY));
        user.setMaritial_status(mPreferences.getString(KEY_MARITAL_STATUS, KEY_EMPTY));
        user.setOccupation(mPreferences.getString(KEY_OCCUPATION, KEY_EMPTY));
        user.setProfile_img(mPreferences.getString(KEY_PROFILE_IMG, KEY_EMPTY));
        user.setSessionExpiryDate(new Date(mPreferences.getLong(KEY_EXPIRES, 0)));

        return user;
    }

    /*public User getUserDetailsInDashboard() {
        //Check if user is logged in first
        if (!isLoggedIn()) {
            return null;
        }
        User user = new User();
        user.setUsername(mPreferences.getString(KEY_USERNAME, KEY_EMPTY));
        user.setFullName(mPreferences.getString(KEY_FULL_NAME, KEY_EMPTY));
        user.setImagePath(mPreferences.getString(KEY_PROFILE_IMAGE, KEY_PROFILE_IMAGE));
        user.setSessionExpiryDate(new Date(mPreferences.getLong(KEY_EXPIRES, 0)));

        return user;
    }*/

    /**
     * Logs out user by clearing the session
     */
    public void logoutUser() {
        mEditor.clear();
        mEditor.commit();
    }

}
