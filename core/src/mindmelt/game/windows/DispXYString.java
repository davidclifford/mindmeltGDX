package mindmelt.game.windows;

import com.badlogic.gdx.graphics.Color;

public class DispXYString {
    private int x;
    private int y;
    private String string;
    private Color colour;

    DispXYString(int x, int y, String string, Color colour) {
        this.x = x;
        this.y = y;
        this.string = string;
        this.colour = colour;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public Color getColour() {
        return colour;
    }

    public void setColour(Color colour) {
        this.colour = colour;
    }
}