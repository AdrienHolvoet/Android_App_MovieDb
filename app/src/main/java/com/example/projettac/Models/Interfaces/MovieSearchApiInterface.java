package com.example.projettac.Models.Interfaces;

import com.example.projettac.Models.SearchMovieResults;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieSearchApiInterface {

    //Part of the API URL to search all movies in the API
    @GET("search/movie?api_key=89f769bfff269da3952206f65a6e23d4&language=fr")
    Call<SearchMovieResults> getSearchMovies (@Query("query") String query);
}

