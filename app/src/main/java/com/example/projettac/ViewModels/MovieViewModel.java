package com.example.projettac.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.projettac.Models.MovieModel;
import com.example.projettac.Repositories.MovieRepository;

import java.util.List;

public class MovieViewModel extends ViewModel {

    private MovieRepository movieRepository;
    private MutableLiveData<List<MovieModel>> mutableRemoteLiveData;
    private MutableLiveData<List<MovieModel>> mutableFavoritesLiveData;

    public MovieViewModel() {
        movieRepository = new MovieRepository();
    }

    public LiveData<List<MovieModel>> getRemoteMovies(String query) {

        mutableRemoteLiveData = movieRepository.requestRemoteMovies(query);
        return mutableRemoteLiveData;
    }

    public LiveData<List<MovieModel>> getInstanceMutableLiveData() {
        if (mutableRemoteLiveData == null) {
            return new MutableLiveData<>();
        }
        return mutableRemoteLiveData;
    }

    public MutableLiveData<List<MovieModel>> getFavoritesMovies() {
        mutableFavoritesLiveData = movieRepository.requestFavoritesMovies();
        return mutableFavoritesLiveData;
    }

    public long movieAlreadyInFavorites(MovieModel movie) {
        mutableFavoritesLiveData =  this.getFavoritesMovies();
        List<MovieModel> movies = mutableFavoritesLiveData.getValue();

        if (movies != null && movies.size() != 0) {
            for (MovieModel var : mutableFavoritesLiveData.getValue()) {
                if (var.getId() == movie.getId()) {
                    return var.getUid();
                }
            }
        }
        return 0;
    }

    public void addMovieToFavorites(MovieModel movie) {
       long insertedId =  movieRepository.addMovieToFavorites(movie);
       movie.setUid(insertedId);
    }

    public void deleteMovieFromFavorites(MovieModel movie) {

        movieRepository.deleteMovieFromFavorites(movie);
    }
}
