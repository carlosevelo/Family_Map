package com.example.familymap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import Request.EventsRequest;
import Request.LoginRequest;
import Request.PersonsRequest;
import Request.RegisterRequest;
import Result.EventsResult;
import Result.LoginResult;
import Result.PersonsResult;
import Result.RegisterResult;

import static org.junit.Assert.*;

public class ServerProxyTests {
    ServerProxy serverProxy;
    LoginRequest loginRequest;
    RegisterRequest registerRequest;

    @Before
    public void setUp() {
        this.serverProxy = new ServerProxy("localhost", "8080");
        this.loginRequest = new LoginRequest("testUsername", "testPassword");
        this.registerRequest = new RegisterRequest("testUsername1", "testPassword",
                "testEmail", "testFirstName", "testLastName", "m");
    }

    @After
    public void tearDown() {

    }

    @Test
    public void loginSuccess() throws IOException {
        LoginResult result = serverProxy.login(loginRequest);
        assertTrue(result.getSuccess());
        assertNotNull(result.getUsername());
        assertNotNull(result.getAuthtoken());
        assertNotNull(result.getPersonID());
    }

    @Test
    public void loginFail() throws IOException {
        loginRequest.setUsername("badUsername");
        LoginResult result = serverProxy.login(loginRequest);
        assertFalse(result.getSuccess());
        assertNotNull(result.getMessage());
    }

    @Test
    public void registerSuccess() throws IOException {
        RegisterResult result = serverProxy.register(registerRequest);
        assertTrue(result.getSuccess());
        assertNotNull(result.getAuthtoken());
        assertNotNull(result.getPersonID());
        assertNotNull(result.getUsername());
    }

    @Test
    public void registerFail() throws IOException {
        RegisterResult result = serverProxy.register(registerRequest);
        assertFalse(result.getSuccess());
        assertNotNull(result.getMessage());
    }

    @Test
    public void getPeopleSuccess() throws IOException {
        LoginResult loginResult = serverProxy.login(loginRequest);
        PersonsRequest personsRequest = new PersonsRequest(loginResult.getAuthtoken());
        PersonsResult personsResult = serverProxy.getPeople(personsRequest);
        assertNotNull(personsResult);
        assertTrue(personsResult.getSuccess());
        assertNotNull(personsResult.getData());
    }

    @Test
    public void getPeopleFail() throws IOException {
        PersonsRequest personsRequest = new PersonsRequest("badAuthToken");
        PersonsResult personsResult = serverProxy.getPeople(personsRequest);
        assertNull(personsResult);
    }

    @Test
    public void getEventsSuccess() throws IOException {
        LoginResult loginResult = serverProxy.login(loginRequest);
        EventsRequest eventsRequest = new EventsRequest(loginResult.getAuthtoken());
        EventsResult eventsResult = serverProxy.getEvents(eventsRequest);
        assertNotNull(eventsResult);
        assertTrue(eventsResult.getSuccess());
        assertNotNull(eventsResult.getData());
    }

    @Test
    public void getEventsFail() throws IOException {
        EventsRequest eventsRequest = new EventsRequest("badAuthToken");
        EventsResult eventsResult = serverProxy.getEvents(eventsRequest);
        assertNull(eventsResult);
    }
}
