package com.example.foreplayapp.game;

import android.widget.ImageView;
import android.widget.TextView;

import com.example.foreplayapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Game {
    private Player playerMale;
    private Player playerFemale;

    private Player currentPlayer;
    private Player nextPlayer;

    private HashMap<Integer, ArrayList<Integer>> actividades;


    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getNextPlayer() {
        return nextPlayer;
    }

    public Player getPlayerMale() {
        return playerMale;
    }

    public Player getPlayerFemale() {
        return playerFemale;
    }

    private int cantidad=32;

    private String[] board = {"HEARTS","DICES","BED","PICKUP","BED","GOBACKN","BED","PICKUPOTHER","DICES","GOBACK","PICKUP","GOBACKN","BED","PICKUPOTHER","DICES","GOBACKN","PICKUP","GOBACK","BED","GOBACK","PICKUPOTHER","GOBACKN","DICES","PICKUP","BED","GOBACK","PICKUPOTHER","GOBACKN","DICES","BED","PICKUP","BED"};


    public Game(String male,String female,ImageView playerMaleImg, ImageView playerFemaleImg, TextView maleText, TextView femaleText) {
        playerMale = new Player(male,"MALE",playerMaleImg,maleText);
        playerFemale = new Player(female,"FEMALE",playerFemaleImg,femaleText);
        Random rng=new Random();
        int actNumber=rng.nextInt(2);
        if (actNumber==0){
            currentPlayer = playerMale;
            nextPlayer = playerFemale;
        }else{
            currentPlayer = playerFemale;
            nextPlayer = playerMale;
        }

        actividades = new HashMap<Integer, ArrayList<Integer>>();
        actividades.put(new Integer(1),new ArrayList<Integer>());
        actividades.put(new Integer(2),new ArrayList<Integer>());
        actividades.put(new Integer(3),new ArrayList<Integer>());
        actividades.put(new Integer(4),new ArrayList<Integer>());

    }

    public void toogleTurn() {
        Player aux;
        aux=currentPlayer;
        currentPlayer=nextPlayer;
        nextPlayer=aux;

    }

    private int calculateNextPosition(int moves){
        int nextPos=currentPlayer.getPos()+moves;
        if (nextPos>=cantidad){
            nextPos=nextPos-cantidad;
            currentPlayer.setLastPos(currentPlayer.getPos());
            currentPlayer.setPos(nextPos);
            currentPlayer.setLap(currentPlayer.getLap()+1);
        }else{
            currentPlayer.setLastPos(currentPlayer.getPos());
            currentPlayer.setPos(nextPos);
        }

        return nextPos;
    }

    public Player play(int dice){
        int newPos=calculateNextPosition(dice);
        currentPlayer.setNextAction(board[newPos]);
        return currentPlayer;

    }

    public Player goBack(){
        for (int i=currentPlayer.getPos();i>-1;i--){
            if ("BED".equals(board[i])){
                currentPlayer.setLastPos(currentPlayer.getPos());
                currentPlayer.setPos(i);
                break;
            }
        }
        currentPlayer.setNextAction(board[currentPlayer.getPos()]);
        return currentPlayer;
    }

    public Player goBackN(int moves){
        int nextPos=currentPlayer.getPos()-moves;
        if ((nextPos<=0)&&(currentPlayer.getLap()==1)){
            nextPos=0;
            currentPlayer.setNextAction("NADA");
            currentPlayer.setLastPos(currentPlayer.getPos());
            currentPlayer.setPos(nextPos);
        }else if (nextPos<0){
            nextPos=nextPos+cantidad;
            currentPlayer.setLastPos(currentPlayer.getPos());
            currentPlayer.setPos(nextPos);
            currentPlayer.setLap(currentPlayer.getLap()-1);
            currentPlayer.setNextAction(board[nextPos]);
        }else{
            currentPlayer.setLastPos(currentPlayer.getPos());
            currentPlayer.setPos(nextPos);
            currentPlayer.setNextAction(board[nextPos]);
        }


        return currentPlayer;
    }

    public int getNextActivity(){
        Random rng=new Random();
        int level = getCurrentPlayer().getLap();
        if (level>4) level=4;

        while (true){
            int actNumber=rng.nextInt(12);
            ArrayList<Integer> activitys= (actividades.get(new Integer(level)));
            if (activitys.size()==12){
                activitys = new ArrayList<>();
            }
            if (!activitys.contains(new Integer(actNumber))){
                activitys.add(new Integer(actNumber));
                return actNumber;
            }
        }




    }



}
