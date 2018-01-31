package mindmelt.game.buttons;

import com.badlogic.gdx.Gdx;
import mindmelt.game.engine.Engine;
import mindmelt.game.spells.Spell;

public class SinglePressSpellButton extends SpellButton {
    public SinglePressSpellButton(Spell spell, int x, int y, int icon) {
        super(spell, x, y, icon);
    }

    public void activate(int x, int y, Engine engine) {
        if(state==OFF) return;
        state = DOWN;
        Gdx.app.log("Spell","Spell "+activeIcon);
        setExpiry(engine,SINGLEPRESS);
        spell.activate(engine);
    }

    @Override
    public void update(Engine engine) {
        if(!spell.isLearned()) {
            state = OFF;
        } else if(time>engine.getSystemTime()) {
            Gdx.app.log("down=", "" + engine.getSystemTime());
            state = DOWN;
        } else {
            state = UP;
        }
    }
}
