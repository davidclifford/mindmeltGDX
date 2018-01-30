package mindmelt.game.spells;

import com.badlogic.gdx.Gdx;
import mindmelt.game.engine.Engine;

public class LightSpell extends TimedSpell {
    static final float EXPIRY = 60*3f;

    @Override
    public void activate(Engine engine) {
        Gdx.app.log("timed spell","activate");
        init(engine, EXPIRY);
        //engine.getPlayer().setLight(1f);
    }

    @Override
    public void update(Engine engine) {
        super.update(engine);
        float elapsed = expiry - engine.getSystemTime();
        float light = 1f-elapsed/EXPIRY;
        if(light>0.95f) light = 0.95f;
        engine.getPlayer().setLight(light);
    }
}
