package mindmelt.game.buttons;

import com.badlogic.gdx.Gdx;
import mindmelt.game.engine.Engine;
import mindmelt.game.gui.Button;
import mindmelt.game.spells.Spell;

public class SpellButton extends Button {

    protected float time;
    protected static final float SINGLEPRESS = 0.2f;
    protected Spell spell;

    public SpellButton(Spell spell, int x, int y, int icon) {
        super(x, y, icon);
        this.spell = spell;
    }

    protected void activate(int x, int y, Engine engine) {
        if (state==OFF) return;
        state = DOWN;
        Gdx.app.log("Spell","Spell "+activeIcon);
        setExpiry(engine,SINGLEPRESS);
        spell.activate(engine);
    }

    public void update(Engine engine) {
        if(state==OFF) return;
        if(engine.getSystemTime()>time) {
            state = UP;
        }
    }

    protected void setExpiry(Engine engine, float expiry) {
        time = engine.getSystemTime() + expiry;
    }
    public float getTime() { return time; }
}
