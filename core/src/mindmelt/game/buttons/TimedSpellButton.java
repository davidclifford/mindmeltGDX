package mindmelt.game.buttons;

import mindmelt.game.engine.Engine;
import mindmelt.game.spells.Spell;

public class TimedSpellButton extends SpellButton {
    public TimedSpellButton(Spell spell, int x, int y, int icon, String name) {
        super(spell, x, y, icon, name);
    }

    @Override
    public void activate(int x, int y, Engine engine) {
        if(state==OFF) return;
        spell.activate(engine);
    }

}
