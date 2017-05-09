package mindmelt.game.windows;

import mindmelt.game.MindmeltGDX;
import mindmelt.game.gui.Window;

/**
 * Created by David on 9/05/2017.
 */
public class ViewWindow extends Window {

    public ViewWindow(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    @Override
    protected void renderThis(MindmeltGDX game, float delta) {
        super.renderThis(game,delta);
        for(int dy=-4;dy<5;dy++) {
            for(int dx=-4;dx<5;dx++) {
                double bright = (4f-Math.sqrt(dx*dx+dy*dy))/4f;
                drawTile(dx+4,dy+4,game.world.getTile(dx+40,dy+39,0).getIcon(),(float)bright,game);
            }
        }
    }
}
