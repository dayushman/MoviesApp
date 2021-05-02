package com.example.moviesapp.Utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class NetworkUtil {
    private static String API_URL = "https://api.themoviedb.org/3/movie";
    private static String API_KEY_PARA = "api_key";
    private static String PAGE_PARA = "api_key";
    private static String LANGUAGE_PARA = "language";
    private static String LANGUAGE = "en-US";
    private static String  API_KEY = "3d99e2f3c1a522e9a3c98fc07f836797";
    private static int cur_page = 1;


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
    public static URL buildUrl(String query, int page){
        cur_page = page;
        Uri uri = Uri.parse(API_URL).buildUpon()
                .appendEncodedPath(query)
                .appendQueryParameter(API_KEY_PARA,API_KEY)
                .appendQueryParameter(LANGUAGE_PARA,LANGUAGE)
                .appendQueryParameter(PAGE_PARA, String.valueOf(cur_page))
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
