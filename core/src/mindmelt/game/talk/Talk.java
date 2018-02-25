package mindmelt.game.talk;

import java.util.ArrayList;
import java.util.List;

public class Talk {
    int id;
    List<Dialogue> dialogue;

    public Talk(int id) {
        this.id = id;
        this.dialogue = new ArrayList<>();
    }

    public Dialogue getReply(String word) {
        for(Dialogue d:dialogue) {
            if (d.isKeyword(word))
                return d;
        }
        return null;
    }

    public void addDialogue(Dialogue dia) {
        dialogue.add(dia);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Dialogue> getDialogue() {
        return dialogue;
    }

    public void setDialogue(List<Dialogue> dialogue) {
        this.dialogue = dialogue;
    }
}
