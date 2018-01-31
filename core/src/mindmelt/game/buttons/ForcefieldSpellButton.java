package mindmelt.game.buttons;

import com.badlogic.gdx.Gdx;
import mindmelt.game.engine.Engine;
import mindmelt.game.spells.Spell;

public class ForcefieldSpellButton extends SpellButton{

    static final long EXPIRY = 3L * 1000000000L;
    public ForcefieldSpellButton(Spell spell, int x, int y, int icon) {
        super(spell, x, y, icon);
    }

    @Override
    protected void activate(int x, int y, Engine engine) {
        if (state!=OFF) {
            state = DOWN;
            Gdx.app.log("Spell","Forcefield");
            setExpiry(engine,EXPIRY);
            engine.getPlayer().setForcefield(true);
        }
    }

    @Override
    public void update(Engine engine) {
        if(state!=DOWN) return;
        long systemTime = engine.getSystemTime();
        if(systemTime>time) {
            state = UP;
            engine.getPlayer().setForcefield(false);
        }
    }
}