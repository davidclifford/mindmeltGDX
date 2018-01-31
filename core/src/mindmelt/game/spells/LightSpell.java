package mindmelt.game.spells;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;
import mindmelt.game.engine.Engine;

public class LightSpell extends TimedSpell {
    static final long EXPIRY = 5L * 60L * 1000000000L;

    @Override
    public void activate(Engine engine) {
        init(engine, EXPIRY);
        setActive(true);
    }

    @Override
    public void update(Engine engine) {
        long elapsed = expiry - engine.getSystemTime();
        if(elapsed<0L) {
            setActive(false);
            return;
        }
        float light = (EXPIRY-elapsed)/((float)EXPIRY);
        engine.getPlayer().setLight(light);

    }
}
