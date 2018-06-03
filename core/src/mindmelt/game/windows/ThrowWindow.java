package mindmelt.game.windows;

import com.badlogic.gdx.Gdx;
import mindmelt.game.engine.Engine;
import mindmelt.game.gui.Window;
import mindmelt.game.objects.Obj;
import mindmelt.game.objects.ObjPlayer;

public class ThrowWindow extends Window {
    public ThrowWindow(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    protected void activate(int x, int y, Engine engine) {
        Obj ob = engine.getPlayerHandObject();
        if(ob==null) return;

        ObjPlayer player = engine.getPlayer();
        int dir = player.getDirection();
        int sx = sgn(x/SZ-4);
        int sy = sgn(y/SZ-4);
        int xx = dir == 0 ? sx : dir == 1 ? -sy : dir == 2 ? -sx : sy;
        int yy = dir == 0 ? sy : dir == 1 ? sx : dir == 2 ? -sy : -sx;
        Gdx.app.log("ThrowWindow",String.format("x = %d, y = %d", xx, yy));
        engine.getPlayerInventory().handToMap(player.getX(),player.getY(),player.getZ(),engine);
        ob.setSpeed(1L);
        ob.throwObj(engine, xx, yy, 5);
    }
}
