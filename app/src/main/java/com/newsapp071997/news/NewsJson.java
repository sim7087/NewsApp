package com.newsapp071997.news;

import android.text.TextUtils;
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

public final class NewsJson {
    public static final String LOG_TAG = NewsJson.class.getSimpleName();

    private NewsJson() {
    }

    public static List<NewsDesc> extractNewsData(String NEWS_JSON) {
        if (TextUtils.isEmpty(NEWS_JSON))
            return null;
        List<NewsDesc> newsData = new ArrayList<>();
        try {

            JSONObject rootJson = new JSONObject(NEWS_JSON);
            JSONObject res = rootJson.getJSONObject("response");
            JSONArray newsArray = res.getJSONArray("results");
            for (int i = 0; i < newsArray.length(); i++) {
                JSONObject object = newsArray.getJSONObject(i);
                JSONArray authorArray = object.getJSONArray("tags");
                String sectionName = object.getString("sectionName");
                String title = object.getString("webTitle");
                String date = object.getString("webPublicationDate");
                String newsUrl = object.getString("webUrl");
                JSONObject authorObject = authorArray.getJSONObject(0);
                NewsDesc newsList;
                if (authorObject.has("webTitle")) {
                    String author = authorObject.getString("webTitle");
                    newsList = new NewsDesc(title, sectionName, date, newsUrl, author);
                } else {
                    newsList = new NewsDesc(title, sectionName, date, newsUrl);
                }
                newsData.add(newsList);
            }
        } catch (JSONException e) {
            Log.e("json", e.toString());
        }
        return newsData;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
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
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news data JSON results.", e);
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

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Query the USGS dataset and return a list of {@link NewsDesc} objects.
     */
    public static List<NewsDesc> fetchNewsData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        List<NewsDesc> news = extractNewsData(jsonResponse);

        // Return the list of {@link Earthquake}s
        return news;
    }
}
