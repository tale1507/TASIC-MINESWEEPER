package com.example.minesapper;

public class Player {
    public String RedniBroj;
    public String Username;
    public String OnlineStatus;
    public String Vreme;

    public Player(String redniBroj,String username,  String vreme) {
        RedniBroj=redniBroj;
        Username = username;

        Vreme = vreme;
    }

    public Player(String Username,String OnlineStatus) {
        this.Username =Username;
        this.OnlineStatus=OnlineStatus;
    }
}
