package com.inspiringteam.mrnews.data.source.remote;

import com.inspiringteam.mrnews.data.models.NewsResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsService {
    @GET("top-headlines")
    Single<NewsResponse> getNews(@retrofit2.http.Query("country") String country, @Query("category") String category);
}
