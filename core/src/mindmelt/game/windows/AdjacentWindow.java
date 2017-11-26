package mindmelt.game.windows;

import com.badlogic.gdx.Gdx;
import mindmelt.game.MindmeltGDX;
import mindmelt.game.engine.Engine;
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
    protected void activate(int x, int y, Engine engine) {
        int xx = x / SZ - 1;
        int yy = y / SZ - 1;
        Gdx.app.log("AdjacentWindow", String.format("%d,%d", xx, yy));
        //Object ??
        int px = engine.getPlayerX();
        int py = engine.getPlayerY();
        int pz = engine.getPlayerZ();
        Obj topObj = engine.getTopObject(px + xx, py + yy, pz);
        //Pick up
        if (topObj != null && !topObj.isBlocked() && engine.getPlayerHandObject() == null) {
            engine.getPlayerInventory().objToHand(topObj,engine);

        // Drop
        } else if (engine.getPlayerHandObject()!=null && engine.canEnter(engine.getPlayerHandObject(),px+xx,py+yy,pz)) {
            if(topObj==null || (topObj!=null && !topObj.isBlocked())) {
                engine.getPlayerInventory().handToMap(px+xx,py+yy,pz,engine);
            }
        } else {
            Gdx.app.log("Activate tile ",String.format("%d,%d,%d",px+xx,py+yy,pz));
            engine.activateTile(px + xx, py + yy, pz);
        }
    }
}
