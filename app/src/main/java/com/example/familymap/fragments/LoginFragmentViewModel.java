package com.example.familymap.fragments;

import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.lifecycle.ViewModel;

public class LoginFragmentViewModel extends ViewModel {

    // Login/Register Fields
    private EditText mHostField;
    private EditText mPortField;
    private EditText mUsername;
    private EditText mPassword;
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mEmail;
    private RadioGroup mGender;
    private String gender;
    private Button mLoginButton;
    private Button mRegisterButton;

    // Login/Register Result values
    private String authToken;
    private String username;
    private String personID;
    private String loginMessage;
    private Boolean loginSuccess;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Boolean getLoginSuccess() {
        return loginSuccess;
    }

    public void setLoginSuccess(Boolean loginSuccess) {
        this.loginSuccess = loginSuccess;
    }

    public String getLoginMessage() {
        return loginMessage;
    }

    public void setLoginMessage(String message) {
        this.loginMessage = message;
    }

    public EditText getmHostField() {
        return mHostField;
    }

    public void setmHostField(EditText mHostField) {
        this.mHostField = mHostField;
    }

    public EditText getmPortField() {
        return mPortField;
    }

    public void setmPortField(EditText mPortField) {
        this.mPortField = mPortField;
    }

    public EditText getmUsername() {
        return mUsername;
    }

    public void setmUsername(EditText mUsername) {
        this.mUsername = mUsername;
    }

    public EditText getmPassword() {
        return mPassword;
    }

    public void setmPassword(EditText mPassword) {
        this.mPassword = mPassword;
    }

    public EditText getmFirstName() {
        return mFirstName;
    }

    public void setmFirstName(EditText mFirstName) {
        this.mFirstName = mFirstName;
    }

    public EditText getmLastName() {
        return mLastName;
    }

    public void setmLastName(EditText mLastName) {
        this.mLastName = mLastName;
    }

    public EditText getmEmail() {
        return mEmail;
    }

    public void setmEmail(EditText mEmail) {
        this.mEmail = mEmail;
    }

    public Button getmLoginButton() {
        return mLoginButton;
    }

    public void setmLoginButton(Button mLoginButton) {
        this.mLoginButton = mLoginButton;
    }

    public Button getmRegisterButton() {
        return mRegisterButton;
    }

    public void setmRegisterButton(Button mRegisterButton) {
        this.mRegisterButton = mRegisterButton;
    }

    public RadioGroup getmGender() {
        return mGender;
    }

    public void setmGender(RadioGroup mGender) {
        this.mGender = mGender;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }
}
