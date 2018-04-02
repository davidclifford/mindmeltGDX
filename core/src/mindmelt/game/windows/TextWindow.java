package mindmelt.game.windows;

import mindmelt.game.MindmeltGDX;
import mindmelt.game.engine.TextLines;
import mindmelt.game.gui.Window;

public class TextWindow extends Window {



    public TextWindow(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    protected void renderThis(MindmeltGDX game, float delta) {
        TextLines text = game.engine.getTextLines();
        for (int line=0; line<10; line++)
        {
            drawText(0,line*SZ/3, text.getTextColor(line),text.getTextLine(line),game);
        }
    }
}