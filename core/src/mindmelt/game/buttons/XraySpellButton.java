package mindmelt.game.buttons;

import com.badlogic.gdx.Gdx;
import mindmelt.game.engine.Engine;

public class XraySpellButton extends SpellButton{

    static final float EXPIRY = 3;
    public XraySpellButton(int x, int y, int icon) {
        super(x, y, icon);
    }

    @Override
    protected void activate(int x, int y, Engine engine) {
        if (state!=OFF) {
            state = DOWN;
            Gdx.app.log("Spell","Xray");
            setExpiry(engine,EXPIRY);
            engine.getPlayer().setXray(true);
        }
    }

    @Override
    public void update(Engine engine) {
        if(state!=DOWN) return;
        float systemTime = engine.getSystemTime();
        if(systemTime>time) {
            state = UP;
            engine.getPlayer().setXray(false);
        }
    }
}