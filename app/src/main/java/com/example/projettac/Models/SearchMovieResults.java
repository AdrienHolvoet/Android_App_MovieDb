package com.example.projettac.Models;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchMovieResults {

    @SerializedName("results")
    @Expose
    private List<MovieModel> results = null;

    public List<MovieModel> getResults() {
    return results;
    }
}