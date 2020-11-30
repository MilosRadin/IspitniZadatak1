package com.ftninfomatika.ispitnizadatak.net.webservices;

import com.ftninfomatika.ispitnizadatak.model.Movie;
import com.ftninfomatika.ispitnizadatak.model.Result;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface OMDBApiEndPointInterface {

    @GET("/")
    Call<Result> getMoviesByTitle(@QueryMap Map<String, String> options);

    @GET("/")
    Call<Movie> getMoviesByIMDBID(@QueryMap Map<String, String> query);


}
