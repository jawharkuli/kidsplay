package com.example.kidsplay;
import java.util.List;

public interface StoriesCallback {
    void onResult(List<StoryInfo> stories);
    void onError(String errorMessage);
}