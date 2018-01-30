package mindmelt.game.buttons;

import com.badlogic.gdx.Gdx;
import mindmelt.game.engine.Engine;
import mindmelt.game.spells.Spell;

public class ToggleSpellButton extends SpellButton {
    public ToggleSpellButton(Spell spell, int x, int y, int icon) {
        super(spell, x, y, icon);
    }

    @Override
    protected void activate(int x, int y, Engine engine) {
        if (state==OFF) return;
        if (state==UP) {
            state = DOWN;
            Gdx.app.log("Toggle button","DOWN");
            spell.activate(engine);
        } else if(state == DOWN) {
            state = UP;
            Gdx.app.log("Toggle button","UP");
            spell.reset(engine);
        }
    }

    public void update(Engine engine) {}
}
