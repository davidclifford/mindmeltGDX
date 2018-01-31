package mindmelt.game.buttons;

import com.badlogic.gdx.Gdx;
import mindmelt.game.engine.Engine;
import mindmelt.game.gui.Button;
import mindmelt.game.spells.Spell;

public class SpellButton extends Button {

    protected float time;
    protected static final float SINGLEPRESS = 0.5f;
    protected Spell spell;

    public SpellButton(Spell spell, int x, int y, int icon) {
        super(x, y, icon);
        this.spell = spell;
    }

    protected void activate(int x, int y, Engine engine) {
    }

    public void update(Engine engine) {
        if(!spell.isLearned())
            state = OFF;
        else if(spell.isActive())
            state = DOWN;
        else
            state = UP;
    }

    protected void setExpiry(Engine engine, float expiry) {
        time = engine.getSystemTime() + expiry;
        Gdx.app.log("time=",""+time);
    }
    public float getTime() { return time; }
}
