package com.example.familymap.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.familymap.R;
import com.example.familymap.activities.PersonActivity;
import com.example.familymap.activities.SearchActivity;
import com.example.familymap.activities.SettingsActivity;
import com.example.familymap.cache.DataCache;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import Models.Event;
import Models.Person;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap map;
    private View view;
    private boolean isEventActivity;

    public MapFragment() {

    }
    public MapFragment(boolean isEventActivity) {
        this.isEventActivity = isEventActivity;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Iconify.with(new FontAwesomeModule());
        this.view = inflater.inflate(R.layout.fragment_map, container, false);

        setHasOptionsMenu(!isEventActivity);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        LinearLayout infoDisplay = view.findViewById(R.id.eventInfo);
        infoDisplay.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PersonActivity.class);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (map != null) {
            map.clear();
            addAllMarkers();
            addLines();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        DataCache data = DataCache.getInstance();

        map = googleMap;
        map.clear();

        addAllMarkers();
        addLines();
        map.animateCamera(CameraUpdateFactory.newLatLng(data.getSelectedMarker().getPosition()));
        setInfoWindow();

        map.setOnMarkerClickListener(marker -> {
            data.setSelectedMarker(marker);
            data.setEventSelected(data.getMarkerMap().get(marker));
            Event event = data.getEventSelected();
            data.setPersonSelected(data.getPeopleByID().get(event.getPersonID()));
            Person person = data.getPersonSelected();

            //Set info text fields
            TextView nameText = view.findViewById(R.id.personName);
            String name = person.getFirstName() + " " + person.getLastName();
            nameText.setText(name);

            TextView typeDateText = view.findViewById(R.id.eventTypeAndDate);
            String typeDate = event.getEventType() + "(" + event.getYear() + ")";
            typeDateText.setText(typeDate);

            TextView locationText = view.findViewById(R.id.eventLocation);
            String location = event.getCity() + ", " + event.getCountry();
            locationText.setText(location);

            if (person.getGender().equals("m")) {
                ImageView genderIcon = view.findViewById(R.id.mapGenderIcon);
                genderIcon.setImageDrawable(new IconDrawable(getActivity(), FontAwesomeIcons.fa_male)
                        .colorRes(R.color.blue)
                        .actionBarSize());
            } else {
                ImageView genderIcon = view.findViewById(R.id.mapGenderIcon);
                genderIcon.setImageDrawable(new IconDrawable(getActivity(), FontAwesomeIcons.fa_female)
                        .colorRes(R.color.pink)
                        .actionBarSize());
            }

            addLines();

            return true;
        });

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.options_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.searchMenuItem);
        searchItem.setIcon(new IconDrawable(getActivity(), FontAwesomeIcons.fa_search)
                .colorRes(R.color.white)
                .actionBarSize());

        MenuItem settingsItem = menu.findItem(R.id.settingsMenuItem);
        settingsItem.setIcon(new IconDrawable(getActivity(), FontAwesomeIcons.fa_gear)
                .colorRes(R.color.white)
                .actionBarSize());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {
        Intent intent;
        switch(menu.getItemId()) {
            case R.id.searchMenuItem:
                intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                return true;
            case R.id.settingsMenuItem:
                intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(menu);
        }
    }

    private void setInfoWindow() {
        DataCache data = DataCache.getInstance();
        Event selectedEvent = data.getEventSelected();

        Person person = data.getPeopleByID().get(selectedEvent.getPersonID());

        //Set info text fields
        TextView nameText = view.findViewById(R.id.personName);
        String name = person.getFirstName() + " " + person.getLastName();
        nameText.setText(name);

        TextView typeDateText = view.findViewById(R.id.eventTypeAndDate);
        String typeDate = selectedEvent.getEventType() + "(" + selectedEvent.getYear() + ")";
        typeDateText.setText(typeDate);

        TextView locationText = view.findViewById(R.id.eventLocation);
        String location = selectedEvent.getCity() + ", " + selectedEvent.getCountry();
        locationText.setText(location);

        if (person.getGender().equals("m")) {
            ImageView genderIcon = view.findViewById(R.id.mapGenderIcon);
            genderIcon.setImageDrawable(new IconDrawable(getActivity(), FontAwesomeIcons.fa_male)
                    .colorRes(R.color.blue)
                    .actionBarSize());
        } else {
            ImageView genderIcon = view.findViewById(R.id.mapGenderIcon);
            genderIcon.setImageDrawable(new IconDrawable(getActivity(), FontAwesomeIcons.fa_female)
                    .colorRes(R.color.pink)
                    .actionBarSize());
        }
        data.setPersonSelected(person);
    }

    private void addAllMarkers() {
        DataCache data = DataCache.getInstance();
        data.setMarkerMap(new HashMap<>());

        for (Map.Entry<String, SortedSet<Event>> entry : data.getEventsByPersonID().entrySet()) {
            for (Event event : entry.getValue()) {
                Person eventPerson = data.getPeopleByID().get(event.getPersonID());

                //Setting filtering
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

                LatLng position = new LatLng(event.getLatitude(), event.getLongitude());
                Marker marker;
                String eventType = event.getEventType().toLowerCase();
                marker = map.addMarker(new MarkerOptions().position(position)
                        .title(event.getEventType())
                        .icon(BitmapDescriptorFactory.defaultMarker(data.getMarkerColors().get(eventType))));

                data.getMarkerMap().put(marker, event);

                if (event.getPersonID().equals(data.getEventSelected().getPersonID())) {
                    if (event.getEventType().equals(data.getEventSelected().getEventType())) {
                        data.setEventSelected(event);
                        data.setSelectedMarker(marker);
                    }
                }
            }
        }
    }

    private void addLines() {
        DataCache data = DataCache.getInstance();

        //Remove all existing lines
        for (Polyline line : data.getLineList()) {
            line.remove();
        }
        data.setLineList(new ArrayList<>());

        Event currentEvent = data.getMarkerMap().get(data.getSelectedMarker());
        if (currentEvent == null) {
            return;
        }

        Person currentPerson = data.getPeopleByID().get(currentEvent.getPersonID());

        if (data.isSpouseLine()) {
            Person spouse = data.getPeopleByID().get(currentPerson.getSpouseID());
            if (spouse != null) {
//                if (data.isMaleEvents() && spouse.getGender().equals("m")) {
//                    SortedSet<Event> spouseEvents = data.getEventsByPersonID().get(currentPerson.getSpouseID());
//                    Event firstEvent = spouseEvents.iterator().next();
//
//                    Polyline newLine = map.addPolyline(new PolylineOptions()
//                            .add(new LatLng(firstEvent.getLatitude(), firstEvent.getLongitude()), new LatLng(currentEvent.getLatitude(), currentEvent.getLongitude()))
//                            .color(data.getSpouseLineColor()));
//
//                    data.getLineList().add(newLine);
//                }
//                if (data.isFemaleEvents() && spouse.getGender().equals("f")) {
//                    SortedSet<Event> spouseEvents = data.getEventsByPersonID().get(currentPerson.getSpouseID());
//                    Event firstEvent = spouseEvents.iterator().next();
//
//                    Polyline newLine = map.addPolyline(new PolylineOptions()
//                            .add(new LatLng(firstEvent.getLatitude(), firstEvent.getLongitude()), new LatLng(currentEvent.getLatitude(), currentEvent.getLongitude()))
//                            .color(data.getSpouseLineColor()));
//
//                    data.getLineList().add(newLine);
//                }

                SortedSet<Event> spouseEvents = data.getEventsByPersonID().get(currentPerson.getSpouseID());
                Event firstEvent = spouseEvents.iterator().next();

                Polyline newLine = map.addPolyline(new PolylineOptions()
                        .add(new LatLng(firstEvent.getLatitude(), firstEvent.getLongitude()), new LatLng(currentEvent.getLatitude(), currentEvent.getLongitude()))
                        .color(data.getSpouseLineColor()));

                data.getLineList().add(newLine);

            }
        }
        if (data.isFamilyLine()) {
            if (currentPerson.getFatherID() != null) {
                fatherLine(currentPerson, currentEvent, 10);
            }
            if (currentPerson.getMotherID() != null) {
                motherLine(currentPerson, currentEvent, 10);
            }
        }
        if (data.isStoryLine()){
            SortedSet<Event> personEvents = data.getEventsByPersonID().get(currentPerson.getPersonID());
            List<Event> personEventList = new ArrayList<>(personEvents);
            for (int i = 1; i < personEventList.size(); i++) {
                Polyline newLine = map.addPolyline(new PolylineOptions()
                    .add(new LatLng(personEventList.get(i).getLatitude(), personEventList.get(i).getLongitude()), new LatLng(personEventList.get(i-1).getLatitude(), personEventList.get(i-1).getLongitude()))
                    .color(data.getStoryLineColor()));

                data.getLineList().add(newLine);
            }

        }
    }

    private void fatherLine(Person currentPerson, Event currentEvent, int lineWidth) {
        DataCache data = DataCache.getInstance();

        if (data.getPeopleByID().get(currentPerson.getFatherID()) != null) {
            SortedSet<Event> fatherEvents = data.getEventsByPersonID().get(currentPerson.getFatherID());
            Event firstEvent = fatherEvents.iterator().next();

            Polyline newLine = map.addPolyline(new PolylineOptions()
                    .add(new LatLng(firstEvent.getLatitude(), firstEvent.getLongitude()), new LatLng(currentEvent.getLatitude(), currentEvent.getLongitude()))
                    .color(data.getFamilyLineColor())
                    .width(lineWidth));

            data.getLineList().add(newLine);

            Person father = data.getPeopleByID().get(currentPerson.getFatherID());
            Person mother = data.getPeopleByID().get(currentPerson.getMotherID());
            fatherLine(father, firstEvent, lineWidth / 2);
            motherLine(mother, firstEvent, lineWidth / 2);
        }
    }

    private void motherLine(Person currentPerson, Event currentEvent, int lineWidth) {
        DataCache data = DataCache.getInstance();

        if (data.getPeopleByID().get(currentPerson.getMotherID()) != null) {
            SortedSet<Event> motherEvents = data.getEventsByPersonID().get(currentPerson.getMotherID());
            Event firstEvent = motherEvents.iterator().next();

            Polyline newLine = map.addPolyline(new PolylineOptions()
                    .add(new LatLng(firstEvent.getLatitude(), firstEvent.getLongitude()), new LatLng(currentEvent.getLatitude(), currentEvent.getLongitude()))
                    .color(data.getFamilyLineColor())
                    .width(lineWidth));

            data.getLineList().add(newLine);

            Person father = data.getPeopleByID().get(currentPerson.getFatherID());
            Person mother = data.getPeopleByID().get(currentPerson.getMotherID());
            fatherLine(father, firstEvent, lineWidth / 2);
            motherLine(mother, firstEvent, lineWidth / 2);
        }
    }
}