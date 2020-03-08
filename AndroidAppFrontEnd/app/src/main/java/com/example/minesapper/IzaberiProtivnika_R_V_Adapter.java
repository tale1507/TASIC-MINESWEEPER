package com.example.minesapper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.net.Socket;
import java.util.ArrayList;

public class IzaberiProtivnika_R_V_Adapter extends RecyclerView.Adapter<IzaberiProtivnika_R_V_Adapter.ViewHolder>{
    ArrayList<Player> igrac;
    Context ctx;
    String userN;
    StartGameMethod IStGame;
    public IzaberiProtivnika_R_V_Adapter(ArrayList<Player> igrac,Context ctx,String userN,StartGameMethod IStGame) {
        this.igrac=igrac;
        this.ctx=ctx;
        this.userN=userN;
        this.IStGame=IStGame;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.item_protivnik, parent, false);
        IzaberiProtivnika_R_V_Adapter.ViewHolder holder=new IzaberiProtivnika_R_V_Adapter.ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            if(igrac.get(position).OnlineStatus.equals("slobodan")){
            holder.redniBroj.setText(String.valueOf(position + 1));
            holder.username.setText(igrac.get(position).Username);

            holder.izazovi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    IStGame.StartujIgru(igrac.get(position).Username,userN);

                }
            });
            }else{
                holder.redniBroj.setText(String.valueOf(position + 1));
                holder.username.setText(igrac.get(position).Username);

                holder.izazovi.setText("Igra...");
                holder.izazovi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        IzaberiProtivnika iz=new IzaberiProtivnika();
//                        IStGame.StartujIgru(igrac.get(position).Username,userN);

                    }
                });
            }


    }

    @Override
    public int getItemCount() {
        return igrac.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView redniBroj,username;
        Button izazovi;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            redniBroj=itemView.findViewById(R.id.redniBroj);
            username=itemView.findViewById(R.id.usernameU);
            izazovi=itemView.findViewById(R.id.izazovi);
        }
    }
}
