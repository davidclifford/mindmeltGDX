package mindmelt.game.spells;

import mindmelt.game.engine.Engine;
import mindmelt.game.objects.ObjPlayer;

public class MapSpell extends Spell {
    public void activate(Engine engine) {
        ObjPlayer player = engine.getPlayer();
        player.setMapActive(true);
        setActive(true);
    }
    public void reset(Engine engine) {
        ObjPlayer player = engine.getPlayer();
        player.setMapActive(false);
        setActive(false);
    }
}
