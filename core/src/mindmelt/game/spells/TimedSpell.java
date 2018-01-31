package mindmelt.game.spells;

import mindmelt.game.engine.Engine;

public class TimedSpell extends Spell {
    protected long expiry;

    @Override
    public void update(Engine engine) {
        setActive(engine.getSystemTime()<expiry);
    }

    protected void init(Engine engine, long time) {
        expiry = engine.getSystemTime()+time;
    }
}
