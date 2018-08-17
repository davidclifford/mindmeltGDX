package mindmelt.game.windows;

import com.badlogic.gdx.Gdx;
import mindmelt.game.engine.Engine;
import mindmelt.game.gui.Window;
import mindmelt.game.objects.Obj;
import mindmelt.game.objects.ObjPlayer;

public class ThrowWindow extends Window {

    private int dx;
    private int dy;

    public ThrowWindow(int x, int y, int w, int h, int dx, int dy) {
        super(x, y, w, h);
        this.dx = dx;
        this.dy = dy;
    }

    protected void activate(int x, int y, Engine engine) {
        Obj ob = engine.getPlayerHandObject();
        if(ob==null) return;

        ObjPlayer player = engine.getPlayer();
        int dir = player.getDirection();
        int xx = dir == 0 ? dx : dir == 1 ? -dy : dir == 2 ? -dx : dy;
        int yy = dir == 0 ? dy : dir == 1 ? dx : dir == 2 ? -dy : -dx;
        engine.getPlayerInventory().handToMap(player.getX(),player.getY(),player.getZ(),engine);
        ob.setSpeed(1L);
        ob.throwObj(engine, xx, yy, 5);
    }
}
