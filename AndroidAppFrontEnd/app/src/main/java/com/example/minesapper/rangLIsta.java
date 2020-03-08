package com.example.minesapper;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class rangLIsta extends Fragment {
    RecyclerView recyclerView;
    Context ctx;
    ArrayList<Player> user;
    String korisnicko_ime;
    public rangLIsta(Context ctx, ArrayList<Player> user,String korisnicko_ime) {
        this.korisnicko_ime=korisnicko_ime;
        this.ctx=ctx;
        this.user=user;



    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = (View) inflater.inflate(R.layout.fragment_rang_lista, container, false);
        poziv();
        return view;
    }
    public void poziv(){
        Log.d("provera","ovdePoziv");
        recyclerView = view.findViewById(R.id.RangView);
        RecyclerViewAdapter adapter=new RecyclerViewAdapter(getContext(),user,korisnicko_ime);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }


}
