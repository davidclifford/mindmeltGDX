package mindmelt.game.spells;

import mindmelt.game.engine.Engine;
import mindmelt.game.maps.EntryExit;

public class CircleSpell extends Spell {
    public void activate(Engine engine) {
        EntryExit circle = new EntryExit(0, 0, 0, 40, 39, 0, "world", "Circle");
        engine.moveToMap(circle);
        engine.getPlayer().setHealthMax();
        engine.addMessage("Back to the Stone Circle");
    }
}
