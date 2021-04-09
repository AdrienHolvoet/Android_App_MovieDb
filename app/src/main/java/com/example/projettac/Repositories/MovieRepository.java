package com.example.projettac.Repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.projettac.Models.Databases.MovieDatabase;
import com.example.projettac.Models.Interfaces.MovieSearchApiInterface;
import com.example.projettac.Models.MovieModel;
import com.example.projettac.Models.SearchMovieResults;
import com.example.projettac.Utils.MyApplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {

    private MovieDatabase movieDatabase =
            MyApplication.getMovieDatabase();

    public MutableLiveData<List<MovieModel>> requestRemoteMovies(String query) {
        final MutableLiveData<List<MovieModel>> mutableLiveData = new MutableLiveData<>();

        // Get retrofit instance to remote api call
        MovieSearchApiInterface apiService =
                MyApplication.getRetrofitClient().create(MovieSearchApiInterface.class);
        Call<SearchMovieResults> callObject = apiService.getSearchMovies(query);

        callObject.enqueue(new Callback<SearchMovieResults>() {
            @Override
            public void onResponse(Call<SearchMovieResults> call, Response<SearchMovieResults> response) {
                mutableLiveData.setValue(response.body().getResults());
            }

            //if the call to the API did not work we log the error message
            @Override
            public void onFailure(Call<SearchMovieResults> call, Throwable t) {
                Log.e("API_GET_ERROR", t.getLocalizedMessage());
            }
        });

        return mutableLiveData;
    }

    /**
     * Request to Room db to get favorites movies
     * **/
    public MutableLiveData<List<MovieModel>> requestFavoritesMovies() {

        final MutableLiveData<List<MovieModel>> mutableLiveData = new MutableLiveData<>();
        movieDatabase.MovieDao().getAll();
        mutableLiveData.setValue(movieDatabase.MovieDao().getAll());
        return mutableLiveData;
    }

    /**
     * Request to Room db to delete  movie in parameter
     * **/
    public void deleteMovieFromFavorites(MovieModel Movie) {
        movieDatabase.MovieDao().delete(Movie);
    }

    /**
     * Request to Room db to add  movie in parameter
     * **/
    public long addMovieToFavorites(MovieModel Movie) {
        return movieDatabase.MovieDao().insert(Movie);
    }
}
