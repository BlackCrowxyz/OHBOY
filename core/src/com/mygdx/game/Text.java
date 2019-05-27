package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

public class Text {
    private Vector2 pos;
    private String line;

    Text(float x, float y, String line) {
        pos = new Vector2(x, y);
        this.line = line;
    }

    public String getLine() {
        return line;
    }

    public float getX() {
        return pos.x;
    }

    public float getY() {
        return pos.y;
    }

}
