package com.example.kidsplay;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    // Fetch home categories
    @GET("get_home_categories.php")
    Call<CategoryResponse> getHomeCategories();

    // Fetch content by category
    @GET("fetch_home_content.php")
    Call<ContentResponse> getCategoryContent(@Query("category_id") int categoryId);
}
