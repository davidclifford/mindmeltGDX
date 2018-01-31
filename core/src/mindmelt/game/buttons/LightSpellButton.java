package mindmelt.game.buttons;

import com.badlogic.gdx.Gdx;
import mindmelt.game.engine.Engine;
import mindmelt.game.spells.Spell;

public class LightSpellButton extends SpellButton{

    static final long EXPIRY = 3L*60L*1000000000L;
    public LightSpellButton(Spell spell, int x, int y, int icon) {
        super(spell, x, y, icon);
    }

    @Override
    protected void activate(int x, int y, Engine engine) {
        if (state!=OFF) {
            state = DOWN;
            Gdx.app.log("Spell","Light");
            setExpiry(engine,EXPIRY);
            engine.getPlayer().setLight(1f);
        }
    }

    @Override
    public void update(Engine engine) {
        if(state!=DOWN) return;
        long systemTime = engine.getSystemTime();
        if(systemTime>time) {
            state = UP;
            //engine.getPlayer().setLight(0f);
        } else {
            float elapsed = time - systemTime;
            engine.getPlayer().setLight(1f-elapsed/EXPIRY);
        }
    }
}
