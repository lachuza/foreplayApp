package com.example.foreplayapp.game;

import android.widget.ImageView;

public class Game {
    private Player playerMale;
    private Player playerFemale;

    private Player currentPlayer;
    private Player nextPlayer;

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


    public Game(ImageView playerMaleImg, ImageView playerFemaleImg) {
        playerMale = new Player("RULO","HOMBRE",playerMaleImg);
        playerFemale = new Player("CELE","MUJER",playerFemaleImg);
        currentPlayer = playerMale;
        nextPlayer = playerFemale;
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
        if ((nextPos<0)&&(currentPlayer.getLap()==1)){
            nextPos=0;
            currentPlayer.setNextAction("NADA");
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



}
