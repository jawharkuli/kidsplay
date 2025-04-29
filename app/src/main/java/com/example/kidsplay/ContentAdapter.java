package com.example.kidsplay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ContentViewHolder> {

    private Context context;
    private List<ContentModel> contentList;

    public ContentAdapter(Context context, List<ContentModel> contentList) {
        this.context = context;
        this.contentList = contentList;
    }

    @Override
    public ContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_content, parent, false);
        return new ContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContentViewHolder holder, int position) {
        ContentModel content = contentList.get(position);
        holder.title.setText(content.getTitle());

        // Loading the content image using Picasso
        Picasso.get().load(content.getFileUrl()).into(holder.contentImage);
    }

    @Override
    public int getItemCount() {
        return contentList.size();
    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView contentImage;

        public ContentViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.content_title);
            contentImage = itemView.findViewById(R.id.content_image);
        }
    }
}
