package com.example.familymap;

import Models.Person;

public class FamilyMember {
    public String relationship;
    public Person person;

    public FamilyMember(String relationship, Person person) {
        this.relationship = relationship;
        this.person = person;
    }
}
