package com.example.minesapper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

public class IzaberiProtivnika extends AppCompatActivity {

    private Socket mSocket;
    ArrayList<Player> igraciRV;
    SessionManager sessionManager;
    String PlayerUsername;
    StartGameMethod IStartGame;
    Button refreshListu;
    String protivnikUsername;
    String id_game;
    API apiRoute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_izaberi_protivnika);
        sessionManager=new SessionManager(this);
        apiRoute=new API();
        sesija();
        refreshListu =findViewById(R.id.pokreniOpetIP);

        setSocket();
        refList();
        attemptSend();

        mSocket.on("send-players", IgraciZaIgru);
        mSocket.on("StartBombe",StartG);


    }

    public void setSocket(){

        {
            try {
                mSocket = IO.socket(apiRoute.liveServer);
            } catch (URISyntaxException e) {}
        }


        mSocket.connect();

    }
       public int[] setIndexBombe(JSONArray niz) throws JSONException {
        if(PlayerUsername.equals(niz.getString(20))){
            protivnikUsername=niz.getString(21);
        }else{
            protivnikUsername=niz.getString(20);
        }
        int[] bombe=new int[20];
        id_game=niz.getString(22);
        for(int i =0; i<niz.length()-3; i++){
            String s=niz.getString(i);
            bombe[i]=Integer.parseInt(s);

        }
        return bombe;

    }



    Emitter.Listener StartG=new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            try {
                runOnUiThread(new Runnable() {
               JSONArray niz= (JSONArray) args[0];



                    @Override
                    public void run() {
                        int[] bombe=new int[20];
                        try {
                            bombe=setIndexBombe(niz);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Intent i =new Intent(getApplicationContext(),GameChallange.class);
                        HashMap<String, String> user=sessionManager.getUserDetail();
                        i.putExtra("username", user.get("NAME"));
                        i.putExtra("protivnik",protivnikUsername);
                        i.putExtra("id_game",id_game);

                        i.putExtra("bombe",bombe);
                        startActivity(i);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };



    Emitter.Listener IgraciZaIgru = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        igraciRV=new ArrayList<>();
                        JSONArray data = (JSONArray) args[0];
                        for(int i =1;i<data.length();i++){
                            try {
                                JSONObject igrac=(JSONObject)data.getJSONObject(i);
                                String username=igrac.getString("username");
                                String status=igrac.getString("status");
                                if(username.equals(PlayerUsername)){

                                }else {
                                    Log.d("status",status);
                                    igraciRV.add(new Player(username,status));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        pokreniRV();
                    }
                });

        }
    };

//    @Override
//    protected void onResume() {
//        super.onResume();
//        attemptSend();
//    }

    public void refList(){
        refreshListu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSend();
            }
        });
    }
    @Override
    protected void onStop() {
        // call the superclass method first
        super.onStop();
        mSocket.emit("igrac_napusta_igru", PlayerUsername+",1");

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.emit("igrac_napusta_igru", PlayerUsername+",1");
        mSocket.disconnect();
//        mSocket.off("new message", onNewMessage);
    }
    private void attemptSend() {
        String message = PlayerUsername;

        mSocket.emit("novi_igrac", message);
        mSocket.emit("igraci_spremni_zaigru",message);
    }






    public void pokreniRV(){
        IStartGame=new StartGameMethod() {
            @Override
            public void StartujIgru(String player1,String player2) {

               mSocket.emit("startGame",player1+","+player2);


            }
        };

        RecyclerView recyclerView;
        recyclerView = findViewById(R.id.RVIzaberiIgraca);
        IzaberiProtivnika_R_V_Adapter adapter=new IzaberiProtivnika_R_V_Adapter(this.igraciRV,this,PlayerUsername,IStartGame);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void sesija(){

        sessionManager.checkLogin();
        HashMap<String, String> user=sessionManager.getUserDetail();

        PlayerUsername=user.get("NAME");
    }



}
