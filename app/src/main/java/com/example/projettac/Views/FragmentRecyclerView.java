package com.example.projettac.Views;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projettac.Models.MovieModel;
import com.example.projettac.R;
import com.example.projettac.Utils.ListItemClickListener;
import com.example.projettac.Utils.LoadingDialog;
import com.example.projettac.ViewModels.MovieViewModel;
import com.example.projettac.Views.Adapters.FilmAdapter;

import java.util.List;

import static com.example.projettac.R.layout.fragment_recycler_view;

public class FragmentRecyclerView extends Fragment {

    private static final String KEY_SCROLL_POSITION = "scrollPosition";
    private static int scrollPosition = 0;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FilmAdapter filmAdapter;
    private DividerItemDecoration dividerItemDecoration;
    private boolean isRemote;
    private SearchView searchView;
    private MovieViewModel movieViewModel;
    private ListItemClickListener listener;
    private TextView infoTextView;
    LoadingDialog loadingDialog;

    //Upate the view if there are new favorites
    @Override
    public void onResume() {
        super.onResume();
        if (!isRemote) {
            LiveData<List<MovieModel>> liveData = movieViewModel.getFavoritesMovies();
            List<MovieModel> movies = liveData.getValue();
            filmAdapter = new FilmAdapter(movies, listener);
            recyclerView.setAdapter(filmAdapter);
            if (movies != null && movies.size() != 0) {
                infoTextView.setVisibility(View.GONE);
            } else {
                infoTextView.setVisibility(View.VISIBLE);
                infoTextView.setText("Vous n'avez pas encore de Favoris, veuillez utiliser la recherche");
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Inflate the layout of MainFragment
        View view = inflater.inflate(fragment_recycler_view, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        searchView = view.findViewById(R.id.searchView);
        infoTextView = view.findViewById(R.id.infoTextView);
        loadingDialog = new LoadingDialog(getActivity());

        listener = (view1, position) -> {
            //transfer the movie on which user clicked to the MovieDetailActivity and opens that activity
            Log.d("Fragment", "position in fragment" + position);
            MovieModel m = filmAdapter.getClickedMovie(position);
            Intent intent = new Intent(getContext(), MovieDetailActivity.class);
            intent.putExtra("CURRENT_MOVIE", m);
            Log.d("CURRENT_MOVIE", "name" + m.getTitle());
            startActivity(intent);
        };

        // Get the ViewModel.
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        Bundle args = getArguments();
        isRemote = args.getBoolean("isRemote");
        if (isRemote) {
            //Get back the data from the viewModel when the app crashes
            LiveData<List<MovieModel>> liveData = movieViewModel.getInstanceMutableLiveData();
            List<MovieModel> movies = liveData.getValue();
            if (movies != null && movies.size() != 0) {
                infoTextView.setVisibility(View.GONE);
                filmAdapter = new FilmAdapter(movies, listener);
                recyclerView.setAdapter(filmAdapter);
            } else {
                infoTextView.setVisibility(View.VISIBLE);
                infoTextView.setText("Veuillez utiliser la barre de recherche pour voir vos films ");
            }

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    // Get the dataset from API
                    loadingDialog.startLoadingDialog();
                    initDatasetFromRemote(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    return false;
                }

            });

        } else {
            // Hide the search on favorites
            searchView.setVisibility(View.GONE);
        }

        if (savedInstanceState != null) {
            // Restore saved scrollPosition.
            scrollPosition = savedInstanceState.getInt(KEY_SCROLL_POSITION);
        }

        // Create a divide line between items for the recyclerView
        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        // END of the setting of the recyclerView

        int orientation = getResources().getConfiguration().orientation;

        // To know the orientation of the phone
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscape
            setRecyclerViewLayoutManager(LayoutManagerType.GRID_LAYOUT_MANAGER);
        } else {
            // In portrait
            setRecyclerViewLayoutManager(LayoutManagerType.LINEAR_LAYOUT_MANAGER);
        }

        return view;
    }

    /**
     * Set RecyclerView's LayoutManager to the one given.
     *
     * @param layoutManagerType Type of layout manager to switch to.
     */
    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                // Recyclerview display the data in a grid way
                layoutManager = new GridLayoutManager(getActivity(), 2);

                // Delete the dividing line
                recyclerView.removeItemDecoration(dividerItemDecoration);
                break;

            default:
                // Recyclerview display the data in a linear way
                layoutManager = new LinearLayoutManager(getActivity());

                // Separate each item of the recyclerView by a line
                recyclerView.addItemDecoration(dividerItemDecoration);
        }

        // Define how the recyclerview will be laid out
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.scrollToPosition(scrollPosition);
    }

    //Save the current scrollPosition of the user on the list when the activity/fragment is down
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // If a layout manager has already been set, get current scroll position.
        if (recyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
            savedInstanceState.putInt(KEY_SCROLL_POSITION, scrollPosition);
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    // Initialize dataset, this data come from remote server.
    private void initDatasetFromRemote(String query) {
        movieViewModel.getRemoteMovies(query).observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                // Give the dateset to our adapter and link it to the recyclerView
                if (movieModels != null && movieModels.size() != 0) {
                    infoTextView.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getActivity(), "Aucun films trouvés pour cette recherche", Toast.LENGTH_LONG).show();
                }
                loadingDialog.dismissDialog();;
                filmAdapter = new FilmAdapter(movieModels, listener);
                recyclerView.setAdapter(filmAdapter);
            }
        });
    }

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }
}
