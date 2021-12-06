package com.example.bookstore.API_Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface APIController {
    String DOMAIN = "https://shopbook3.herokuapp.com/api/books/";
    Gson gson =new GsonBuilder().setDateFormat("yyyy MM dd HH:mm:ss").create();

    APIController apiService = new Retrofit.Builder()
            .baseUrl(DOMAIN)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(APIController.class);

    @GET("{publisher}")
    Call<ResponseEntity> getBookByPublisher(@Path(value = "publisher", encoded = true) String publisher);

}
