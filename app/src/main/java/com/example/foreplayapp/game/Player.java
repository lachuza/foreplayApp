package com.example.foreplayapp.game;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class Player {

    private String name;
    private int pos=0;



    private int lastPos=0;
    private int lap=3;
    private String sex;



    public void setNextAction(String nextAction) {
        this.nextAction = nextAction;
    }

    private String nextAction;

    public TextView getNameText() {
        return nameText;
    }

    private TextView nameText;


    public Player(String name, String sex,ImageView imageView,TextView nameText) {
        this.name = name;
        this.sex = sex;
        this.imageView = imageView;
        this.nameText = nameText;
        nameText.setText(name);

    }


    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return the pos
     */
    public int getPos() {
        return pos;
    }
    /**
     * @param pos the pos to set
     */
    public void setPos(int pos) {
        this.pos = pos;
    }
    /**
     * @return the lap
     */
    public int getLap() {
        return lap;
    }
    /**
     * @param lap the lap to set
     */
    public void setLap(int lap) {
        this.lap = lap;
    }
    /**
     * @return the sex
     */
    public String getSex() {
        return sex;
    }


    public int getLastPos() {
        return lastPos;
    }

    public void setLastPos(int lastPos) {
        this.lastPos = lastPos;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    private ImageView imageView;

    public String getNextAction() {
        return nextAction;
    }

    public boolean isCorner(){
        if ((lastPos<7)&&(pos>7)) return true;
        if ((lastPos<16)&&(pos>16)) return true;
        if ((lastPos<23)&&(pos>23)) return true;
        if ((lastPos<0)&&(pos>0)) return true;
        return false;
    }



}
