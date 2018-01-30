package mindmelt.game.spells;

import mindmelt.game.engine.Engine;
import mindmelt.game.objects.ObjPlayer;

public class DirectionSpell extends Spell {
    private static final String[] dirs = {"North","East","South","West"};

    @Override
    public void activate(Engine engine){
        ObjPlayer player = engine.getPlayer();
        int dir = player.getDirection();
        String direction = dirs[dir];
        String mess = String.format("You are facing %s", direction);
        engine.addMessage(mess);
    }
}
