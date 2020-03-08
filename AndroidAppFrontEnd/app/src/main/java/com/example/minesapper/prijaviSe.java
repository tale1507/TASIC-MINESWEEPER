package com.example.minesapper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class prijaviSe extends AppCompatActivity {
    TextInputEditText korisnicko_ime,password;
    Button click2Log;
    TextView obavesenje,forgotPass,registrujSe;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prijavi_se);
    // region setovanje
        korisnicko_ime=findViewById(R.id.username);
        password=findViewById(R.id.password);
        click2Log=findViewById(R.id.prijaviSe);
        forgotPass=findViewById(R.id.forgotPass);
        obavesenje=findViewById(R.id.ObavestenjePrijava);
        registrujSe=findViewById(R.id.registrujClick);
        sessionManager=new SessionManager(this);


        //endregion
        click2Log.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String user=  korisnicko_ime.getText().toString();
                String pass=password.getText().toString();
                if(user.isEmpty()||pass.isEmpty()||(user.isEmpty()&&pass.isEmpty())){

                        obavesenje.setText("Popunite polja");
                }else{
                    prijavi_Se(user,pass);

                }
            }
        });

        registrujSe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrujSe.setTextColor(v.getResources().getColor(R.color.correct));

                Intent myIntent = new Intent(v.getContext(), Singup_form.class);
                startActivity(myIntent);

//                registrujSe.setTextColor(v.getResources().getColor(R.color.colorPrimaryDark));

            }
        });

    }

    public void prijavi_Se(String user,String pass){
        proveriIIPrijavi(user, pass);


    }

    public void proveriIIPrijavi(String user,String pass){
        dataIO controller1 = new dataIO(this);
        controller1.PrijaviSe(user, pass, new ServerCallBack() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        try {

                            JSONArray jsonArray = response.getJSONArray("broj");
                            JSONObject clan = jsonArray.getJSONObject(0);
                            String jsonO= clan.getString("odgovor");
                            JSONObject reader = new JSONObject(jsonO);
                            String status,odgovor;
                            String username,email,imePrezime;
                            status=reader.getString("status");
                            JSONArray userA=reader.getJSONArray("odg");
                            JSONObject user=userA.getJSONObject(0);
                            username=user.getString("username");
                            email=user.getString("email");
                            imePrezime=user.getString("ime_prezime");
                            switch (status){
                                case "OK":
                                    sessionManager.createSession(username,email,imePrezime);
                                    Intent i = new Intent(getBaseContext(), StartApp.class);
                                   startActivity(i);
                                    break;
                                case "username":
                                    obavesenje.setText("Korisnicko ime ne postoji");
                                    break;
                                case "lozinka":
                                    obavesenje.setText("Uneta lozinka nije tacna");
                                    break;


                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }
}
