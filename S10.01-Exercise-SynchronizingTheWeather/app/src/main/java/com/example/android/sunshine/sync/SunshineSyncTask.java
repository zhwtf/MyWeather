//  TODO (1) Create a class called SunshineSyncTask
//  TODO (2) Within SunshineSyncTask, create a synchronized public static void method called syncWeather
//      TODO (3) Within syncWeather, fetch new weather data
//      TODO (4) If we have valid results, delete the old data and insert the new
package com.example.android.sunshine.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.example.android.sunshine.data.WeatherContract;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;

import java.net.URL;

public class SunshineSyncTask {
    synchronized public static void syncWeather(Context context) {
        // fetch new data
        try {
            URL weatherRequestUrl = NetworkUtils.getUrl(context);
            //use the URL to retrieve the JSON
            String jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);

            // Parse the JSON into a list of weather values
            ContentValues[] weatherValues = OpenWeatherJsonUtils.getWeatherContentValuesFromJson(context, jsonWeatherResponse);

            if (weatherValues != null && weatherValues.length != 0) {
                // get a handle on the ContentResolver to delete and insert data
                ContentResolver sunshineContentResolver = context.getContentResolver();

                // if we have valid weather data, then we delete the old data and insert the new data
                sunshineContentResolver.delete(WeatherContract.WeatherEntry.CONTENT_URI, null, null);

                // Insert our new weather data into sunshine's ContentProvider
                sunshineContentResolver.bulkInsert(WeatherContract.WeatherEntry.CONTENT_URI, weatherValues);

                /* If the code reaches this point, we have successfully performed our sync */

            }
        } catch (Exception e) {
            // Server probably invalid
            e.printStackTrace();
        }
    }

}