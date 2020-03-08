package com.example.minesapper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class StartApp extends AppCompatActivity {

    Button igrajSam,logOut,btnLista,btnIgrajOnline;
    TextView rangPozicija;
    TextView imePrezime;
    SessionManager sessionManager;
    JSONArray rangListaArray;
    ArrayList<Player> users;
    boolean proveraBack=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_app);
        igrajSam=findViewById(R.id.igrajSam);
        rangPozicija=findViewById(R.id.Rang);
        imePrezime=findViewById(R.id.ImePrezime);
        logOut=findViewById(R.id.logOut);
        btnIgrajOnline=findViewById(R.id.igrajSNekim);
        sessionManager=new SessionManager(this);
        btnLista=findViewById(R.id.btnrang);

        sesija();

        igrajsam();
        igrajSnekim();
        LogOut();
        lista();
    }

    @Override
    public void onBackPressed() {
        if(proveraBack==false){
       return;
        }else{
            super.onBackPressed();
            proveraBack=false;
        }

    }

    public void sesija(){

        sessionManager.checkLogin();
        HashMap<String, String> user=sessionManager.getUserDetail();
        imePrezime.setText(user.get("IMEPREZIME"));

    }
    public void igrajsam(){
        igrajSam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(getBaseContext(),MainActivity.class);
                HashMap<String, String> user=sessionManager.getUserDetail();
                i.putExtra("username", user.get("NAME"));

                startActivity(i);

            }
        });
    }
    public void igrajSnekim(){
        btnIgrajOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(getBaseContext(),IzaberiProtivnika.class);
                startActivity(i);
            }
        });

    }
    public void lista(){
        btnLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                proveraBack= upaliListu();



            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        reset();


    }
    public void reset(){
        users=new ArrayList<>();
        HashMap<String, String> user=sessionManager.getUserDetail();
        setRang(user.get("NAME"));
    }

    public boolean upaliListu(){

        HashMap<String, String> user=sessionManager.getUserDetail();



        rangLIsta   fragmentList = new rangLIsta(getBaseContext(),users,user.get("NAME"));
        FragmentManager fm= getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fm.beginTransaction().setCustomAnimations(R.anim.slide_up,R.anim.slide_down);
        fragmentTransaction.replace(R.id.parentApp, fragmentList);

        fragmentTransaction.commit();
        fragmentTransaction.addToBackStack(null);


        return true;
    }
    public void setRangLista(JSONArray usersArray){
        for(int i =0; i<usersArray.length(); i++) {
            try {

                JSONObject clan = usersArray.getJSONObject(i);
                int x=i+1;

                String redni_br=   Integer.toString(x);
                users.add(new Player(redni_br,clan.getString("username"),clan.getString("najbolje_vreme")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    public void LogOut(){
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logout();
            }
        });
    }
    public void setRang(String username){

        dataIO controller1 = new dataIO(this);
        controller1.rangiranje(username, new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject response) {

                try {
                    JSONObject lista = response.getJSONObject("objekat");
                    String s=lista.getString("no");
                    rangPozicija.setText("NO."+s);
                    rangListaArray=lista.getJSONArray("lista");
                    setRangLista(rangListaArray);
                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        });

    }
}
