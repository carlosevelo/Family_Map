package com.example.familymap.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.familymap.FamilyMember;
import com.example.familymap.R;
import com.example.familymap.cache.DataCache;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Models.Event;
import Models.Person;

public class PersonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataCache data = DataCache.getInstance();
        setContentView(R.layout.activity_person);
        getSupportActionBar().setTitle("Person Info");

        Person selectedPerson = data.getPersonSelected();
        //Populates all the data in data cache
        data.setAllFamilyMembers();
        List<Event> eventsList = new ArrayList<>(data.getEventsByPersonID().get(selectedPerson.getPersonID()));

        //Removes death event and puts it at the end
        int deathIndex;
        for (Event event : eventsList) {
            Person person = data.getPeopleByID().get(event.getPersonID());

            //Setting filtering (NEW)
            if (!data.isMaleEvents()) {
                if(person.getGender().equals("m")) {
                    eventsList.remove(event);
                    break;
                }
            }
            if (!data.isFemaleEvents()) {
                if(person.getGender().equals("f")) {
                    eventsList.remove(event);
                    break;
                }
            }
            if (!data.isFatherSide()) {
                if (data.getFatherSideMales().contains(person)) {
                    eventsList.remove(event);
                    break;
                }
                if (data.getFatherSideFemales().contains(person)) {
                    eventsList.remove(event);
                    break;
                }
            }
            if (!data.isMotherSide()) {
                if (data.getMotherSideMales().contains(person)) {
                    eventsList.remove(event);
                    break;
                }
                if (data.getMotherSideFemales().contains(person)) {
                    eventsList.remove(event);
                    break;
                }
            }

            if (event.getEventType().equals("Death")) {
                deathIndex = eventsList.indexOf(event);
                eventsList.remove(deathIndex);
                eventsList.add(event);
            }

        }

        List<FamilyMember> allFamilyMembers = data.getAllFamilyMembers();

        TextView firstName = findViewById(R.id.firstNameValue);
        firstName.setText(selectedPerson.getFirstName());
        TextView lastName = findViewById(R.id.lastNameValue);
        lastName.setText(selectedPerson.getLastName());
        TextView gender = findViewById(R.id.genderValue);
        if (selectedPerson.getGender().equals("m")) {
            gender.setText("Male");
        }
        else {
            gender.setText("Female");
        }

        ExpandableListView lifeList = findViewById(R.id.expandable_lists);
        lifeList.setAdapter(new ExpandableListAdapter(eventsList, allFamilyMembers));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
    }



    public class ExpandableListAdapter extends BaseExpandableListAdapter {
        private static final int LIFE_EVENTS_GROUP_POSITION = 0;
        private static final int FAMILY_GROUP_POSITION = 1;

        private final DataCache data = DataCache.getInstance();
        private final List<Event> personEvents;
        private final List<FamilyMember> familyMembers;

        ExpandableListAdapter(List<Event> personEvents, List<FamilyMember> familyMembers) {
            this.personEvents = personEvents;
            this.familyMembers = familyMembers;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            switch (groupPosition) {
                case LIFE_EVENTS_GROUP_POSITION:
                    return personEvents.size();
                case FAMILY_GROUP_POSITION:
                    return familyMembers.size();
                default:
                    throw new IllegalArgumentException("Unrecognized group position.");
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            switch (groupPosition) {
                case LIFE_EVENTS_GROUP_POSITION:
                    return "Life Events";
                case FAMILY_GROUP_POSITION:
                    return "Family Members";
                default:
                    throw new IllegalArgumentException("Unrecognized group position.");
            }
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            switch (groupPosition) {
                case LIFE_EVENTS_GROUP_POSITION:
                    return personEvents.get(childPosition);
                case FAMILY_GROUP_POSITION:
                    return familyMembers.get(childPosition);
                default:
                    throw new IllegalArgumentException("Unrecognized group position.");
            }
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_group, parent, false);
            }

            TextView titleView = convertView.findViewById(R.id.listTitle);

            switch (groupPosition) {
                case LIFE_EVENTS_GROUP_POSITION:
                    titleView.setText("Life Events");
                    break;
                case FAMILY_GROUP_POSITION:
                    titleView.setText("Family Members");
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position.");
            }

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View itemView;

            switch (groupPosition) {
                case LIFE_EVENTS_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.event_items, parent, false);

                    Event personEvent = personEvents.get(childPosition);
                    Person eventPerson = data.getPeopleByID().get(personEvent.getPersonID());

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

                    ImageView eventIcon = itemView.findViewById(R.id.icon);
                    eventIcon.setImageDrawable(new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_map_marker)
                            .colorRes(R.color.black)
                            .actionBarSize());

                    TextView topEventLine = itemView.findViewById(R.id.topEventLine);
                    String life1Text = personEvents.get(childPosition).getEventType() + ": " + personEvents.get(childPosition).getCity() + ", " + personEvents.get(childPosition).getCountry();
                    topEventLine.setText(life1Text);

                    TextView bottomEventLine = itemView.findViewById(R.id.bottomEventLine);
                    String life2Text = data.getPersonSelected().getFirstName() + " " + data.getPersonSelected().getLastName();
                    bottomEventLine.setText(life2Text);

                    itemView.setOnClickListener(v -> {
                        Intent intent = new Intent(PersonActivity.this, EventActivity.class);
                        data.setEventSelected(personEvents.get(childPosition));
                        startActivity(intent);
                    });
                    break;

                case FAMILY_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.person_items, parent, false);

                    ImageView genderIcon = itemView.findViewById(R.id.genderIcon);
                    if (familyMembers.get(childPosition).person.getGender().equals("m")) {
                        genderIcon.setImageDrawable(new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_male)
                                .colorRes(R.color.blue)
                                .actionBarSize());
                    }
                    else {
                        genderIcon.setImageDrawable(new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_female)
                                .colorRes(R.color.pink)
                                .actionBarSize());
                    }

                    TextView topFamilyLine = itemView.findViewById(R.id.topFamilyLine);
                    String family1Text = familyMembers.get(childPosition).person.getFirstName() + " " + familyMembers.get(childPosition).person.getLastName();
                    topFamilyLine.setText(family1Text);

                    TextView bottomFamilyLine = itemView.findViewById(R.id.bottomFamilyLine);
                    String family2Text = familyMembers.get(childPosition).relationship;
                    bottomFamilyLine.setText(family2Text);

                    itemView.setOnClickListener(v -> {
                        Intent intent = new Intent(PersonActivity.this, PersonActivity.class);
                        data.setPersonSelected(familyMembers.get(childPosition).person);
                        startActivity(intent);
                    });
                    break;

                default:
                    throw new IllegalArgumentException("Unrecognized group position.");
            }

            return itemView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public void onGroupExpanded(int groupPosition) {

        }

        @Override
        public void onGroupCollapsed(int groupPosition) {

        }

        @Override
        public long getCombinedChildId(long groupId, long childId) {
            return 0;
        }

        @Override
        public long getCombinedGroupId(long groupId) {
            return 0;
        }
    }
}