package mindmelt.game.spells;

import mindmelt.game.engine.Engine;
import mindmelt.game.objects.ObjPlayer;

public class BackSpell extends Spell {
    public void activate(Engine engine) {
        ObjPlayer player = engine.getPlayer();
        player.setBack(engine);
        setActive(true);
    }
    public void reset(Engine engine) {
        ObjPlayer player = engine.getPlayer();
        player.goBack(engine);
        setActive(false);
    }
}
