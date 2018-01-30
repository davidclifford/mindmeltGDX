package mindmelt.game.buttons;

import com.badlogic.gdx.Gdx;
import mindmelt.game.engine.Engine;
import mindmelt.game.spells.Spell;

public class WaterSpellButton extends SpellButton{

    static final float EXPIRY = 10;
    public WaterSpellButton(Spell spell, int x, int y, int icon) {
        super(spell, x, y, icon);
    }

    @Override
    protected void activate(int x, int y, Engine engine) {
        if (state!=OFF) {
            state = DOWN;
            Gdx.app.log("Spell","Water");
            setExpiry(engine,EXPIRY);
            engine.getPlayer().setWater(true);
        }
    }

    @Override
    public void update(Engine engine) {
        if(state!=DOWN) return;
        float systemTime = engine.getSystemTime();
        if(systemTime>time) {
            state = UP;
            engine.getPlayer().setWater(false);
        }
    }
}