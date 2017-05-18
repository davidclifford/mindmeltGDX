package mindmelt.game.windows;

import com.badlogic.gdx.Gdx;
import mindmelt.game.MindmeltGDX;
import mindmelt.game.gui.Window;
import mindmelt.game.objects.Obj;

/**
 * Created by David on 18/05/2017.
 */
public class AdjacentWindow extends Window {
    public AdjacentWindow(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    @Override
    protected void activate(int x, int y, MindmeltGDX game) {
        int xx = x/SZ - 1;
        int yy = y/SZ - 1;
        Gdx.app.log("AdjacentWindow",String.format("%d,%d",xx,yy));
        //Object ??
        int px = game.player.getX();
        int py = game.player.getY();
        int pz = game.player.getZ();
        Obj topObj = game.world.getTopObject(px+xx,py+yy,pz);
        if (topObj!=null && !topObj.isBlocked()) {
            topObj.moveToObject(game.player,game.world);
        }
    }
}
