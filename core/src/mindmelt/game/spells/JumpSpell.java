package mindmelt.game.spells;

import com.badlogic.gdx.graphics.Color;
import mindmelt.game.engine.Engine;
import mindmelt.game.engine.Message;
import mindmelt.game.maps.TileType;
import mindmelt.game.objects.ObjPlayer;

public class JumpSpell extends Spell {
    private static final int MAXJUMP = 3;

    @Override
    public void activate(Engine engine) {
            jumpForward(engine);
    }

    private void jumpForward(Engine engine) {
        int vx[] = {0,1,0,-1};
        int vy[] = {-1,0,1,0};
        ObjPlayer player = engine.getPlayer();
        int x = player.getX();
        int y = player.getY();
        int z = player.getZ();
        int dir = player.getDirection();
        for(int i=MAXJUMP;i>0;i--) {
            int jx = x+vx[dir]*i;
            int jy = y+vy[dir]*i;
            TileType tile = engine.getTile(jx,jy,z);
            if(tile == TileType.grass && (engine.getTopObject(jx,jy,z)==null || !engine.getTopObject(jx,jy,z).isBlocked())) {
                player.moveToMap(jx,jy,z,engine);
                //zing
                return;
            }
        }
        Color red = new Color(0.7f,0,0,1);
        Message mess = new Message(x,y,z,"Can't Jump here", red,1000L);
        engine.addMessage(mess);
    }
}
