package com.example.kidsplay;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeContentFragment extends Fragment {

    private RecyclerView recyclerView;
    private ContentAdapter contentAdapter;
    private List<ContentModel> contentList;

    public HomeContentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_content, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Get category ID passed from activity or fragment
        int categoryId = getArguments() != null ? getArguments().getInt("category_id", -1) : -1;

        // Fetch category content if categoryId is valid
        if (categoryId != -1) {
            fetchCategoryContent(categoryId);
        }

        return view;
    }

    private void fetchCategoryContent(int categoryId) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<ContentResponse> call = apiService.getCategoryContent(categoryId);
        call.enqueue(new Callback<ContentResponse>() {
            @Override
            public void onResponse(Call<ContentResponse> call, Response<ContentResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    contentList = response.body().getData();
                    contentAdapter = new ContentAdapter(getContext(), contentList);
                    recyclerView.setAdapter(contentAdapter);
                } else {
                    // Handle empty response or error in fetching data
                }
            }

            @Override
            public void onFailure(Call<ContentResponse> call, Throwable t) {
                // Handle failure, maybe show an error message to the user
            }
        });
    }
}
