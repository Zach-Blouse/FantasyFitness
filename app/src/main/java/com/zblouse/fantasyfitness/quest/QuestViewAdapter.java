package com.zblouse.fantasyfitness.quest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.home.UserHomeFragment;

import java.util.List;

public class QuestViewAdapter extends RecyclerView.Adapter<QuestViewAdapter.ViewHolder> {

    private List<Quest> quests;
    private UserHomeFragment userHomeFragment;

    public QuestViewAdapter(UserHomeFragment userHomeFragment, List<Quest> quests){
        this.quests = quests;
        this.userHomeFragment = userHomeFragment;
    }

    @NonNull
    @Override
    public QuestViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quest_view_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestViewAdapter.ViewHolder holder, int position) {
        Quest quest = quests.get(position);
        holder.questNameTextView.setText(quest.getQuestName());
        holder.questCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userHomeFragment.displayQuestDetails(quest);
            }
        });
    }

    @Override
    public int getItemCount() {
        return quests.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CardView questCard;
        public TextView questNameTextView;

        public ViewHolder(View itemView){
            super(itemView);
            questCard = itemView.findViewById(R.id.quest_card);
            questNameTextView = itemView.findViewById(R.id.quest_name);
        }
    }

}
