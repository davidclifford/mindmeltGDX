package mindmelt.game.buttons;

import com.badlogic.gdx.Gdx;
import mindmelt.game.engine.Engine;
import mindmelt.game.objects.ObjPlayer;

public class BackSpellButton extends SpellButton{

    static final float EXPIRY = 3*60;
    public BackSpellButton(int x, int y, int icon) {
        super(x, y, icon);
    }

    @Override
    protected void activate(int x, int y, Engine engine) {
        ObjPlayer player = engine.getPlayer();
        if (state==UP) {
            state = DOWN;
            Gdx.app.log("Spell","Save Back");
            player.setBack(engine);
        } else if(state==DOWN) {
            state = UP;
            Gdx.app.log("Spell","Go Back");
            player.goBack(engine);
        }
    }

    @Override
    public void update(Engine engine) {}
}