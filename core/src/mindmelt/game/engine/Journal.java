package mindmelt.game.engine;

import com.badlogic.gdx.graphics.Color;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Journal {

    private List<String> textStore = new ArrayList<>();
    private List<Color> textColour = new ArrayList<>();
    private Set<String> text = new LinkedHashSet<>();
    private int currentLine = 0;

    public void addLine(String line, Color colour) {
        if(text.contains(line)) return;

        textStore.add(line);
        textColour.add(colour);
        text.add(line);
        currentLine++;
    }

    public void updateCurrentLine(String line, Color colour) {
        textStore.set(currentLine-1, line);
        textColour.set(currentLine-1, colour);
    }

    public String getTextLine(int line) {
        int relLine = currentLine-12+line;
        if(relLine<0 || relLine>currentLine) return "";
        return textStore.get(relLine);
    }

    public Color getTextColor(int line) {
        int relLine = currentLine-12+line;
        if(relLine<0 || relLine>currentLine) return Color.WHITE;
        return textColour.get(relLine);
    }

    public void scrollUp() {
        if(currentLine==1) return;
        currentLine--;
    }
    public void scrollDown() {
        if(currentLine>=textStore.size()) return;
        currentLine++;
    }

    public void loadJournal(String name) {
        textStore = new ArrayList<>();
        textColour = new ArrayList<>();
        text = new LinkedHashSet<>();
        currentLine = 0;

        String filename = "data/" + name + ".jou";
        String line;
        BufferedReader input;

        try {
            input = new BufferedReader((new FileReader(filename)));
            boolean firstLine = true;
            while ((line = input.readLine()) != null) {
                if (line.length() == 0) continue;
                if (line.startsWith("//")) continue;
                if(firstLine) {
                    currentLine = Integer.parseInt(line);
                    firstLine = false;
                } else {
                    String tex = line.substring(8);
                    String colour = line.substring(0,8);
                    textStore.add(tex);
                    int color = Integer.parseUnsignedInt(colour,16);
                    textColour.add(new Color(color));
                    text.add(tex);
                }
            }
            input.close();
        } catch (Exception e) {
            System.out.println("Error: " + e);
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void saveJournal(String name) {
        String filename = "data/" + name + ".jou";
        BufferedWriter output;
        try {
            output = new BufferedWriter((new FileWriter(filename)));
            output.write(String.format("%s\n",currentLine));
            for(int i=0; i<textStore.size(); i++) {
                String line = textStore.get(i);
                String color = textColour.get(i).toString();
                output.write(String.format("%s%s\n",color,line));
            }
            output.close();
        } catch (Exception e) {
            System.out.println("Error: " + e);
            e.printStackTrace();
            System.exit(1);
        }
    }
}
