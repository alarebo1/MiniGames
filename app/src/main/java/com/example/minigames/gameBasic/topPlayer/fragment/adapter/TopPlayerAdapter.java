package com.example.minigames.gameBasic.topPlayer.fragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.example.minigames.R;
import com.example.minigames.firebase.model.User;

public class TopPlayerAdapter extends RecyclerView.Adapter<TopPlayerAdapter.ViewHolder> {

    private List<User> mData;
    private LayoutInflater mInflater;
    private int game;

    public TopPlayerAdapter(Context context, int game) {
        this.mInflater = LayoutInflater.from(context);
        this.game = game;
    }

    public void updateData(List<User> data){
        this.mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_top_player_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txt_user_mail.setText(mData.get(position).getName());

        String score = "";
        Context context = holder.itemView.getContext();
        switch (game){
            case 1:{
                score = context.getString(R.string.top_score,mData.get(position).getScoreGame1());
                break;
            }
            case 2:{
                score = context.getString(R.string.top_score,mData.get(position).getScoreGame2());
                break;
            }
            case 3:{
                score = context.getString(R.string.top_score,mData.get(position).getScoreGame3());
                break;
            }
            case 4:{
                score = context.getString(R.string.top_score_point,mData.get(position).getScoreGame4());
                break;
            }
            case 5:{
                score = context.getString(R.string.top_score_meter,mData.get(position).getScoreGame5());
                break;
            }

        }
        holder.txt_score.setText(score);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_user_mail, txt_score;

        ViewHolder(View itemView) {
            super(itemView);
            txt_user_mail = itemView.findViewById(R.id.txt_user_mail);
            txt_score = itemView.findViewById(R.id.txt_score);
        }
    }
}
