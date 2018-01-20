package mindmelt.game.engine;

import mindmelt.game.gui.Button;

public class SpellReset {
    private Button spell;
    private float time;

    SpellReset(Button spell, float time) {
        this.spell = spell;
        this.time = time;
    }

    public boolean needToReset(float systemTime) {
        if(systemTime>time) {
            spell.reset();
            return true;
        }
        return false;
    }
}
