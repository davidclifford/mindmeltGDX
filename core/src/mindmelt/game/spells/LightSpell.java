package mindmelt.game.spells;

import mindmelt.game.engine.Engine;

public class LightSpell extends TimedSpell {
    static final long EXPIRY = 5L * 60L * 1000000000L;

    @Override
    public void activate(Engine engine) {
        init(engine, EXPIRY);
    }

    @Override
    public void update(Engine engine) {
        long elapsed = expiry - engine.getSystemTime();
        setActive(elapsed>0L);
        if(!isActive()) return;
        float light = (EXPIRY-elapsed)/((float)EXPIRY);
        engine.getPlayer().setLight(light);
    }
}
