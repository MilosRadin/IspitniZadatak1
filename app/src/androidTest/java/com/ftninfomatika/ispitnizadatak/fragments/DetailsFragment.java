package com.ftninfomatika.ispitnizadatak.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.ftninfomatika.ispitnizadatak.R;
import com.ftninfomatika.ispitnizadatak.activities.MainActivity;
import com.ftninfomatika.ispitnizadatak.model.Movie;
import com.squareup.picasso.Picasso;

import java.sql.SQLException;

public class DetailsFragment extends Fragment {


    private Movie movie;

    public DetailsFragment() {

    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        setHasOptionsMenu(true);

        TextView tvTitle = view.findViewById(R.id.tvTitle_Details);
        TextView tvYear = view.findViewById(R.id.tvYear_Details);
        TextView tvPlot = view.findViewById(R.id.tvPlot_Details);

        ImageView image = view.findViewById(R.id.imageView_Details);

        RatingBar ratingBar = view.findViewById(R.id.ratingBar_Details);


        tvTitle.setText(movie.getTitle());
        tvYear.setText(movie.getYear());
        tvPlot.setText(movie.getPlot());

        ratingBar.setRating(movie.getRating());

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                try {
                    movie.setRating(ratingBar.getRating());
                    ((MainActivity) getActivity()).getDatabaseHelper().getMovieDao().update(movie);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        Picasso.get().load(movie.getPoster()).into(image);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.details_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                try {
                    ((MainActivity) getActivity()).getDatabaseHelper().getMovieDao().delete(movie);
                    Toast.makeText(getActivity(), movie.getTitle() + " uspesno obrisan", Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                } catch (SQLException e) {
                    Toast.makeText(getActivity(), movie.getTitle() + " nije obrisan", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
