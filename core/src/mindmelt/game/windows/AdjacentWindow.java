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
        int dir = engine.getPlayerDirection();
        int x1 = x / SZ - 1;
        int y1 = y / SZ - 1;
        int xx = dir==0 ? x1 : dir==3 ? y1 : dir==2 ? -x1 : -y1;
        int yy = dir==0 ? y1 : dir==3 ? -x1 : dir==2 ? -y1 : x1;
        //Object ??
        int px = engine.getPlayerX()+xx;
        int py = engine.getPlayerY()+yy;
        int pz = engine.getPlayerZ();
        Gdx.app.log("AdjacentWindow", String.format("%d,%d", px, py));
        Obj topObj = engine.getTopObject(px, py, pz);
        //Pick up
        if (topObj != null && !topObj.isBlocked() && engine.getPlayerHandObject() == null) {
            int fx = topObj.getX();
            int fy = topObj.getY();
            int fz = topObj.getZ();
            engine.getPlayerInventory().objToHand(topObj,engine);
            engine.fromTo(fx,fy,fz,0,0,0);
        // Drop
        } else if (engine.getPlayerHandObject()!=null && engine.canEnter(engine.getPlayerHandObject(),px,py,pz)) {
            if(topObj==null || (topObj!=null && !topObj.isBlocked())) {
                engine.getPlayerInventory().handToMap(px,py,pz,engine);
                engine.fromTo(0,0,0, px, py, pz);
            }
        } else if( topObj != null) {
            if (topObj.isMonster()){
                //attack monster
                topObj.attack(engine);
            } else if(topObj.isPerson()){
                topObj.talkTo(engine);
                //talk to person
            } else if( topObj!=null && topObj.isAnimal()) {
                //animal
            }
        } else {
            Gdx.app.log("Activate tile ",String.format("%d,%d,%d",px,py,pz));
            engine.activateTile(px, py, pz);
        }
    }
}
