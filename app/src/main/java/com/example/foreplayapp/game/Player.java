package com.example.foreplayapp.game;

import android.widget.ImageView;

public class Player {

    private String name;
    private int pos=0;
    private int lap=1;
    private String sex;


    public void setNextAction(String nextAction) {
        this.nextAction = nextAction;
    }

    private String nextAction;


    public Player(String name, String sex,ImageView imageView) {
        this.name = name;
        this.sex = sex;
        this.imageView = imageView;
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
    /**
     * @param sex the sex to set
     */
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



}
