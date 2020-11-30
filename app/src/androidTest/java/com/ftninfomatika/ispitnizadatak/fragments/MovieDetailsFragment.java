package com.ftninfomatika.ispitnizadatak.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.ftninfomatika.ispitnizadatak.R;
import com.ftninfomatika.ispitnizadatak.activities.MainActivity;
import com.ftninfomatika.ispitnizadatak.adapters.AddedMovieAdapter;
import com.ftninfomatika.ispitnizadatak.model.Movie;
import com.squareup.picasso.Picasso;

import java.sql.SQLException;
import java.util.List;

public class MovieDetailsFragment extends Fragment {


    private onItemClickListener listener;

    private ListView lvAddedMovies;

    public MovieDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);

        setHasOptionsMenu(true);

        lvAddedMovies = view.findViewById(R.id.lvWatchedMovies);

        try {
            List<Movie> movies = ((MainActivity) getActivity()).getDatabaseHelper().getMovieDao().queryForAll();

            if (movies.size() > 0) {
                AddedMovieAdapter adapter = new AddedMovieAdapter(getActivity(), movies);
                lvAddedMovies.setAdapter(adapter);
                lvAddedMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

                        boolean open_tehnicki = sharedPreferences.getBoolean("show_details", true);
                        boolean open_detalji = sharedPreferences.getBoolean("show_actors_details", true);

                        if (open_tehnicki) {
                            listener.showTehnickiDetalji(adapter.getItem(i));
                        }
                        if (open_detalji) {
                            listener.showDetaljiOUcesnicima(adapter.getItem(i));
                        }
                    }
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.movie_details_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                listener.onaddClicked();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof onItemClickListener) {
            listener = (onItemClickListener) context;
        } else {
            Toast.makeText(getActivity(), "Morate implementirati intefrace", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface onItemClickListener {
        void onaddClicked();

        void showTehnickiDetalji(Movie movie);

        void showDetaljiOUcesnicima(Movie movie);
    }
}