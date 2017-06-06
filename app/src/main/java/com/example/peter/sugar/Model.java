package com.example.peter.sugar;

/**
 * Created by Funkyaron on 06.06.2017.
 */

public class Model {

    private String name;
    private long id;
    private boolean selected;

    public Model(String name, long id) {
        this.name = name;
        this.id = id;
        selected = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
