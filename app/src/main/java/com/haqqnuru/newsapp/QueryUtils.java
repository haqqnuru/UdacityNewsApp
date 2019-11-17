package com.haqqnuru.newsapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

//Helper methods related to requesting and receiving news from the url.
public class QueryUtils {

    private static final String TAG = QueryUtils.class.getName();

    //Create a private constructor because no one should ever create a {@link QueryUtils} object.
    // This class is only meant to hold static variables and methods, which can be accessed
    // directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
    private QueryUtils() {
    }

    //Returns new URL object from the given string URL.
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Problem building the URL", e);
        }
        return url;
    }

    //Make an HTTP request to the given URL and return a String as the response.
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(TAG, "Problem retrieving the News JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    //Convert the {@link InputStream} into a String which contains the
    //whole JSON response from the server.
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder outputString = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader isr = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader br = new BufferedReader(isr);
            String currentLine = br.readLine();
            while (currentLine != null) {
                outputString.append(currentLine);
                currentLine = br.readLine();
            }
        }
        return outputString.toString();
    }

    //Return a list of {@link NewsItems} objects that has been built up from
    //parsing the given JSON response.
    private static List<NewsItems> extractNews(String jsonResponse) {

        // Create an empty ArrayList that we can start adding new items to
        List<NewsItems> news = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject jsonObject = new JSONObject(jsonResponse);

            // Extract the JSONObject associated with the key called "response",
            // which represents a list of news.
            JSONObject responseObject = jsonObject.getJSONObject("response");

            // For a given newsItem, extract the JSONObject associated with the
            // key called "results", which represents a list of all results
            // for that news.
            JSONArray resultsArray = responseObject.getJSONArray("results");

            // For each news item in the resultsArray, create an {@link newsItem} object
            for (int i = 0; i < resultsArray.length(); i++) {

                // Get a single newItem at position i within the list of newsItems
                JSONObject newsJSONObject = resultsArray.getJSONObject(i);

                // Extract the value for the key called "webTitle"
                String title = newsJSONObject.getString("webTitle");

                // Extract the value for the key called "sectionName"
                String section = newsJSONObject.getString("sectionName");

                // Extract the value for the key called "webPublicationDate"
                String publicationDate = newsJSONObject.getString("webPublicationDate");

                // Extract the value for the key called "webUrl"
                String webUrl = newsJSONObject.getString("webUrl");

                // Extract the value for the key called "type"
                String category = newsJSONObject.getString("type");

                // Create an empty ArrayList that we can start adding authors to
                ArrayList<String> author = new ArrayList<>();

                // Extract the JSONObject associated with the key called "tags",
                // which represents a (list) of authors.
                JSONArray newsTagsArray = newsJSONObject.getJSONArray("tags");

                // For each author in the author / tags Array, create an {@link author} object
                for (int j = 0; j < newsTagsArray.length(); j++) {

                    // Get a single newItem at position i within the list of newsItems
                    JSONObject authorObjectInTags = newsTagsArray.getJSONObject(j);

                    // Extract the value for the key called "webTitle" and add to the author Object
                    author.add(authorObjectInTags.getString("webTitle"));
                }

                // Create a new {@link NewsItem} object with the title, section, category, publicationDate
                // webUrl and author from the JSON response.
                news.add(new NewsItems(title, section, category, publicationDate, webUrl, author));
            }


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(TAG, "Problem parsing the news JSON results", e);
        }

        // Returns list of NewsItems
        return news;
    }

    //Query the news dataset and return a list of {@link NewsItems} objects.
    static List<NewsItems> fetchNewsData(String requestUrl) {

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(TAG, "Problem making the HTTP request", e);
        }

        // Return the list of {@link news}
        return extractNews(jsonResponse);
    }
}

