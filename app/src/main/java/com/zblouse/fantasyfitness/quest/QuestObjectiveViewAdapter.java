package com.zblouse.fantasyfitness.quest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zblouse.fantasyfitness.R;

import java.util.List;

public class QuestObjectiveViewAdapter extends RecyclerView.Adapter<QuestObjectiveViewAdapter.ViewHolder> {

    private List<QuestObjective> questObjectives;

    public QuestObjectiveViewAdapter(List<QuestObjective> questObjectives){
        this.questObjectives = questObjectives;
    }

    @NonNull
    @Override
    public QuestObjectiveViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quest_objective_view_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestObjectiveViewAdapter.ViewHolder holder, int position) {
        holder.objectiveDescriptionTextView.setText(questObjectives.get(position).getQuestObjectiveDescription());
        if(questObjectives.get(position).isObjectiveMet()){
            holder.completedImage.setImageResource(R.drawable.objective_met);
        } else {
            holder.completedImage.setImageResource(R.drawable.objective_not_met);
        }
    }

    @Override
    public int getItemCount() {
        return questObjectives.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView completedImage;
        public TextView objectiveDescriptionTextView;

        public ViewHolder(View itemView){
            super(itemView);
            completedImage = itemView.findViewById(R.id.objective_complete);
            objectiveDescriptionTextView = itemView.findViewById(R.id.quest_objective_description);
        }
    }

}
