package mindmelt.game.spells;

import com.badlogic.gdx.Gdx;
import mindmelt.game.engine.Engine;
import mindmelt.game.objects.ObjPlayer;

public class BackSpell extends Spell {
    public void activate(Engine engine) {
        Gdx.app.log("BackSpell","Save");
        ObjPlayer player = engine.getPlayer();
        player.setBack(engine);
        setActive(true);
    }
    public void reset(Engine engine) {
        Gdx.app.log("BackSpell","Back");
        ObjPlayer player = engine.getPlayer();
        player.goBack(engine);
        setActive(false);
    }
}
