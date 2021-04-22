package com.example.moviesapp.Utilities;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;

import com.example.moviesapp.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class NetworkUtil {
    private static String API_URL = "https://api.themoviedb.org/3/movie";
    private static String API_KEY_PARA = "api_key";
    private static String LANGUAGE_PARA = "language";
    private static String LANGUAGE = "en-US";
    private static String  API_KEY = Resources.getSystem().getString(R.string.api_key);


    //Builds the api URL
    public static URL buildUrl(String query){

        Uri uri = Uri.parse(API_URL).buildUpon()
                .appendEncodedPath(query)
                .appendQueryParameter(API_KEY_PARA,API_KEY)
                .appendQueryParameter(LANGUAGE_PARA,LANGUAGE)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


    //Fetches the response from the APi and returns the response
    public static String getHTTPSResponse(URL url) throws IOException {
        HttpsURLConnection httpsURLConnection = null;
        String response = null;
        try {
            httpsURLConnection = (HttpsURLConnection) url.openConnection();
            InputStream inputStream = httpsURLConnection.getInputStream();
            Scanner scanner =new Scanner(inputStream);
            scanner.useDelimiter("//A");
            if (scanner.hasNext())
                response = scanner.next();
        } catch (IOException e) {
            e.printStackTrace();

        }
        finally {
            if (httpsURLConnection != null) {
                httpsURLConnection.disconnect();
            }
        }
        return response;

    }
}
