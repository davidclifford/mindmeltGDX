package mindmelt.game.spells;

import mindmelt.game.engine.Engine;

public class TimedSpell extends Spell {
    protected float expiry;

    @Override
    public void update(Engine engine) {
        setActive(engine.getSystemTime()<expiry);
    }

    protected void init(Engine engine, float time) {
        expiry = engine.getSystemTime()+time;
    }
}
