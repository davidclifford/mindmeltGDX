package mindmelt.game.windows;

import mindmelt.game.MindmeltGDX;
import mindmelt.game.engine.Journal;
import mindmelt.game.gui.Window;

public class JournalWindow extends Window {



    public JournalWindow(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    protected void renderThis(MindmeltGDX game, float delta) {
        Journal text = game.engine.getJournal();
        for (int line=0; line<12; line++)
        {
            drawText(0,line*SZ/3, text.getTextColor(line),text.getTextLine(line),game);
        }
    }
}