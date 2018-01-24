package mindmelt.game.buttons;

import com.badlogic.gdx.Gdx;
import mindmelt.game.engine.Engine;

public class ForcefieldSpellButton extends SpellButton{

    static final float EXPIRY = 3;
    public ForcefieldSpellButton(int x, int y, int icon) {
        super(x, y, icon);
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
        float systemTime = engine.getSystemTime();
        if(systemTime>time) {
            state = UP;
            engine.getPlayer().setForcefield(false);
        }
    }
}