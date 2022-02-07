package com.example.familymap;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import Request.EventsRequest;
import Request.LoginRequest;
import Request.PersonsRequest;
import Request.RegisterRequest;
import Result.ClearResult;
import Result.EventsResult;
import Result.LoginResult;
import Result.PersonsResult;
import Result.RegisterResult;

public class ServerProxy {
    private final String host;
    private final String port;

    public ServerProxy(String host, String port) {
        this.host = host;
        this.port = port;
    }

    public ClearResult clear() throws IOException {
        ClearResult res = null;
        URL url = new URL("http://" + host + ":" + port + "/clear");

        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        connection.connect();

        try {
            //Handles the Server response
            InputStream respBody = connection.getInputStream();
            String bodyString = requestBodyToString(respBody);
            Gson gsonIn = new Gson();
            res = gsonIn.fromJson(bodyString, ClearResult.class);

        }
        catch (IOException e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
        }
        return res;
    }

    public LoginResult login (LoginRequest req) throws IOException {
        LoginResult res = null;

        URL url = new URL("http://" + host + ":" + port + "/user/login");

        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        connection.connect();

        try {
            Gson gson = new Gson();
            String jsonString = gson.toJson(req);

            OutputStream reqBody = connection.getOutputStream();
            Writer writer = new OutputStreamWriter(reqBody);
            writer.write(jsonString);
            writer.flush();
            reqBody.close();

            //Handles the Server response
            InputStream respBody = connection.getInputStream();
            String bodyString = requestBodyToString(respBody);
            Gson gsonIn = new Gson();
            res = gsonIn.fromJson(bodyString, LoginResult.class);

        }
        catch (IOException e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
        }

        return res;
    }

    public RegisterResult register (RegisterRequest req) throws IOException {
        RegisterResult res = null;

        URL url = new URL("http://" + host + ":" + port + "/user/register");

        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        connection.connect();

        try {
            Gson gson = new Gson();
            String jsonString = gson.toJson(req);

            OutputStream reqBody = connection.getOutputStream();
            Writer writer = new OutputStreamWriter(reqBody);
            writer.write(jsonString);
            writer.flush();
            reqBody.close();

            //Handles the Server response
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = connection.getInputStream();
                String bodyString = requestBodyToString(respBody);
                Gson gsonIn = new Gson();
                res = gsonIn.fromJson(bodyString, RegisterResult.class);

            }
        }
        catch (IOException e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
        }

        return res;
    }

    public PersonsResult getPeople (PersonsRequest req) throws IOException {
        PersonsResult res = null;

        URL url = new URL("http://" + host + ":" + port + "/person");

        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoOutput(false);

        connection.addRequestProperty("Authorization", req.getToken());

        connection.connect();

        try {
            //Handles the Server response
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {


                InputStream respBody = connection.getInputStream();
                String bodyString = requestBodyToString(respBody);
                Gson gsonIn = new Gson();
                res = gsonIn.fromJson(bodyString, PersonsResult.class);

            }
        }
        catch (IOException e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
        }

        return res;
    }

    public EventsResult getEvents (EventsRequest req) throws IOException {
        EventsResult res = null;

        URL url = new URL("http://" + host + ":" + port + "/event");

        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoOutput(false);

        connection.addRequestProperty("Authorization", req.getToken());

        connection.connect();

        try {

            //Handles the Server response
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {


                InputStream respBody = connection.getInputStream();
                String bodyString = requestBodyToString(respBody);
                Gson gsonIn = new Gson();
                res = gsonIn.fromJson(bodyString, EventsResult.class);

            }
        }
        catch (IOException e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
        }

        return res;
    }

    String requestBodyToString (InputStream is) {
        try {
            InputStreamReader reader = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(reader);

            int b;
            StringBuilder buf = new StringBuilder(512);
            while ((b = bufferedReader.read()) != -1) {
                buf.append((char) b);
            }

            bufferedReader.close();
            reader.close();
            return buf.toString();

        } catch (IOException e) {
            return null;
        }
    }
}
