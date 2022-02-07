package com.example.familymap.cache;

import android.graphics.Color;

import com.example.familymap.FamilyMember;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import Models.Event;
import Models.Person;

public class DataCache {
    private static DataCache instance;

    public synchronized static DataCache getInstance() {
        if (instance == null) {
            instance = new DataCache();
        }
        return instance;
    }

    private DataCache() {
    }

    public void ClearDataCache() {
        instance = null;
    }

    //-----INITIAL DATA-----//
    //User data
    private Person user;
    private String authToken;
    private Map<String, Person> peopleByID = new HashMap<>();
    private Map<String, List<Person>> childrenByParentID = new HashMap<>();
    private Map<String, SortedSet<Event>> eventsByPersonID = new HashMap<>();

    //Ancestor data
    private Set<Person> fatherSideMales = new HashSet<>();
    private Set<Person> fatherSideFemales = new HashSet<>();
    private Set<Person> motherSideMales = new HashSet<>();
    private Set<Person> motherSideFemales = new HashSet<>();
    private List<FamilyMember> allFamilyMembers;

    private final Comparator<Event> eventComparator = (o1, o2) -> {
        String event1Type = o1.getEventType().toLowerCase();
        String event2Type = o2.getEventType().toLowerCase();

        switch (event1Type) {
            case "birth":
                if (event2Type.equals("birth")) {
                    return 0;
                } else {
                    return -1;
                }
            case "marriage":
                if (event2Type.equals("birth")) {
                    return 1;
                } else if (event2Type.equals("marriage")) {
                    return 0;
                } else {
                    return -1;
                }
            case "death":
                if (event2Type.equals("death")) {
                    return 0;
                } else {
                    return 1;
                }
            default:
                if (event2Type.equals("birth") || event2Type.equals("marriage") || event2Type.equals("death")) {
                    return 1;
                }
                else {
                    if (Integer.compare(o1.getYear(), o2.getYear()) == 0) {
                        return 1;
                    }
                    else {
                        return Integer.compare(o1.getYear(), o2.getYear());
                    }
                }
        }
    };

    //-----MARKER DATA-----//
    private Map<Marker, Event> markerMap = new HashMap<>();
    private Marker selectedMarker;
    private Map<String, Integer> markerColors = new HashMap<>();
    private List<Polyline> lineList= new ArrayList<>();

    //-----SETTINGS-----//
    //Lines
    private boolean spouseLine = true;
    private boolean familyLine = true;
    private boolean storyLine = true;
    //Filters
    private boolean fatherSide = true;
    private boolean motherSide = true;
    private boolean maleEvents = true;
    private boolean femaleEvents = true;

    //-----ACTION DATA-----//
    private Person personSelected;
    private Event eventSelected;

    //-----METHODS-----//
    public Set<Person> findMales(Person currentPerson, List<Person> personList, Set<Person> malesSet) {
        String fatherID = currentPerson.getFatherID();
        Person father = null;
        for (Person person : personList) {
            if (person.getPersonID().equals(fatherID)) {
                father = person;
            }
        }
        if (father == null) {
            return malesSet;
        }
        malesSet.add(father);

        String spouseId = father.getSpouseID();
        Person spouse = null;
        for (Person person : personList) {
            if (person.getPersonID().equals(spouseId)) {
                spouse = person;
            }
        }

        malesSet.addAll(findMales(father, personList, malesSet));
        malesSet.addAll(findMales(spouse, personList, malesSet));

        return malesSet;
    }

    public Set<Person> findFemales(Person currentPerson, List<Person> personList, Set<Person> femalesSet) {
        String motherID = currentPerson.getMotherID();
        Person mother = null;
        for (Person person : personList) {
            if (person.getPersonID().equals(motherID)) {
                mother = person;
            }
        }
        if (mother == null) {
            return femalesSet;
        }
        femalesSet.add(mother);

        String spouseId = mother.getSpouseID();
        Person spouse = null;
        for (Person person : personList) {
            if (person.getPersonID().equals(spouseId)) {
                spouse = person;
            }
        }

        femalesSet.addAll(findFemales(mother, personList, femalesSet));
        femalesSet.addAll(findFemales(spouse, personList, femalesSet));

        return femalesSet;


    }

    public List<Person> searchPeople(String query) {
        DataCache data = DataCache.getInstance();
        List<Person> people = new ArrayList<>();
        for (Map.Entry<String, Person> entry : data.getPeopleByID().entrySet()) {
            if (entry.getValue().getFirstName().toLowerCase().contains(query)) {
                people.add(entry.getValue());
            }
            else if (entry.getValue().getLastName().toLowerCase().contains(query)) {
                people.add(entry.getValue());
            }
        }
        return people;
    }

    public List<Event> searchEvents(String query) {
        DataCache data = DataCache.getInstance();
        List<Event> events = new ArrayList<>();
        for (Map.Entry<String, SortedSet<Event>> entry : data.getEventsByPersonID().entrySet()) {
            for (Event event : entry.getValue()) {
                Person eventPerson = data.getPeopleByID().get(event.getPersonID());

                //Setting filtering (NEW)
                if (!data.isMaleEvents()) {
                    if(eventPerson.getGender().equals("m")) {
                        break;
                    }
                }
                if (!data.isFemaleEvents()) {
                    if(eventPerson.getGender().equals("f")) {
                        break;
                    }
                }
                if (!data.isFatherSide()) {
                    if (data.getFatherSideMales().contains(eventPerson)) {
                        break;
                    }
                    if (data.getFatherSideFemales().contains(eventPerson)) {
                        break;
                    }
                }
                if (!data.isMotherSide()) {
                    if (data.getMotherSideMales().contains(eventPerson)) {
                        break;
                    }
                    if (data.getMotherSideFemales().contains(eventPerson)) {
                        break;
                    }
                }

                if (event.getCity().toLowerCase().contains(query)) {
                    events.add(event);
                }
                else if (event.getCountry().toLowerCase().contains(query)) {
                    events.add(event);
                }
                else if (event.getEventType().toLowerCase().contains(query)) {
                    events.add(event);
                }
                else if (Integer.toString(event.getYear()).toLowerCase().contains(query)) {
                    events.add(event);
                }
            }

        }
        return events;
    }


    //-----GETTERS AND SETTERS-----//
    public Comparator<Event> getEventComparator() {
        return eventComparator;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public Map<String, Person> getPeopleByID() {
        return peopleByID;
    }

    public void setPeopleByID(Person[] personArray) {
        Map<String, Person> personMap = new HashMap<>();
        for (Person person : personArray) {
            personMap.put(person.getPersonID(), person);
        }
        this.peopleByID = personMap;
    }

    public void setChildrenByParentID(Person[] personArray) {
        Map<String, List<Person>> childrenMap = new HashMap<>();
        for (Person child : personArray) {
            if (childrenMap.containsKey(child.getFatherID())) {
                childrenMap.get(child.getFatherID()).add(child);
            } else {
                childrenMap.put(child.getFatherID(), new ArrayList<Person>());
                childrenMap.get(child.getFatherID()).add(child);
            }
            if (childrenMap.containsKey(child.getMotherID())) {
                childrenMap.get(child.getMotherID()).add(child);
            } else {
                childrenMap.put(child.getMotherID(), new ArrayList<Person>());
                childrenMap.get(child.getMotherID()).add(child);
            }
        }

        this.childrenByParentID = childrenMap;
    }

    public Map<String, SortedSet<Event>> getEventsByPersonID() {
        return eventsByPersonID;
    }

    public void setEventsByPersonID(Event[] eventsArray) {
        Map<String, SortedSet<Event>> eventMap = new HashMap<>();
        for (Event eventToInsert : eventsArray) {
            if (eventMap.containsKey(eventToInsert.getPersonID())) {
                eventMap.get(eventToInsert.getPersonID()).add(eventToInsert);
            } else {
                eventMap.put(eventToInsert.getPersonID(), new TreeSet<>(this.getEventComparator()));
                eventMap.get(eventToInsert.getPersonID()).add(eventToInsert);
            }
        }

        this.eventsByPersonID = eventMap;
    }

    public Person getUser() {
        return user;
    }

    public void setUser(String personID) {
        this.user = this.peopleByID.get(personID);
    }

    public Set<Person> getFatherSideMales() {
        return fatherSideMales;
    }

    public void setFatherSideMales(Person[] personArray) {
        List<Person> personList = Arrays.asList(personArray);
        Set<Person> fatherMalesSet = new HashSet<>();
        String fatherID = user.getFatherID();
        Person father = null;
        for (Person person : personList) {
            if (person.getPersonID().equals(fatherID)) {
                father = person;
            }
        }
        if (father != null) {
            fatherMalesSet.add(father);
            this.fatherSideMales = findMales(father, personList, fatherMalesSet);
        }
    }

    public Set<Person> getFatherSideFemales() {
        return fatherSideFemales;
    }

    public void setFatherSideFemales(Person[] personArray) {
        List<Person> personList = Arrays.asList(personArray);
        Set<Person> fatherFemalesSet = new HashSet<>();
        String fatherID = user.getFatherID();
        Person father = null;
        for (Person person : personList) {
            if (person.getPersonID().equals(fatherID)) {
                father = person;
            }
        }
        if (father != null) {
            this.fatherSideFemales = findFemales(father, personList, fatherFemalesSet);
        }
    }

    public Set<Person> getMotherSideMales() {
        return motherSideMales;
    }

    public void setMotherSideMales(Person[] personArray) {
        List<Person> personList = Arrays.asList(personArray);
        Set<Person> motherMalesSet = new HashSet<>();
        String motherID = user.getMotherID();
        Person mother = null;
        for (Person person : personList) {
            if (person.getPersonID().equals(motherID)) {
                mother = person;
            }
        }
        if (mother != null) {
            this.motherSideMales = findMales(mother, personList, motherMalesSet);
        }
    }

    public Set<Person> getMotherSideFemales() {
        return motherSideFemales;
    }

    public void setMotherSideFemales(Person[] personArray) {
        List<Person> personList = Arrays.asList(personArray);
        Set<Person> motherFemalesSet = new HashSet<>();
        String motherID = user.getMotherID();
        Person mother = null;
        for (Person person : personList) {
            if (person.getPersonID().equals(motherID)) {
                mother = person;
            }
        }
        if (mother != null) {
            motherFemalesSet.add(mother);
            this.motherSideFemales = findFemales(mother, personList, motherFemalesSet);
        }
    }

    public boolean isSpouseLine() {
        return spouseLine;
    }

    public void setSpouseLine(boolean spouseLine) {
        this.spouseLine = spouseLine;
    }

    public boolean isFamilyLine() {
        return familyLine;
    }

    public void setFamilyLine(boolean familyLine) {
        this.familyLine = familyLine;
    }

    public boolean isStoryLine() {
        return storyLine;
    }

    public void setStoryline(boolean storyLine) {
        this.storyLine = storyLine;
    }

    public boolean isFatherSide() {
        return fatherSide;
    }

    public void setFatherSide(boolean fatherSide) {
        this.fatherSide = fatherSide;
    }

    public boolean isMotherSide() {
        return motherSide;
    }

    public void setMotherSide(boolean motherSide) {
        this.motherSide = motherSide;
    }

    public boolean isMaleEvents() {
        return maleEvents;
    }

    public void setMaleEvents(boolean maleEvents) {
        this.maleEvents = maleEvents;
    }

    public boolean isFemaleEvents() {
        return femaleEvents;
    }

    public void setFemaleEvents(boolean femaleEvents) {
        this.femaleEvents = femaleEvents;
    }

    public int getSpouseLineColor() {
        return Color.RED;
    }

    public int getFamilyLineColor() {
        return Color.BLUE;
    }

    public int getStoryLineColor() {
        return Color.GREEN;
    }

    public Map<Marker, Event> getMarkerMap() {
        return markerMap;
    }

    public void setMarkerMap(Map<Marker, Event> markerMap) {
        this.markerMap = markerMap;
    }

    public Marker getSelectedMarker() {
        return selectedMarker;
    }

    public void setSelectedMarker(Marker selectedMarker) {
        this.selectedMarker = selectedMarker;
    }

    public List<Polyline> getLineList() {
        return lineList;
    }

    public void setLineList(List<Polyline> lineList) {
        this.lineList = lineList;
    }

    public Map<String, Integer> getMarkerColors() {
        return markerColors;
    }

    public void setMarkerColors(Event[] eventArray) {
        Map<String, Integer> colorMap = new HashMap<>();
        int i = 1;
        for (Event event : eventArray) {
            String eventType = event.getEventType().toLowerCase();
            if (!colorMap.containsKey(eventType)) {
                colorMap.put(eventType, (i % 360));
                i+= 10;
            }
        }

        this.markerColors = colorMap;
    }

    public Person getPersonSelected() {
        return personSelected;
    }

    public void setPersonSelected(Person personSelected) {
        this.personSelected = personSelected;
    }

    public Event getEventSelected() {
        return eventSelected;
    }

    public void setEventSelected(Event eventSelected) {
        this.eventSelected = eventSelected;
    }

    public List<FamilyMember> getAllFamilyMembers() {
        return allFamilyMembers;
    }

    public void setAllFamilyMembers() {
        DataCache data = DataCache.getInstance();
        Person selectedPerson = data.getPersonSelected();

        List<FamilyMember> allFamMembers = new ArrayList<>();
        Map<String, Person> allPeople = data.getPeopleByID();
        for (Map.Entry<String, Person> entry : allPeople.entrySet()) {
            Person person = entry.getValue();

            if (entry.getValue().getFatherID() != null) {
                if (entry.getValue().getFatherID().equals(selectedPerson.getPersonID())) {
                    allFamMembers.add(new FamilyMember("Child", entry.getValue()));
                }
            }
            if (entry.getValue().getMotherID() != null) {
                if (entry.getValue().getMotherID().equals(selectedPerson.getPersonID())) {
                    allFamMembers.add(new FamilyMember("Child", entry.getValue()));
                }
            }
            if (selectedPerson.getFatherID() != null) {
                if (entry.getValue().getPersonID().equals(selectedPerson.getFatherID())) {
                    allFamMembers.add(new FamilyMember("Father", entry.getValue()));
                }
            }
            if (selectedPerson.getMotherID() != null) {
                if (entry.getValue().getPersonID().equals(selectedPerson.getMotherID())) {
                    allFamMembers.add(new FamilyMember("Mother", entry.getValue()));
                }
            }
            if (selectedPerson.getSpouseID() != null) {
                if (entry.getValue().getPersonID().equals(selectedPerson.getSpouseID())) {
                    allFamMembers.add(new FamilyMember("Spouse", entry.getValue()));
                }
            }
        }
        this.allFamilyMembers = allFamMembers;
    }

}