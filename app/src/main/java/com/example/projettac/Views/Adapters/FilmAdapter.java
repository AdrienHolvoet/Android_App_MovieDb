package com.example.projettac.Views.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projettac.Models.MovieModel;
import com.example.projettac.R;
import com.example.projettac.Utils.ListItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.List;

// Link the recyclerview to the DataSet
public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.FilmViewHolder> {

    private List<MovieModel> movieList;
    private ListItemClickListener mListener;

    //Pass the DataSet
    public FilmAdapter(List<MovieModel> movieList, ListItemClickListener listener) {
        this.movieList = movieList;
        mListener = listener;
    }

    //Class to store the information of one item
    public static class FilmViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title;
        private TextView description;
        private ImageView filmImg;
        private ListItemClickListener mListener;

        public FilmViewHolder(@NonNull View itemView, ListItemClickListener listener) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            filmImg = itemView.findViewById(R.id.filmImg);
            mListener = listener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            mListener.onListItemClick(view, getBindingAdapterPosition());
        }
    }

    // Create new views(row_film, invoked by the layout manager)
    @NonNull
    @Override
    public FilmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_film, parent, false);
        FilmViewHolder filmViewHolder = new FilmViewHolder(view, mListener);
        return filmViewHolder;
    }

    //set the content of the view=(row_film), Link the data of the list to the viewHolder(row_film)
    @Override
    public void onBindViewHolder(@NonNull FilmViewHolder holder, int position) {

       MovieModel movie = movieList.get(position);
       holder.title.setText(movie.getTitle());
        if(movie.getDescription().isEmpty()) {
            holder.description.setText("Pas de description disponible");
        } else {
            holder.description.setText(movie.getDescription());
        }
        //Url for movie poster that we get from APi
        if(movie.getPosterPath() != null) {
        String posterImage = "https://image.tmdb.org/t/p/w200" + movie.getPosterPath();

        //We use Picasso library to manage images (cache and image display)
        Picasso.get().load(posterImage).into(holder.filmImg);
        }
    }

    //return size of the DataSet (List)
    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public MovieModel getClickedMovie(int position) {
        Log.d("POSITION", "position = "+position);
        return movieList.get(position);
    }
}
