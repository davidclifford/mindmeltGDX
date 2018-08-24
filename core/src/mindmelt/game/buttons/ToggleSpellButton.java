package mindmelt.game.buttons;

import com.badlogic.gdx.Gdx;
import mindmelt.game.engine.Engine;
import mindmelt.game.spells.Spell;

public class ToggleSpellButton extends SpellButton {
    public ToggleSpellButton(Spell spell, int x, int y, int icon, String name) {
        super(spell, x, y, icon, name);
    }

    @Override
    protected void activate(int x, int y, Engine engine) {
        if (state==OFF) return;
        if (state==UP) {
            state = DOWN;
            spell.activate(engine);
        } else if(state == DOWN) {
            state = UP;
            spell.reset(engine);
        }
    }
}
