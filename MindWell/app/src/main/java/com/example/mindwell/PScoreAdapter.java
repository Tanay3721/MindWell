package com.example.mindwell;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class PScoreAdapter extends FirebaseRecyclerAdapter<PScoreModel,PScoreAdapter.myviewholder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    Activity activity;
    public PScoreAdapter(@NonNull FirebaseRecyclerOptions<PScoreModel> options, Activity activity) {
        super(options);
        this.activity = activity;
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull PScoreModel model) {
        holder.perScore.setText(model.getScore());
        holder.Date.setText(model.getDate());
        holder.progressBar.setProgress(Integer.parseInt(model.getScore()));
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_score,parent,false);
        return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder{

        TextView perScore,Date;
        ProgressBar progressBar;

        public myviewholder(View itemView)
        {
            super(itemView);
            perScore = itemView.findViewById(R.id.score_scoreper);
            Date = itemView.findViewById(R.id.score_date);
            progressBar = itemView.findViewById(R.id.score_progress);
        }

    }
}
