package com.example.familymap.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.familymap.R;
import com.example.familymap.ServerProxy;
import com.example.familymap.cache.DataCache;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Models.Event;
import Request.EventsRequest;
import Request.LoginRequest;
import Request.PersonsRequest;
import Request.RegisterRequest;
import Result.EventsResult;
import Result.LoginResult;
import Result.PersonsResult;
import Result.RegisterResult;

public class LoginFragment extends Fragment {

    private LoginFragmentViewModel getViewModel () {
        return new ViewModelProvider(this).get(LoginFragmentViewModel.class);
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        //Gets the model data for the login fragment
        LoginFragmentViewModel model = getViewModel();
        //Sets model data members to entries in the view fields
        model.setmHostField(view.findViewById(R.id.serverHostValue));
        model.setmPortField(view.findViewById(R.id.serverPortValue));
        model.setmUsername(view.findViewById(R.id.userNameValue));
        model.setmPassword(view.findViewById(R.id.passwordValue));
        model.setmFirstName(view.findViewById(R.id.firstNameValue));
        model.setmLastName(view.findViewById(R.id.lastNameValue));
        model.setmEmail(view.findViewById(R.id.emailValue));
        model.setmGender(view.findViewById(R.id.genderRadioGroup));
        model.setmLoginButton(view.findViewById(R.id.button_login));
        model.setmRegisterButton(view.findViewById(R.id.button_register));

        //Sets text listeners to each input field
        model.getmGender().setOnCheckedChangeListener((group, checkedId) -> {
            String userNameField = ((EditText) view.findViewById(R.id.userNameValue)).getText().toString();
            String passwordField = ((EditText) view.findViewById(R.id.passwordValue)).getText().toString();
            String firstNameField = ((EditText) view.findViewById(R.id.firstNameValue)).getText().toString();
            String lastNameField = ((EditText) view.findViewById(R.id.lastNameValue)).getText().toString();
            String emailField = ((EditText) view.findViewById(R.id.emailValue)).getText().toString();
            int buttonID = group.getCheckedRadioButtonId();
            Button registerBtn = view.findViewById(R.id.button_register);

            if (buttonID == R.id.radio_male) {
                model.setGender("m");
            }
            else if (buttonID == R.id.radio_female) {
                model.setGender("f");
            }

            registerBtn.setEnabled((!passwordField.equals("")) && (!userNameField.equals(""))
                    && (!firstNameField.equals("")) && (!lastNameField.equals(""))
                    && (!emailField.equals("")) && (buttonID != -1));

        });
        model.getmUsername().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String userNameField = ((EditText) view.findViewById(R.id.userNameValue)).getText().toString();
                String passwordField = ((EditText) view.findViewById(R.id.passwordValue)).getText().toString();
                String firstNameField = ((EditText) view.findViewById(R.id.firstNameValue)).getText().toString();
                String lastNameField = ((EditText) view.findViewById(R.id.lastNameValue)).getText().toString();
                String emailField = ((EditText) view.findViewById(R.id.emailValue)).getText().toString();
                int genderRadio = ((RadioGroup) view.findViewById(R.id.genderRadioGroup)).getCheckedRadioButtonId();
                Button loginBtn = view.findViewById(R.id.button_login);
                Button registerBtn = view.findViewById(R.id.button_register);

                loginBtn.setEnabled((!passwordField.equals("")) && (!userNameField.equals("")));

                registerBtn.setEnabled((!passwordField.equals("")) && (!userNameField.equals(""))
                        && (!firstNameField.equals("")) && (!lastNameField.equals(""))
                        && (!emailField.equals("")) && (genderRadio != -1));
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        model.getmPassword().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String userNameField = ((EditText) view.findViewById(R.id.userNameValue)).getText().toString();
                String passwordField = ((EditText) view.findViewById(R.id.passwordValue)).getText().toString();
                String firstNameField = ((EditText) view.findViewById(R.id.firstNameValue)).getText().toString();
                String lastNameField = ((EditText) view.findViewById(R.id.lastNameValue)).getText().toString();
                String emailField = ((EditText) view.findViewById(R.id.emailValue)).getText().toString();
                int genderRadio = ((RadioGroup) view.findViewById(R.id.genderRadioGroup)).getCheckedRadioButtonId();
                Button loginBtn = view.findViewById(R.id.button_login);
                Button registerBtn = view.findViewById(R.id.button_register);

                loginBtn.setEnabled((!passwordField.equals("")) && (!userNameField.equals("")));

                registerBtn.setEnabled((!passwordField.equals("")) && (!userNameField.equals(""))
                        && (!firstNameField.equals("")) && (!lastNameField.equals(""))
                        && (!emailField.equals("")) && (genderRadio != -1));
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        model.getmFirstName().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String userNameField = ((EditText) view.findViewById(R.id.userNameValue)).getText().toString();
                String passwordField = ((EditText) view.findViewById(R.id.passwordValue)).getText().toString();
                String firstNameField = ((EditText) view.findViewById(R.id.firstNameValue)).getText().toString();
                String lastNameField = ((EditText) view.findViewById(R.id.lastNameValue)).getText().toString();
                String emailField = ((EditText) view.findViewById(R.id.emailValue)).getText().toString();
                int genderRadio = ((RadioGroup) view.findViewById(R.id.genderRadioGroup)).getCheckedRadioButtonId();
                Button registerBtn = view.findViewById(R.id.button_register);

                registerBtn.setEnabled((!passwordField.equals("")) && (!userNameField.equals(""))
                        && (!firstNameField.equals("")) && (!lastNameField.equals(""))
                        && (!emailField.equals("")) && (genderRadio != -1));
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        model.getmLastName().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String userNameField = ((EditText) view.findViewById(R.id.userNameValue)).getText().toString();
                String passwordField = ((EditText) view.findViewById(R.id.passwordValue)).getText().toString();
                String firstNameField = ((EditText) view.findViewById(R.id.firstNameValue)).getText().toString();
                String lastNameField = ((EditText) view.findViewById(R.id.lastNameValue)).getText().toString();
                String emailField = ((EditText) view.findViewById(R.id.emailValue)).getText().toString();
                int genderRadio = ((RadioGroup) view.findViewById(R.id.genderRadioGroup)).getCheckedRadioButtonId();
                Button registerBtn = view.findViewById(R.id.button_register);

                registerBtn.setEnabled((!passwordField.equals("")) && (!userNameField.equals(""))
                        && (!firstNameField.equals("")) && (!lastNameField.equals(""))
                        && (!emailField.equals("")) && (genderRadio != -1));
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        model.getmEmail().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String userNameField = ((EditText) view.findViewById(R.id.userNameValue)).getText().toString();
                String passwordField = ((EditText) view.findViewById(R.id.passwordValue)).getText().toString();
                String firstNameField = ((EditText) view.findViewById(R.id.firstNameValue)).getText().toString();
                String lastNameField = ((EditText) view.findViewById(R.id.lastNameValue)).getText().toString();
                String emailField = ((EditText) view.findViewById(R.id.emailValue)).getText().toString();
                int genderRadio = ((RadioGroup) view.findViewById(R.id.genderRadioGroup)).getCheckedRadioButtonId();
                Button registerBtn = view.findViewById(R.id.button_register);

                registerBtn.setEnabled((!passwordField.equals("")) && (!userNameField.equals(""))
                        && (!firstNameField.equals("")) && (!lastNameField.equals(""))
                        && (!emailField.equals(""))&& (genderRadio != -1));
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Sets a click listener to the login button
        model.getmLoginButton().setOnClickListener(v -> {
            //Creates a handler to run a background task
            @SuppressLint("HandlerLeak") Handler uiThreadMessageHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    Bundle bundle = msg.getData();
                    boolean loginMessage = bundle.getBoolean("loginSuccess");

                    if (loginMessage) {
                        Handler dataLoadMessageHandler = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                Bundle bundle = msg.getData();
                                boolean dataMessage = bundle.getBoolean("dataSuccess");

                                if (dataMessage) {
                                    Toast.makeText(getActivity(),
                                            DataCache.getInstance().getUser().getFirstName() + " " + DataCache.getInstance().getUser().getLastName() + " logged in",
                                            Toast.LENGTH_SHORT).show();
                                }
                               else {
                                    Toast.makeText(getActivity(),
                                       "Data Load error.",
                                         Toast.LENGTH_SHORT).show();
                                }
                            }
                        };

                        GetDataTask getDataTask = new GetDataTask(model.getmHostField().getText().toString(),model.getmPortField().getText().toString(), dataLoadMessageHandler);
                        ExecutorService executor = Executors.newSingleThreadExecutor();
                        executor.submit(getDataTask);
                    }
                    else {
                        Toast.makeText(getActivity(),
                                model.getLoginMessage(),
                                Toast.LENGTH_SHORT).show();
                    }

                }
            };

            //Calls a background task
            LoginTask loginTask = new LoginTask(model.getmHostField().getText().toString(), model.getmPortField().getText().toString(), uiThreadMessageHandler);
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(loginTask);

        });
        //Sets a click listener to the register button
        model.getmRegisterButton().setOnClickListener(v -> {
            //Creates a handler to run a background task
            @SuppressLint("HandlerLeak") Handler uiThreadMessageHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    Bundle bundle = msg.getData();
                    boolean registerMessage = bundle.getBoolean("registerSuccess");

                    if (registerMessage) {
                        Handler dataLoadMessageHandler = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                Bundle bundle = msg.getData();
                                boolean dataMessage = bundle.getBoolean("dataSuccess");

                                if (dataMessage) {
                                    Toast.makeText(getActivity(),
                                            DataCache.getInstance().getUser().getFirstName() + " " + DataCache.getInstance().getUser().getLastName() + " logged in",
                                            Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(getActivity(),
                                            "Data Load error.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        };

                        GetDataTask getDataTask = new GetDataTask(model.getmHostField().getText().toString(),model.getmPortField().getText().toString(), dataLoadMessageHandler);
                        ExecutorService executor = Executors.newSingleThreadExecutor();
                        executor.submit(getDataTask);

                    }
                    else {
                        Toast.makeText(getContext(),
                                model.getLoginMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            };
            //Calls a background task
            RegisterTask registerTask = new RegisterTask(model.getmHostField().getText().toString(), model.getmPortField().getText().toString(), uiThreadMessageHandler);
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(registerTask);

        });

        return view;
    }

    public class LoginTask implements Runnable {
        private final String host;
        private final String port;
        private final Handler msgHandler;

        public LoginTask(String host, String port, Handler msgHandler) {
            this.host = host;
            this.port = port;
            this.msgHandler = msgHandler;
        }

        @Override
        public void run() {
            //Gets the model for the login fragment
            LoginFragmentViewModel model = getViewModel();
            //Creates a LoginRequest object from the model data
            String username = model.getmUsername().getText().toString();
            String password = model.getmPassword().getText().toString();
            LoginRequest req = new LoginRequest(username, password);
            //Creates a new ServerProxy object to send request to the server
            ServerProxy serverProxy = new ServerProxy(host, port);

            LoginResult res = null;
            //Attempts to login
            try {
                res = serverProxy.login(req);
            } catch (IOException e) {
                model.setLoginSuccess(false);
                e.printStackTrace();
            }

            //------Is this how to do it?-----//
            if (res != null) {
                if (res.getAuthtoken() == null) {
                    model.setLoginSuccess(false);
                    model.setLoginMessage(res.getMessage());
                }
                else {
                    model.setLoginSuccess(true);
                    model.setAuthToken(res.getAuthtoken());
                    model.setUsername(res.getUsername());
                    model.setPersonID(res.getPersonID());
                }
            }

            sendMessage(model.getLoginSuccess());
        }

        private void sendMessage (Boolean loginSuccess) {
            Message msg = Message.obtain();
            Bundle msgBundle = new Bundle();
            msgBundle.putBoolean("loginSuccess", loginSuccess);
            msg.setData(msgBundle);
            msgHandler.sendMessage(msg);
        }
    }

    public class RegisterTask implements Runnable {
        private final String host;
        private final String port;
        private final Handler msgHandler;

        public RegisterTask(String host, String port, Handler msgHandler) {
            this.host = host;
            this.port = port;
            this.msgHandler = msgHandler;
        }

        @Override
        public void run() {
            //Gets the model for the login fragment
            LoginFragmentViewModel model = getViewModel();
            //Creates a RegisterRequest object from the model data
            RegisterRequest req = new RegisterRequest(model.getmUsername().getText().toString(), model.getmPassword().getText().toString(),
                    model.getmEmail().getText().toString(), model.getmFirstName().getText().toString(), model.getmLastName().getText().toString(),
                    model.getGender());
            //Creates a new ServerProxy object to send request to the server
            ServerProxy serverProxy = new ServerProxy(host, port);

            RegisterResult res = null;
            //Attempts to register
            try {
                res = serverProxy.register(req);
            } catch (IOException e) {
                model.setLoginSuccess(false);
                e.printStackTrace();
            }


            //-----Is this how to do it?------//
            if (res != null) {
                if (res.getAuthtoken() == null) {
                    model.setLoginSuccess(false);
                    model.setLoginMessage(res.getMessage());
                } else {
                    model.setLoginSuccess(true);
                    model.setAuthToken(res.getAuthtoken());
                    model.setUsername(res.getUsername());
                    model.setPersonID(res.getPersonID());
                }
            }

            sendMessage(model.getLoginSuccess());
        }

        private void sendMessage (Boolean registerSuccess) {
            Message msg = Message.obtain();
            Bundle msgBundle = new Bundle();
            msgBundle.putBoolean("registerSuccess", registerSuccess);
            msg.setData(msgBundle);
            msgHandler.sendMessage(msg);
        }
    }

    public class GetDataTask implements Runnable {
        private final String host;
        private final String port;
        private final Handler msgHandler;

        public GetDataTask(String host, String port, Handler msgHandler) {
            this.host = host;
            this.port = port;
            this.msgHandler = msgHandler;
        }

        @Override
        public void run() {
            //Gets the model for the login fragment
            LoginFragmentViewModel model = getViewModel();
            //Creates a PersonsRequest object from the model data
            PersonsRequest req1 = new PersonsRequest(model.getAuthToken());
            //Creates a EventsRequest object from the model data
            EventsRequest req2 = new EventsRequest(model.getAuthToken());
            //Creates a new ServerProxy object to send request to the server
            ServerProxy serverProxy = new ServerProxy(host, port);

            try {
                //----- DATA CACHE STUFF -----//

                PersonsResult res1 = serverProxy.getPeople(req1);
                EventsResult res2 = serverProxy.getEvents(req2);

                //Data cache storage
                DataCache dataCache = DataCache.getInstance();
                //Sets the users auth token
                dataCache.setAuthToken(model.getAuthToken());
                //Populates the peopleByID map
                dataCache.setPeopleByID(res1.getData());
                //Sets the user
                dataCache.setUser(model.getPersonID());
                //Sets the selectedPerson
                dataCache.setPersonSelected(dataCache.getPeopleByID().get(model.getPersonID()));
                //Populate the EventsByPersonID map
                dataCache.setEventsByPersonID(res2.getData());
                //Sets the selected event
                List<Event> events = new ArrayList<>(dataCache.getEventsByPersonID().get(model.getPersonID()));
                dataCache.setEventSelected(events.get(0));
                //Populate the MarkerColors map
                dataCache.setMarkerColors(res2.getData());
                //Populate the childrenByParentID map
                dataCache.setChildrenByParentID(res1.getData());
                //Populate the fatherSideMales set
                dataCache.setFatherSideMales(res1.getData());
                //Populate the fatherSideFemales set
                dataCache.setFatherSideFemales(res1.getData());
                //Populate the motherSideMales set
                dataCache.setMotherSideMales(res1.getData());
                //Populate the motherSideFemales set
                dataCache.setMotherSideFemales(res1.getData());

            } catch (IOException e) {
                sendMessage(false);
                e.printStackTrace();
            }

            sendMessage(true);

            Fragment mapFragment = new MapFragment();
            FragmentManager fm = getActivity().getSupportFragmentManager();
            fm.beginTransaction()
                    .replace(R.id.mainActivityLayout, mapFragment)
                    .addToBackStack(null)
                    .commit();

        }

        private void sendMessage (Boolean dataSuccess) {
            Message msg = Message.obtain();
            Bundle msgBundle = new Bundle();
            msgBundle.putBoolean("dataSuccess", dataSuccess);
            msg.setData(msgBundle);
            msgHandler.sendMessage(msg);
        }
    }

}