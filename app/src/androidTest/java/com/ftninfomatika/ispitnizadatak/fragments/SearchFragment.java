package com.ftninfomatika.ispitnizadatak.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.ftninfomatika.ispitnizadatak.R;
import com.ftninfomatika.ispitnizadatak.adapters.SearchListAdapter;
import com.ftninfomatika.ispitnizadatak.model.Movie;
import com.ftninfomatika.ispitnizadatak.model.Result;
import com.ftninfomatika.ispitnizadatak.model.Search;
import com.ftninfomatika.ispitnizadatak.net.webservices.RESTService;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    private ListView lvMovies;

    private onItemClickListener listener;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        EditText etTitle = view.findViewById(R.id.etSearch);
        Button buttonSearch = view.findViewById(R.id.bSearch);
        lvMovies = view.findViewById(R.id.lvSearch);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etTitle.getText().toString().equals(""))
                    searchMovies(etTitle.getText().toString());
            }
        });
        return view;
    }

    private void searchMovies(String search) {

        HashMap<String, String> query = new HashMap<>();
        query.put("apikey", RESTService.API_KEY);
        query.put("s", search);

        Call<Result> call = RESTService.apiInterface().getMoviesByTitle(query);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.code() == 200) {
                    showMovies(response.body().getSearch());
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showMovies(List<Search> search) {
        if (search != null) {
            SearchListAdapter adapter = new SearchListAdapter(getActivity(), search);
            lvMovies.setAdapter(adapter);
            lvMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    searchMovieByID(adapter.getItem(position).getImdbID());
                }
            });
        }
    }

    private void searchMovieByID(String id) {
        HashMap<String, String> query = new HashMap<>();
        query.put("apikey", RESTService.API_KEY);
        query.put("i", id);

        Call<Movie> call = RESTService.apiInterface().getMoviesByIMDBID(query);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        listener.onSearchItemClicked(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
        void onSearchItemClicked(Movie movie);
    }

    public interface onListItemClickListener {
    }
}

