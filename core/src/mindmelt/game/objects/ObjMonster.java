package mindmelt.game.objects;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import mindmelt.game.engine.Engine;
import mindmelt.game.engine.Message;
import mindmelt.game.maps.World;

public class ObjMonster extends Obj {

    public static final int SMELL_DIST = 5;
    private boolean hasBeenZapped = false;
    
    @Override
    public void update(Engine engine, float delta) {
        updateMessage(engine);

        if(isReady(delta)) {
            Obj player = engine.getObjects().getPlayer();
            if (((ObjPlayer) player).isStun()) return;
            int px = player.getX();
            int py = player.getY();

            int dx = x;
            int dy = y;

            int difx = px - x;
            int dify = py - y;
            if (difx * difx + dify * dify > SMELL_DIST * SMELL_DIST) {
                if (rand.nextInt(2) == 0) {
                    dx += rand.nextInt(2) * 2 - 1;
                } else {
                    dy += rand.nextInt(2) * 2 - 1;
                }
            } else {
                int nx = sign(px - x);
                int ny = sign(py - y);
                if (Math.abs(nx) + Math.abs(ny) == 2) {
                    if (rand.nextInt(2) == 0) {
                        nx = 0;
                    } else {
                        ny = 0;
                    }
                }
                dx += nx;
                dy += ny;
            }

            if(player.getX() == dx && player.getY() == dy && player.getZ() == z) {
                int hits = strength/10 + MathUtils.random(5);
                player.attack(engine, hits);
            }
            if (engine.canEnter(this, dx, dy, z)) {
                engine.moveObjToMap(this, dx, dy, z);
            } else {
                //engine.activateTile(dx, dy, z);
            }
        }
    }

    public void attack(Engine engine) {
        //attacked by player
        ObjPlayer player = engine.getPlayer();
        int str = player.getStrength();
        int hits = str / 10 + MathUtils.random(5);
        hits(engine, hits);
    }

    public void hits(Engine engine, int hits) {
        if(strength<=0) return; //already dead
        strength -= hits;
        strength = (strength<0) ? 0 : strength;
        Color colour = Color.GREEN;
        if(strength<20) colour = Color.YELLOW;
        if(strength<10) colour = Color.RED;
        String damage = ""+hits;
        setMessage(engine,damage,colour);
        if(isDead()) icon = DEAD_ICON;
    }

    public void zap(Engine engine) {
        if(hasBeenZapped) return;
        hits(engine, 5);
        hasBeenZapped = true;
    }

    public void setZapped(boolean hasBeenZapped) {
        this.hasBeenZapped = hasBeenZapped;
    }
}