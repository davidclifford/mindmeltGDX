package mindmelt.game.buttons;

import com.badlogic.gdx.Gdx;
import mindmelt.game.engine.Engine;
import mindmelt.game.gui.Button;

public class SpellButton extends Button {

    protected float time;
    protected static final float SINGLEPRESS = 0.2f;

    public SpellButton(int x, int y, int icon) {
        super(x, y, icon);
    }

    protected void activate(int x, int y, Engine engine) {
        if (state==UP) {
            state = DOWN;
            Gdx.app.log("Spell","Spell "+activeIcon);
            setExpiry(engine,SINGLEPRESS);
        }
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
