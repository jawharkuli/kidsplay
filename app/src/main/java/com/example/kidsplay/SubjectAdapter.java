package com.example.kidsplay;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder> {

    private final Context context;
    private final List<DatabaseHelper.SubjectInfo> subjects;
    private final String selectedClass; // Added to store the class name

    // Modified constructor to include selectedClass
    public SubjectAdapter(Context context, List<DatabaseHelper.SubjectInfo> subjects, String selectedClass) {
        this.context = context;
        this.subjects = subjects;
        this.selectedClass = selectedClass;
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_subject, parent, false);
        return new SubjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        DatabaseHelper.SubjectInfo subject = subjects.get(position);

        holder.subjectName.setText(subject.getName());

        // Load image using Picasso (make sure to add Picasso dependency to your build.gradle)
        if (subject.getImageUrl() != null && !subject.getImageUrl().isEmpty()) {
            Picasso.get()
                    .load(subject.getImageUrl())
                    .placeholder(R.drawable.error_image) // Replace with your placeholder image
                    .error(R.drawable.error_image) // Replace with your error image
                    .into(holder.subjectImage);
        }

        holder.cardView.setOnClickListener(v -> {
            // Create intent to start PrimaryContentActivity
            Intent intent = new Intent(context, PrimaryContentActivity.class);

            // Pass data in the intent
            intent.putExtra("selectedClass", selectedClass);
            intent.putExtra(PrimaryActivity.EXTRA_SUBJECT_ID, subject.getId());
            intent.putExtra(PrimaryActivity.EXTRA_SUBJECT_NAME, subject.getName());

            // Show a toast message for feedback
            Toast.makeText(context, "Selected " + subject.getName() + " for " + selectedClass, Toast.LENGTH_SHORT).show();

            // Start the activity
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    public static class SubjectViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView subjectImage;
        TextView subjectName;

        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            subjectImage = itemView.findViewById(R.id.subject_image);
            subjectName = itemView.findViewById(R.id.subject_name);
        }
    }
}