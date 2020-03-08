package com.example.minesapper;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    ArrayList<Player> players;
    Context ctx;
    String user;
    public RecyclerViewAdapter(Context ctx, ArrayList<Player> players,String user) {

        this.user=user;
        this.ctx=ctx;
        this.players=players;
        for(int i =0; i<players.size();i++){
            Log.d("stigo",players.get(i).RedniBroj);
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rang_lista, parent, false);
        ViewHolder holder=new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(players.get(position).Username.equals(user)){
            holder.redniBroj.setText(players.get(position).RedniBroj);
            holder.username.setText(players.get(position).Username);
            holder.vreme.setText(players.get(position).Vreme);
                holder.username.setTextColor(Color.CYAN);
            holder.redniBroj.setTextColor(Color.CYAN);
            holder.vreme.setTextColor(Color.CYAN);
        }else {
            holder.redniBroj.setText(players.get(position).RedniBroj);
            holder.username.setText(players.get(position).Username);
            holder.vreme.setText(players.get(position).Vreme);
        }
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView redniBroj,username,vreme;
        RelativeLayout parentF;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            redniBroj=itemView.findViewById(R.id.redniBroj);
            username=itemView.findViewById(R.id.usernameU);
            vreme=itemView.findViewById(R.id.vreme);
            parentF=itemView.findViewById(R.id.parentLayout);
        }
    }
}
