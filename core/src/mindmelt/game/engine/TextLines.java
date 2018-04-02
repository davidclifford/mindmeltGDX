package mindmelt.game.engine;

import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;
import java.util.List;

public class TextLines {

    private List<String> textStore = new ArrayList<>();
    private List<Color> textColour = new ArrayList<>();
    private int currentLine = 0;

    public void addLine(String line, Color colour) {
        textStore.add(line);
        textColour.add(colour);
        currentLine++;
    }

    public void updateCurrentLine(String line, Color colour) {
        textStore.set(currentLine-1, line);
        textColour.set(currentLine-1, colour);
    }

    public String getTextLine(int line) {
        int relLine = currentLine-10+line;
        if(relLine<0 || relLine>currentLine) return "";
        return textStore.get(relLine);
    }

    public Color getTextColor(int line) {
        int relLine = currentLine-10+line;
        if(relLine<0 || relLine>currentLine) return Color.WHITE;
        return textColour.get(relLine);
    }
}
