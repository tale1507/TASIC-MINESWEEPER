package com.example.minesapper;

import android.widget.ImageView;

public class setGameMap {
    int table_row = 9;
    int table_cols = 9;
    static String prazan = "prazan";
    static String bomba = "bomba";
    static String otvoren = "otvoren";
    static String zatvoren = "zatvoren";
    ImageView kockica[][] = new ImageView[table_row][table_cols];
    String status[][] = new String[table_row][table_cols];
    String oZ[][]=new String [table_row][table_cols];


    public void setStatusI() {

        for (int i = 0; i < table_row; i++) {
            for (int j = 0; j < table_cols; j++) {
                status[i][j] = "1";
                oZ[i][j]=zatvoren;
            }
        }

        setbomba();

    }





    public void setbomba() {

    }




}
