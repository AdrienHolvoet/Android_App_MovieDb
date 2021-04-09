package com.example.projettac.Models;

import android.os.Parcel;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

//Define table name
@Entity
public class MovieModel implements Serializable {

    //Create id column
    @PrimaryKey(autoGenerate = true)
    private long uid;

    //id from API
    @SerializedName("id")
    @Expose
    private int id;

    //link to movie image from API
    @SerializedName("poster_path")
    @Expose
    private String posterPath;

    //Create title column
    @SerializedName("title")
    @Expose
    @ColumnInfo(name="title")
    private String title;

    @SerializedName("vote_average")
    @Expose
    @ColumnInfo(name="vote_average")
    private String vote_average;

    //Create description column
    @SerializedName("overview")
    @Expose
    @ColumnInfo(name="description")
    private String description;

    //image for detail view of movies
    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;

    public MovieModel(String title, String description) {
        this.title = title;
        this.description = description;
    }

    protected MovieModel(Parcel in) {
        uid = in.readInt();
        id = in.readInt();
        title = in.readString();
        description = in.readString();
    }

    public long getUid() {
        return uid;
    }

    public int getId() {
        return id;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getVote_average()  { return vote_average; }

    public void setVote_average(String vote) {
        this.vote_average = vote;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getDescription() {
        return description;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

}
