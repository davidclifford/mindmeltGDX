package mindmelt.game.spells;

import mindmelt.game.engine.Engine;

public class StunSpell extends TimedSpell {
    static final long EXPIRY = 1L * 1000000000L;

    @Override
    public void activate(Engine engine) {
        init(engine, EXPIRY);
    }

    @Override
    public void update(Engine engine) {
        long elapsed = expiry - engine.getSystemTime();
        setActive(elapsed>0L);
        engine.getPlayer().setStun(isActive());
    }
}
