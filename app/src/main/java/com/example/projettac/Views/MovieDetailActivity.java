package com.example.projettac.Views;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.projettac.Models.MovieModel;
import com.example.projettac.R;
import com.example.projettac.ViewModels.MovieViewModel;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

    private TextView titleTextView;
    private TextView voteAvgTxtView;
    private TextView descriptionTxtView;
    private ImageView moviePosterImgView;
    private ImageButton addToFavoriteBtn;
    private MovieViewModel movieViewModel;
    private boolean alreadyFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("ONCREATE", "detail activity");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_detail);

        Intent intent = getIntent();
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        final MovieModel movieToDisplay = (MovieModel) intent.getSerializableExtra("CURRENT_MOVIE");
        addToFavoriteBtn = findViewById(R.id.addToFavoriteBtn);
        moviePosterImgView = findViewById(R.id.moviePosterImgView);
        descriptionTxtView = findViewById(R.id.descriptionTxtView);
        voteAvgTxtView = findViewById(R.id.voteAvgTxtView);
        voteAvgTxtView.setText("note : " + movieToDisplay.getVote_average());
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText(movieToDisplay.getTitle());

        if(movieToDisplay.getDescription().isEmpty()) {
            descriptionTxtView.setText("Aucune description n'est disponible pour ce film");
        } else {
            descriptionTxtView.setText(movieToDisplay.getDescription());
        }

        if(movieToDisplay.getBackdropPath() != null) {
        String imgUrl = "https://image.tmdb.org/t/p/original" + movieToDisplay.getBackdropPath();
        Picasso.get().load(imgUrl).into(moviePosterImgView);
        }

        long uid = movieViewModel.movieAlreadyInFavorites(movieToDisplay);
        if (uid != 0) {
            alreadyFavorite = true;
            addToFavoriteBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_full_heart));
            movieToDisplay.setUid(uid);
        } else {
            alreadyFavorite = false;
            addToFavoriteBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_empty_heart));
        }

        addToFavoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (alreadyFavorite) {
                    Toast.makeText(getApplicationContext(), movieToDisplay.getTitle() + " a été retiré des favoris", Toast.LENGTH_SHORT).show();
                    movieViewModel.deleteMovieFromFavorites(movieToDisplay);
                    addToFavoriteBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_empty_heart));
                } else {
                    Toast.makeText(getApplicationContext(), movieToDisplay.getTitle() + " a été ajouté au favoris", Toast.LENGTH_SHORT).show();
                    movieViewModel.addMovieToFavorites(movieToDisplay);
                    addToFavoriteBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_full_heart));
                }
                alreadyFavorite = !alreadyFavorite;
                //Put delay before the next click to avoid duplicates
                addToFavoriteBtn.setEnabled(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        addToFavoriteBtn.setEnabled(true);
                    }
                }, 300);
            }
        });
    }
}