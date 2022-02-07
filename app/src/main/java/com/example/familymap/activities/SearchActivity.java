package com.example.familymap.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.familymap.R;
import com.example.familymap.cache.DataCache;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import Models.Event;
import Models.Person;

public class SearchActivity extends AppCompatActivity {
    private static final int PERSON_GROUP_POSITION = 0;
    private static final int EVENTS_GROUP_POSITION = 1;
    private RecyclerView recyclerView;
    private SearchResultsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setTitle("Search");

        DataCache data = DataCache.getInstance();

        SearchView searchView = findViewById(R.id.search_view);
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Type Search Here");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                List<Event> searchEvents = data.searchEvents(query);
                List<Person> searchPeople = data.searchPeople(query);

                adapter = new SearchResultsAdapter(searchEvents, searchPeople);
                recyclerView.setAdapter(adapter);

                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        recyclerView = findViewById(R.id.search_results);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

    }

    public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsViewHolder> {
        private final List<Event> events;
        private final List<Person> people;

        public SearchResultsAdapter(List<Event> events, List<Person> people) {
            this.events = events;
            this.people = people;
        }

        @Override
        public int getItemViewType(int position) {
            return position < people.size() ? PERSON_GROUP_POSITION : EVENTS_GROUP_POSITION;
        }

        @NonNull
        @Override
        public SearchResultsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;

            if (viewType == PERSON_GROUP_POSITION) {
                view = getLayoutInflater().inflate(R.layout.person_items, parent, false);
            }
            else {
                view = getLayoutInflater().inflate(R.layout.event_items, parent, false);
            }

            return new SearchResultsViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchActivity.SearchResultsViewHolder holder, int position) {
            if (position < people.size()) {
                holder.bind(people.get(position));
            }
            else {
                 holder.bind(events.get(position - people.size()));
            }
        }

        @Override
        public int getItemCount() {
            return people.size() + events.size();
        }
    }

    public class SearchResultsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView eventIcon;
        private TextView eventTopLine;
        private TextView eventBottomLine;
        private ImageView personIcon;
        private TextView personTopLine;

        private final int viewType;
        private Event event;
        private Person person;

        public SearchResultsViewHolder(View view, int viewType) {
            super(view);
            this.viewType = viewType;

            itemView.setOnClickListener(this);

            if (viewType == EVENTS_GROUP_POSITION) {
                eventIcon = itemView.findViewById(R.id.icon);
                eventTopLine = itemView.findViewById(R.id.topEventLine);
                eventBottomLine = itemView.findViewById(R.id.bottomEventLine);
            }
            else {
                personIcon = itemView.findViewById(R.id.genderIcon);
                personTopLine = itemView.findViewById(R.id.topFamilyLine);
            }
        }

        private void bind(Event event) {
            DataCache data = DataCache.getInstance();
            this.event = event;
            Person person = data.getPeopleByID().get(event.getPersonID());

            this.eventIcon.setImageDrawable(new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_map_marker)
                    .colorRes(R.color.black)
                    .actionBarSize());
            String eventText1 = event.getEventType() + ": " + event.getCity() + ", " + event.getCountry();
            this.eventTopLine.setText(eventText1);
            String eventText2 = person.getFirstName() + " " + person.getLastName();
            this.eventBottomLine.setText(eventText2);
        }

        private void bind(Person person) {
            this.person = person;

            if (person.getGender().equals("m")) {
                this.personIcon.setImageDrawable(new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_male)
                        .colorRes(R.color.blue)
                        .actionBarSize());
            }
            else {
                this.personIcon.setImageDrawable(new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_female)
                        .colorRes(R.color.pink)
                        .actionBarSize());
            }
            String personText = person.getFirstName() + " " + person.getLastName();
            this.personTopLine.setText(personText);
        }

        @Override
        public void onClick(View v) {
            DataCache data = DataCache.getInstance();

            Intent intent;
            if (viewType == EVENTS_GROUP_POSITION) {
                intent = new Intent(SearchActivity.this, EventActivity.class);
                data.setEventSelected(event);
            }
            else {
                intent = new Intent(SearchActivity.this, PersonActivity.class);
                data.setPersonSelected(person);
            }
            startActivity(intent);

        }
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
}