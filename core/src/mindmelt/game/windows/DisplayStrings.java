package mindmelt.game.windows;

import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;
import java.util.List;

public class DisplayStrings {

    private List<DispXYString> dispXYStrings;

    public DisplayStrings() {
        dispXYStrings = new ArrayList<>();
    }

    public void add(int x, int y, String message, Color colour) {
        if(isAlreadyMessageAt(x,y)) {
            y--;
        }
        DispXYString xyString = new DispXYString(x,y,message,colour);
        dispXYStrings.add(xyString);
    }

    public List<DispXYString> getDispXYStrings() {
        return dispXYStrings;
    }

    public boolean isAlreadyMessageAt(int x, int y) {
        for(DispXYString xys : dispXYStrings)
        {
            if(xys.getX()==x && xys.getY()==y) {
                return true;
            }
        }
        return false;
    }
}
