package com.ftninfomatika.ispitnizadatak.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ftninfomatika.ispitnizadatak.R;
import com.ftninfomatika.ispitnizadatak.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AddedMovieAdapter {
    private List<Movie> movies = null;
    private Activity activity;

    public AddedMovieAdapter(Activity activity, List<Movie> movies) {
        this.movies = movies;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Movie getItem(int position) {
        return movies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.listview_search_single_item, null);
        }

        ImageView imageMovie = convertView.findViewById(R.id.image_Movie);
        TextView tvTitle = convertView.findViewById(R.id.textView_Title);

        tvTitle.setText(movies.get(position).getTitle() + " (" + movies.get(position).getYear() + ") ");

        Picasso.get().load(movies.get(position).getPoster()).into(imageMovie);

        return convertView;
    }
}