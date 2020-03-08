package com.example.minesapper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ButtonBarLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;
import android.widget.Toolbar;

import org.json.JSONObject;

import java.lang.Math;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    int table_row = 9;
    int table_cols = 9;
    private static final String TAG = "MyActivity";

    static String prazan = "prazan";
    static String bomba = "bomba";
    static String otvoren = "otvoren";
    static String zatvoren = "zatvoren";
    public  int velicinax,velicinay;
    public int brojacZastavica=1;
    public int prebrojOtvorene=0;
    public int osnoviX;
    public int osnoviY;
    public boolean akoje=false;
    public int brojacPoteza=0;
    public boolean gameOver=false;
    private boolean running;
    private Chronometer timer;
    Button ponovi;
    ImageView kockica[][] = new ImageView[table_row][table_cols];
    String status[][] = new String[table_row][table_cols];
    String oZ[][]=new String [table_row][table_cols];
    private String username;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intent = getIntent();
        username=intent.getExtras().getString("username");
        ponovi=findViewById(R.id.ponovo);
        timer=findViewById(R.id.chronometer);
        velicinaK(table_row,table_cols);

        setStatusI();
        setButtonTable();
        setMrezuIgre();
        ponovi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });

    }


    public void velicinaK(int x,int y){

        if(x<10){
            velicinax=110;
            velicinay=110;
        }else if(x>=10 && x<16){
            velicinax=70;
            velicinay=70;
        }
        else if(x>=16 && x<21){
            velicinax=55;
            velicinay=55;
        }

    }

    public void setButtonTable() {

        TableLayout tableL = findViewById(R.id.tabela);

        for (int i = 0; i < table_row; i++) {
            Context context;
            final TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT

            ));
            tableL.addView(tableRow);

            for (int j = 0; j < table_cols; j++) {

                final int x = i;
                final int y = j;

                final ImageView btn = new ImageView(this);

                btn.setLayoutParams(new TableRow.LayoutParams(velicinax, velicinay));
                if ((j + i) % 2 == 0)
                    btn.setImageResource(R.mipmap.siva);
                else
                    btn.setImageResource(R.mipmap.sivamala);
                tableRow.addView(btn);

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(running==false){
                            startVreme(v);
                        }
                        if (brojacPoteza++ == 0) {
                            if(status[x][y]=="bomba"){

                                akoje=true;
                                osnoviX=x;
                                osnoviY=y;
                                setStatusI();
                                setMrezuIgre();
                                pokreniSeSad(x,y);
                            }else{
                                pokreniSeSad(x,y);
                            }

                        }else{
                            if (gameOver == true) {

                            } else {
                                pokreniSeSad(x, y);
                            }


                        }
                    }
                });
                btn.setOnLongClickListener(new View.OnLongClickListener() {
                   @Override
                   public boolean onLongClick(View v) {
                       brojacPoteza++;
                       if(gameOver==true)
                       {
                           Toast.makeText(getBaseContext(), "IZGUBILI STE", Toast.LENGTH_SHORT).show();

                       }else {

                           if (brojacZastavica > 10) {

                               if (oZ[x][y] == "zastavica") {
                                   zastavica(x, y);
                               }

                           } else {
                               zastavica(x, y);
                           }

                       }
                Log.e(TAG,Integer.toString(brojacZastavica));
                       return true;
                   }
               });
                        kockica[i][j] = btn;

            }

        }

    }


    public void zastavica(int x,int y){
        if(oZ[x][y]=="zastavica"){
            brojacZastavica--;
            oZ[x][y]=zatvoren;
            if ((x + y) % 2 == 0)
                kockica[x][y].setImageResource(R.mipmap.siva);

            else
                kockica[x][y].setImageResource(R.mipmap.sivamala);
        }else if(oZ[x][y]==zatvoren){

            kockica[x][y].setImageResource(R.drawable.zastavica);
            oZ[x][y] = "zastavica";
            brojacZastavica++;}


    }
    public void  cekthegame(){
            prebrojOtvorene=0;
        for(int i=0; i<table_row; i++){
            for(int j =0; j<table_cols; j++){
                if(oZ[i][j]==otvoren){
                    prebrojOtvorene++;
                }
            }
        }

        if((table_row*table_cols)-10==prebrojOtvorene) {
            gameOver=true;
        Toast.makeText(getBaseContext(), "Pobedili ste", Toast.LENGTH_SHORT).show();
            timer.stop();
            long time = SystemClock.elapsedRealtime() - timer.getBase();
            timeGameAndInsert(time);

            pozivFragmentOcena();
    }else if((table_row*table_cols)==prebrojOtvorene){
            Toast.makeText(getBaseContext(), "gameOver", Toast.LENGTH_SHORT).show();
            timer.stop();



            pozivFragmentOcena();
            }





}

    public void timeGameAndInsert(long time){
        int h   = (int)(time /3600000);
        int m = (int)(time - h*3600000)/60000;
        int s= (int)(time - h*3600000- m*60000)/1000 ;
        String hh = h < 10 ? "0"+h: h+"";
        String mm = m < 10 ? "0"+m: m+"";
        String ss = s < 10 ? "0"+s: s+"";

        HashMap data=new HashMap();
        data.put("username",username);
        data.put("sat",hh);
        data.put("minut",mm);
        data.put("sekund",ss);
        dataIO controller1 = new dataIO(this);
        controller1.insertTime(data, new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject result) {

            }
        });

    }

    public boolean pozivFragmentOcena(){


        rateUsF   fragmentList = new rateUsF();
        FragmentManager fm= getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fm.beginTransaction().setCustomAnimations(R.anim.slide_up,R.anim.slide_down);
        fragmentTransaction.replace(R.id.baseAc, fragmentList);

        fragmentTransaction.commit();
        fragmentTransaction.addToBackStack(null);

        return true;

    }
    public void startVreme(View v){
        if(!running){
            timer.setBase(SystemClock.elapsedRealtime());
            timer.start();
            running=true;
        }
    }
    public void sveaaa() {
        for (int x = 0; x < table_row; x++) {
            for (int y = 0; y < table_cols; y++) {
                if (status[x][y] == "bomba") {
                    kockica[x][y].setImageResource(R.drawable.bomba);
                } else if (status[x][y] == "prazan") {
                    kockica[x][y].setImageResource(R.mipmap.prazan);

                } else {
                    switch (status[x][y]) {
                        case "jedan":
                            kockica[x][y].setImageResource(R.drawable.jedan);
                            break;

                        case "dva":
                            kockica[x][y].setImageResource(R.drawable.dva);
                            break;

                        case "tri":
                            kockica[x][y].setImageResource(R.drawable.tri);
                            break;

                        case "cetri":
                            kockica[x][y].setImageResource(R.drawable.cetri);
                            break;

                        case "pet":
                            kockica[x][y].setImageResource(R.drawable.pet);
                            break;

                        case "sest":
                            kockica[x][y].setImageResource(R.drawable.sest);
                            break;

                        case "sedam":
                            kockica[x][y].setImageResource(R.drawable.sedam);
                            break;

                        case "osam":
                            kockica[x][y].setImageResource(R.drawable.osam);
                            break;
                        default:
                            Toast.makeText(this, "greska", Toast.LENGTH_SHORT).show();
                    }


                }
            }
        }
    }

    public void pokreniSeSad(int x,int y){

        if (oZ[x][y]=="zastavica")
        {

        }else if(status[x][y]=="bomba"){
        sveaaa();
        for(int i = 0; i<table_row;i++){
            for(int j = 0; j<table_row;j++){
                oZ[i][j]=otvoren;
            }
        }
        cekthegame();
        gameOver=true;

       }
       else if(status[x][y]=="prazan") {
           samOtvara(x, y);
           pokreniSe(x,y);
           cekthegame();

       }else{
           oZ[x][y]=otvoren;
           prebrojOtvorene++;


           pokreniSe(x,y);
           cekthegame();
       }


    }
    public void pokreniSe(int x, int y) {

        if (status[x][y] == "bomba") {
            kockica[x][y].setImageResource(R.drawable.bomba);
        } else if (status[x][y] == "prazan") {
            kockica[x][y].setImageResource(R.mipmap.prazan);



        } else {
            switch (status[x][y]) {
                case "jedan":
                    kockica[x][y].setImageResource(R.drawable.jedan);
                    break;

                case "dva":
                    kockica[x][y].setImageResource(R.drawable.dva);
                    break;

                case "tri":
                    kockica[x][y].setImageResource(R.drawable.tri);
                    break;

                case "cetri":
                    kockica[x][y].setImageResource(R.drawable.cetri);
                    break;

                case "pet":
                    kockica[x][y].setImageResource(R.drawable.pet);
                    break;

                case "sest":
                    kockica[x][y].setImageResource(R.drawable.sest);
                    break;

                case "sedam":
                    kockica[x][y].setImageResource(R.drawable.sedam);
                    break;

                case "osam":
                    kockica[x][y].setImageResource(R.drawable.osam);
                    break;
                default:
                    Toast.makeText(this, "greska", Toast.LENGTH_SHORT).show();
            }


        }

    }





    public void setStatusI() {

        for (int i = 0; i < table_row; i++) {
            for (int j = 0; j < table_cols; j++) {
                status[i][j] = "1";
               oZ[i][j]=zatvoren;
            }
        }

        setbomba();

    }


    int cekx[] = new int[table_row+1];
    int ceky[] = new int[table_cols+1];

    public void setbomba() {
        int x = 0;
        int y = 0;
        cekx[0] = -1;
        ceky[0] = -1;
        int i;
        for ( i = 0; i < 10; i++) {

            x = randomic(table_row);
            y = randomic(table_cols);




            boolean proveri = proveraI(cekx, ceky, x, y, i);


            if (proveri == true) {
                cekx[i] = x;
                ceky[i] = y;
                if(akoje==true) {

                    if (x == osnoviX && y == osnoviY) {
                        i--;

                    }else{
                        status[x][y] = "bomba";
                    }


                }else{
                    status[x][y] = "bomba";
                }
            } else {

                --i;

            }


        }
        akoje=false;

    }


    public boolean proveraI(int cekxx[], int cekyy[], int x, int y, int n) {

        boolean ceking = false;
        int xy = 0;
        for (int i = 0; i <= n; i++) {
            if (cekxx[i] == x && cekyy[i] == y) {
                ceking = false;

                break;

            } else {

                ceking = true;

            }

        }


        return ceking;


    }

    public int randomic(int broj) {

        int max = broj - 1;
        int min = 0;
        int range = max - min + 1;
        int rand = (int) (Math.random() * range) + min;
        return rand;

    }


    public void setMrezuIgre() {

        for (int i = 0; i < table_row; i++) {

            for (int j = 0; j < table_cols; j++) {
                if (status[i][j] == "bomba") {

                } else {
                    status[i][j] = setMrezuI(i, j, table_row);
                }
            }
        }


    }

    public String setMrezuI(int i, int j, int n) {

        String broj = "";
        if (i == 0 && j == 0) {
            broj = prebrojavac(0, 2, 1);

        } else if (i == 0 && j == (table_cols - 1)) {
            broj = prebrojavac(0, table_cols - 1, 2);
        } else if (i == table_cols - 1 && j == 0) {
            broj = prebrojavac(table_cols - 1, 0, 3);

        } else if (i == table_cols - 1 && j == table_cols - 1) {
            broj = prebrojavac(i, j, 4);

        } else if (i == 0) {
            broj = prebrojavac(i, j, 5);

        } else if (j == 0) {
            broj = prebrojavac(i, j, 6);

        } else if (i == table_cols - 1) {
            broj = prebrojavac(i, j, 7);

        } else if (j == table_cols - 1) {
            broj = prebrojavac(i, j, 8);

        } else {
            broj = prebrojavac(i, j, 9);

        }
        return broj;
    }


    public String prebrojavac(int od, int do_, int tip) {
        String broj = "";
        int brojac = 0;
        if (tip == 1) {
            for (int x = od; x < do_; x++) {
                for (int y = od; y < do_; y++) {
                    if (status[x][y] == "bomba") {
                        brojac++;
                    }
                }
            }
        } else if (tip == 2) {
            for (int x = od; x < od + 2; x++) {
                for (int y = do_ - 1; y < do_ + 1; y++) {
                    if (status[x][y] == "bomba") {
                        brojac++;
                    }
                }
            }

        } else if (tip == 3) {
            for (int x = od - 1; x < od + 1; x++) {
                for (int y = do_; y < do_ + 2; y++) {
                    if (status[x][y] == "bomba") {
                        brojac++;
                    }
                }
            }
        } else if (tip == 4) {
            for (int x = od - 1; x < do_ + 1; x++) {
                for (int y = od - 1; y < do_ + 1; y++) {
                    if (status[x][y] == "bomba") {
                        brojac++;
                    }
                }
            }

        } else if (tip == 5) {
            for (int x = od; x < 2; x++) {
                for (int y = do_ - 1; y < do_ + 2; y++) {
                    if (status[x][y] == "bomba") {
                        brojac++;
                    }
                }
            }
        } else if (tip == 6) {
            for (int x = od - 1; x < od + 2; x++) {
                for (int y = 0; y < 2; y++) {
                    if (status[x][y] == "bomba") {
                        brojac++;
                    }
                }
            }
        } else if (tip == 7) {

            for (int x = od - 1; x < od + 1; x++) {
                for (int y = do_ - 1; y < do_ + 2; y++) {
                    if (status[x][y] == "bomba") {
                        brojac++;
                    }
                }
            }
        } else if (tip == 8) {

            for (int x = od - 1; x < od + 2; x++) {
                for (int y = do_ - 1; y < do_ + 1; y++) {
                    if (status[x][y] == "bomba") {
                        brojac++;
                    }
                }
            }
        } else if (tip == 9) {

            for (int x = od - 1; x < od + 2; x++) {
                for (int y = do_ - 1; y < do_ + 2; y++) {
                    if (status[x][y] == "bomba") {
                        brojac++;
                    }
                }
            }
        }
        switch (brojac) {
            case 1:
                broj = "jedan";
                break;
            case 2:
                broj = "dva";
                break;
            case 3:
                broj = "tri";
                break;
            case 4:
                broj = "cetri";
                break;
            case 5:
                broj = "pet";
                break;
            case 6:
                broj = "sest";
                break;
            case 7:
                broj = "sedam";
                break;
            case 8:
                broj = "osam";
                break;
            default:
                broj = "prazan";
        }
        return broj;
    }

    public void samOtvara(int x, int y) {
        prebrojOtvorene++;
    oZ[x][y]=otvoren;
        cekthegame();

        vratiStanje(x,y);

    }




    public void vratiStanje(int i, int j) {

        if (i == 0 && j == 0) {
            prebrojavacI(0, 2, 1);

        } else if (i == 0 && j == (table_cols - 1)) {
           prebrojavacI(0, table_cols - 1, 2);
        } else if (i == table_cols - 1 && j == 0) {
            prebrojavacI(table_cols - 1, 0, 3);

        } else if (i == table_cols - 1 && j == table_cols - 1) {
            prebrojavacI(i, j, 4);

        } else if (i == 0) {
            prebrojavacI(i, j, 5);

        } else if (j == 0) {
          prebrojavacI(i, j, 6);

        } else if (i == table_cols - 1) {
             prebrojavacI(i, j, 7);

        } else if (j == table_cols - 1) {
           prebrojavacI(i, j, 8);

        } else {
            prebrojavacI(i, j, 9);

        }

    }

    public void prebrojavacI(int od, int do_, int tip) {



        if (tip == 1) {
            for (int x = od; x < do_; x++) {
                for (int y = od; y < do_; y++) {
                    if(oZ[x][y]==otvoren ||oZ[x][y]=="zastavica"){

                    }else  if (x == od && y == do_) {


                    } else if (status[x][y] == "bomba") {

                    } else if (status[x][y] == "prazan") {

                        pokreniSe(x,y);

                        samOtvara(x,y);
                    } else {
                        oZ[x][y]=otvoren;
                        prebrojOtvorene++;
                        pokreniSe(x,y);
                        cekthegame();
                    }
                }
            }
        } else if (tip == 2) {
            for (int x = od; x < od + 2; x++) {
                for (int y = do_ - 1; y < do_ + 1; y++) {
                    if(oZ[x][y]==otvoren ||oZ[x][y]=="zastavica"){

                    }else  if (x == od && y == do_) {


                    } else if (status[x][y] == "bomba") {

                    } else if (status[x][y] == "prazan") {

                        pokreniSe(x,y);

                        samOtvara(x,y);
                    } else {
                        oZ[x][y]=otvoren;
                        prebrojOtvorene++;
                        pokreniSe(x,y);
                        cekthegame();
                    }
                }
            }

        } else if (tip == 3) {
            for (int x = od - 1; x < od + 1; x++) {
                for (int y = do_; y < do_ + 2; y++) {
                    if(oZ[x][y]==otvoren ||oZ[x][y]=="zastavica"){

                    }else  if (x == od && y == do_) {


                    } else if (status[x][y] == "bomba") {

                    } else if (status[x][y] == "prazan") {

                        pokreniSe(x,y);

                        samOtvara(x,y);
                    } else { prebrojOtvorene++;
                        oZ[x][y]=otvoren;
                        pokreniSe(x,y);
                        cekthegame();
                    }
                }
            }
        } else if (tip == 4) {
            for (int x = od - 1; x < do_ + 1; x++) {
                for (int y = od - 1; y < do_ + 1; y++) {
                    if(oZ[x][y]==otvoren ||oZ[x][y]=="zastavica"){

                    }else  if (x == od && y == do_) {


                    } else if (status[x][y] == "bomba") {

                    } else if (status[x][y] == "prazan") {

                        pokreniSe(x,y);

                        samOtvara(x,y);
                    } else { prebrojOtvorene++;
                        oZ[x][y]=otvoren;
                        pokreniSe(x,y);
                        cekthegame();
                    }
                }
            }

        } else if (tip == 5) {
            for (int x = od; x < 2; x++) {
                for (int y = do_ - 1; y < do_ + 2; y++) {
                    if(oZ[x][y]==otvoren ||oZ[x][y]=="zastavica"){

                    }else  if (x == od && y == do_) {


                    } else if (status[x][y] == "bomba") {

                    } else if (status[x][y] == "prazan") {

                        pokreniSe(x,y);

                        samOtvara(x,y);
                    } else { prebrojOtvorene++;
                        oZ[x][y]=otvoren;
                        pokreniSe(x,y);
                        cekthegame();
                    }
                }
            }
        } else if (tip == 6) {
            for (int x = od - 1; x < od + 2; x++) {
                for (int y = 0; y < 2; y++) {
                    if(oZ[x][y]==otvoren ||oZ[x][y]=="zastavica"){

                    }else  if (x == od && y == do_) {


                    } else if (status[x][y] == "bomba") {

                    } else if (status[x][y] == "prazan") {

                        pokreniSe(x,y);

                        samOtvara(x,y);
                    } else { prebrojOtvorene++;
                        oZ[x][y]=otvoren;
                        pokreniSe(x,y);
                        cekthegame();
                    }
                }
            }
        } else if (tip == 7) {

            for (int x = od - 1; x < od + 1; x++) {
                for (int y = do_ - 1; y < do_ + 2; y++) {
                    if(oZ[x][y]==otvoren ||oZ[x][y]=="zastavica"){

                    }else  if (x == od && y == do_) {


                    } else if (status[x][y] == "bomba") {

                    } else if (status[x][y] == "prazan") {

                        pokreniSe(x,y);

                        samOtvara(x,y);
                    } else { prebrojOtvorene++;
                        oZ[x][y]=otvoren;
                        pokreniSe(x,y);
                        cekthegame();
                    }
                }
            }
        } else if (tip == 8) {

            for (int x = od - 1; x < od + 2; x++) {
                for (int y = do_ - 1; y < do_ + 1; y++) {
                    if(oZ[x][y]==otvoren ||oZ[x][y]=="zastavica"){

                    }else  if (x == od && y == do_) {


                    } else if (status[x][y] == "bomba") {

                    } else if (status[x][y] == "prazan") {

                        pokreniSe(x,y);

                        samOtvara(x,y);
                    } else { prebrojOtvorene++;
                        oZ[x][y]=otvoren;
                        pokreniSe(x,y);
                        cekthegame();
                    }
                }
            }
        } else if (tip == 9) {

            for (int x = od - 1; x < od + 2; x++) {
                for (int y = do_ - 1; y < do_ + 2; y++) {
                    if(oZ[x][y]==otvoren ||oZ[x][y]=="zastavica"){

                    }else  if (x == od && y == do_) {


                    } else if (status[x][y] == "bomba") {

                    } else if (status[x][y] == "prazan") {

                        pokreniSe(x,y);

                        samOtvara(x,y);
                    } else { prebrojOtvorene++;
                        oZ[x][y]=otvoren;
                        pokreniSe(x,y);
                        cekthegame();
                    }
                }
            }
        }

    }


}