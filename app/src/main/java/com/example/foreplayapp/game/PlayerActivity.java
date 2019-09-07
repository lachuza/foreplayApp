package com.example.foreplayapp.game;

public class PlayerActivity {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;
    private int time;
    private String text;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    private int imageId;

    public PlayerActivity() {
    }



    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }



    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
