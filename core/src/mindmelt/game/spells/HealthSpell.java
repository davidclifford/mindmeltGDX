package mindmelt.game.spells;

import mindmelt.game.engine.Engine;

public class HealthSpell extends Spell {
    public void activate(Engine engine) {
        engine.getPlayer().setHealthMax();
        engine.addMessage("Health restored");
    }
}
