package mindmelt.game.spells;

import com.badlogic.gdx.Gdx;
import mindmelt.game.engine.Engine;
import mindmelt.game.maps.EntryExit;

public class CircleSpell extends Spell {
    public void activate(Engine engine) {
        Gdx.app.log("Spell", "Circle");
        EntryExit circle = new EntryExit(0, 0, 0, 40, 39, 0, "world", "Circle");
        engine.moveToMap(circle);
        engine.addMessage("Return to the Stone circle");
    }
}
