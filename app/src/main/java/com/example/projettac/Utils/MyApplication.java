package com.example.projettac.Utils;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.room.Room;

import com.example.projettac.Models.Databases.MovieDatabase;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyApplication extends Application {

    final String TAG = getClass().getSimpleName();
    private static MyApplication mInstance;
    private static Retrofit retrofit = null;
    private static MovieDatabase movieDatabaseRoom = null;
    private static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static void setContext(Context context) {
        applicationContext = context;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * Get a global instance of retrofit
     */
    public static Retrofit getRetrofitClient() {

        if (retrofit == null) {
           retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    /**
     * Get a global instance of MovieDatabase Room
     */
    public static MovieDatabase getMovieDatabase() {
        if (movieDatabaseRoom == null) {
            movieDatabaseRoom = Room.databaseBuilder(applicationContext, MovieDatabase.class, "database")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return movieDatabaseRoom;
    }
}

