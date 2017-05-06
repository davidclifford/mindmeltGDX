package mindmelt.game.gui;

import mindmelt.game.MindmeltGDX;

/**
 * Created by David on 3/05/2017.
 */
public class Window extends GuiElem {

    public Window(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    @Override
    protected void renderThis(MindmeltGDX game) {
        if (debug) rectangle(getAbsX(),getAbsY(),w,h,game);
    }
}
