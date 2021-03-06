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
    protected void renderThis(MindmeltGDX game, float delta) {
        if (debug) rectangle(0,0,w,h,game);
    }


    protected int sgn(int a) {
        return a < 0 ? -1 : (a > 0 ? 1 : 0);
    }
}
