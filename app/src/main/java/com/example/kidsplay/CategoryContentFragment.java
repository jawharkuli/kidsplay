package com.example.kidsplay;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryContentFragment extends Fragment {

    private RecyclerView recyclerView;
    private ContentAdapter contentAdapter;
    private List<ContentModel> contentList;
    private int selectedCategoryId = -1;

    public CategoryContentFragment() {}

    // Static factory method to create a new instance with category ID
    public static CategoryContentFragment newInstance(int categoryId) {
        CategoryContentFragment fragment = new CategoryContentFragment();
        Bundle args = new Bundle();
        args.putInt("category_id", categoryId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category_content, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewContent);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize list and adapter
        contentList = new ArrayList<>();
        contentAdapter = new ContentAdapter(getContext(), contentList);
        recyclerView.setAdapter(contentAdapter);

        // Check if category ID is passed in arguments and fetch content
        if (getArguments() != null) {
            selectedCategoryId = getArguments().getInt("category_id", -1);
            if (selectedCategoryId != -1) {
                fetchCategoryContent(selectedCategoryId);
            }
        }

        return view;
    }

    // Fetch category content from the API using Retrofit
    private void fetchCategoryContent(int categoryId) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ContentResponse> call = apiService.getCategoryContent(categoryId);  // Corrected to use ContentResponse

        call.enqueue(new Callback<ContentResponse>() {
            @Override
            public void onResponse(Call<ContentResponse> call, Response<ContentResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // If the API response is successful, update the content list
                    ContentResponse contentResponse = response.body();
                    if (contentResponse.isSuccess()) {
                        contentList.clear();
                        contentList.addAll(contentResponse.getData());  // Add data from the response
                        contentAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "No content found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Failed to load content", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ContentResponse> call, Throwable t) {
                // Handle failure in fetching content
                Toast.makeText(getContext(), "Failed to load content", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
