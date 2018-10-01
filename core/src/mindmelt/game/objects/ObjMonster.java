package mindmelt.game.objects;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import mindmelt.game.engine.Engine;
import mindmelt.game.maps.TileType;

public class ObjMonster extends Obj {

    public static final int SMELL_DIST = 5;
    private boolean hasBeenZapped = false;
    
    @Override
    public void update(Engine engine, float delta) {
        updateMessage(engine);
        if(isReady(engine) && !isDead()) {
            ObjPlayer player = (ObjPlayer)engine.getObjects().getPlayer();
            if (player.isStun()) return;

            int px = player.getX();
            int py = player.getY();

            int dx = x;
            int dy = y;
            int smellDist = player.getSmellDist(x, y, z);
            int sd;
            boolean r = true;
            if((sd=player.getSmellDist(x+1, y, z)) < smellDist) {
                dx = x+1;
                dy = y;
                smellDist = sd;
                r = false;
            }
            if((sd=player.getSmellDist(x-1, y, z)) < smellDist || (sd==smellDist && rand.nextBoolean())) {
                dx = x-1;
                dy = y;
                smellDist = sd;
                r = false;
            }
            if((sd=player.getSmellDist(x, y+1, z)) < smellDist || (sd==smellDist && rand.nextBoolean())) {
                dx = x;
                dy = y+1;
                smellDist = sd;
                r = false;
            }
            if((sd=player.getSmellDist(x, y-1, z)) < smellDist || (sd==smellDist && rand.nextBoolean())) {
                dx = x;
                dy = y-1;
                r = false;
            }

            if(r) {
                if (rand.nextInt(2) == 0) {
                    dx += rand.nextInt(2) * 2 - 1;
                } else {
                    dy += rand.nextInt(2) * 2 - 1;
                }
            }
/*
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
*/
            if(player.getX() == dx && player.getY() == dy && player.getZ() == z) {
                int hits = strength/10 + MathUtils.random(5);
                player.attack(engine, hits);
            }
            if (engine.canEnter(this, dx, dy, z)) {
                engine.moveObjToMap(this, dx, dy, z);
            } else {
                //engine.activateTile(dx, dy, z);
            }
            resetWait(engine);
        }

        //regenerating monsters
        if(isDead() && mapId==1) {
            ObjPlayer player = (ObjPlayer)engine.getObjects().getPlayer();
            if(abs(player.getX()-x)>4 || abs(player.getY()-y)>4) {
                int nx = rand.nextInt(80);
                int ny = rand.nextInt(80);
                if (engine.getTopObject(nx, ny, 0) == null && engine.getTile(nx, ny, getZ()) == TileType.grass) {
                    setStrength(rand.nextInt(20) + 10);
                    setSpeed(rand.nextInt(5) + 5);
                    setIcon(rand.nextInt(17) + 62);
                    engine.moveObjToMap(this, nx, ny, getZ());
                    resetWait(engine);
                }
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
        if(isDead()) return; //already dead
        if(!isOmgra()) strength -= hits;
        strength = (strength<0) ? 0 : strength;
        Color colour = Color.GREEN;
        if(strength<20) colour = Color.YELLOW;
        if(strength<10) colour = Color.RED;
        String damage = ""+hits;
        setMessage(engine,damage,colour);
        if(isDead()) {
            icon = DEAD_ICON;
            unloadObjects(engine);
        }
    }

    public void zap(Engine engine) {
        if(hasBeenZapped) return;
        hits(engine, 5+rand.nextInt(engine.getPlayer().getLevel()));
        hasBeenZapped = true;
    }

    public void setZapped(boolean hasBeenZapped) {
        this.hasBeenZapped = hasBeenZapped;
    }

}