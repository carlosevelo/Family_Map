package com.example.familymap;

import com.example.familymap.cache.DataCache;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import Models.Event;
import Models.Person;
import Request.EventsRequest;
import Request.PersonsRequest;
import Request.RegisterRequest;
import Result.EventsResult;
import Result.PersonsResult;
import Result.RegisterResult;

import static org.junit.Assert.*;

public class DataCacheTest {
    private PersonsResult personsResult;
    private EventsResult eventsResult;
    ServerProxy serverProxy;
    DataCache data;

    @Before
    public void setUp() throws IOException {
        serverProxy = new ServerProxy("localhost", "8080");

        RegisterRequest registerRequest = new RegisterRequest("testUsername4", "testPassword", "testEmail", "testFirstName", "testLastName", "m");
        RegisterResult registerResult = serverProxy.register(registerRequest);

        PersonsRequest personsRequest = new PersonsRequest(registerResult.getAuthtoken());
        EventsRequest eventsRequest = new EventsRequest(registerResult.getAuthtoken());

        personsResult = serverProxy.getPeople(personsRequest);
        eventsResult = serverProxy.getEvents(eventsRequest);

        data = DataCache.getInstance();

        data.setEventsByPersonID(eventsResult.getData());

        data.setPeopleByID(personsResult.getData());

        Person person = data.getPeopleByID().get(registerResult.getPersonID());
        data.setPersonSelected(person);
    }

    @After
    public void tearDown() throws IOException {
        data.ClearDataCache();
        serverProxy.clear();
    }

    @Test
    public void CalculateFamilySuccess() {
        data.setAllFamilyMembers();
        assertNotNull(data.getAllFamilyMembers());
        List<FamilyMember> familyMembers = data.getAllFamilyMembers();
        for (FamilyMember familyMember : familyMembers) {
            assertNotNull(familyMember.person);
        }
    }

    @Test
    public void CalculateFamilyFail() {
        data.ClearDataCache();
        assertNull(data.getAllFamilyMembers());
    }

    @Test
    public void FilterSuccess() {
        data.setMaleEvents(true);
        data.setMaleEvents(true);
        data.setFatherSide(true);
        data.setMotherSide(true);

        String query = "birth";
        List<Event> eventList = data.searchEvents(query);

        assertNotNull(eventList);
        assertEquals(31, eventList.size());
    }

    @Test
    public void FilterSuccess2() {
        data.setMaleEvents(true);
        data.setMaleEvents(false);
        data.setFatherSide(true);
        data.setMotherSide(false);

        String query = "birth";
        List<Event> eventList = data.searchEvents(query);

        assertNotNull(eventList);
        assertEquals(15, eventList.size());
    }

    @Test
    public void SortEventsSuccess() {
        Map<String, SortedSet<Event>> sortedSetMap = data.getEventsByPersonID();
        assertNotNull(sortedSetMap);
        for (Map.Entry<String, SortedSet<Event>> entry : sortedSetMap.entrySet()) {
            List<Event> eventList = new ArrayList<>(entry.getValue());
            assertNotNull(eventList.get(0));
            assertEquals("Birth", eventList.get(0).getEventType());
        }
    }

    @Test
    public void SortEventsNoBirth() {
        Map<String, SortedSet<Event>> sortedSetMap = new HashMap<>();
        SortedSet<Event> sortedSet = new TreeSet<>(data.getEventComparator());
        Event event = new Event("testEvent", "testEvent", 12, 12, "testEvent", "testEvent", "testEvent", 2000);
        sortedSet.add(event);
        sortedSetMap.put("testEvent", sortedSet);

        for (Map.Entry<String, SortedSet<Event>> entry : sortedSetMap.entrySet()) {
            List<Event> eventList = new ArrayList<>(entry.getValue());
            assertNotNull(eventList.get(0));
            assertNotEquals("Birth", eventList.get(0).getEventType());
        }
    }

    @Test
    public void SearchSuccess() {
        String query = "birth";
        List<Event> eventList = data.searchEvents(query);
        List<Person> personList = data.searchPeople(query);

        assertNotNull(eventList);
        assertNotNull(personList);
        assertNotEquals(0, eventList.size());
        assertEquals(0, personList.size());
    }

    @Test
    public void SearchNoResults() {
        String query = "randomString";
        List<Event> eventList = data.searchEvents(query);
        List<Person> personList = data.searchPeople(query);

        assertNotNull(eventList);
        assertNotNull(personList);
        assertEquals(0, eventList.size());
        assertEquals(0, personList.size());
    }

}
