package mindmelt.game.objects;


import mindmelt.game.engine.Engine;
import mindmelt.game.maps.World;

public class ObjMonster extends Obj {

    public static final int SMELL_DIST = 5;
    
    @Override
    public void update(Engine engine, int delta) {
        Obj player = engine.getObjects().getPlayer();
        int px = player.getX();
        int py = player.getY();
        ObjMonster mon = this;
        int dx = x;
        int dy = y;
        
        int difx = px - x;
        int dify = py - y;
        if(difx*difx+dify*dify > SMELL_DIST*SMELL_DIST) {
            if (rand.nextInt(2)==0) {
                dx += rand.nextInt(2)*2-1;
            } else {
                dy += rand.nextInt(2)*2-1;
            }
        } else {
            int nx = sign(px-x);
            int ny = sign(py-y);
            if (Math.abs(nx) + Math.abs(ny) == 2) {
                if (rand.nextInt(2)==0) {
                    nx = 0;
                } else {
                    ny = 0;
                }
            }
            dx += nx;
            dy += ny;
        }
        if(isReady(delta)) {
            if(engine.canEnter(this,dx,dy,z)) {
                engine.moveObjToMap(this, dx, dy, z);
            } else {
                engine.activateTile(dx, dy, z);
            }
        }
    }
    
    private int sign(int s) {
        return (s<0) ? -1 : (s>0) ? 1 : 0;
    }
}