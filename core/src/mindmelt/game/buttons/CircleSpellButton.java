package mindmelt.game.buttons;

import com.badlogic.gdx.Gdx;
import mindmelt.game.engine.Engine;
import mindmelt.game.gui.Button;
import mindmelt.game.maps.EntryExit;

public class CircleSpellButton  extends Button {
    public CircleSpellButton(int x, int y, int icon) {
        super(x, y, icon);
    }

    @Override
    protected void activate(int x, int y, Engine engine) {
        if (state==UP) {
            state = DOWN;
            Gdx.app.log("Spell","Circle");
            EntryExit circle = new EntryExit(0,0,0,40,39,0,"world","Circle");
            engine.moveToMap(circle);
            engine.resetSpell(this,0.2f);
        }
    }
}

