package mindmelt.game.spells;

import mindmelt.game.engine.Engine;
import mindmelt.game.objects.ObjPlayer;

public class CoordsSpell extends Spell {
    @Override
    public void activate(Engine engine) {
        ObjPlayer player = engine.getPlayer();
        String mess = String.format("Your coords are %d,%d,%d",player.getX(),player.getY(),player.getZ());
        engine.addMessage(mess);
    }
}
