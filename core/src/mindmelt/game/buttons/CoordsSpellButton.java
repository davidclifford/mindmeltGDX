package mindmelt.game.buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import mindmelt.game.engine.Engine;
import mindmelt.game.engine.Message;
import mindmelt.game.objects.ObjPlayer;

public class CoordsSpellButton extends SpellButton{

    public CoordsSpellButton(int x, int y, int icon) {
        super(x, y, icon);
    }

    @Override
    protected void activate(int x, int y, Engine engine) {
        if (state==UP) {
            state = DOWN;
            Gdx.app.log("Spell","Coords");
            ObjPlayer player = engine.getPlayer();
            String mess = String.format("Your coords are %d,%d,%d",player.getX(),player.getY(),player.getZ());
            Message message = new Message(player.getX()-2,player.getY(),player.getZ(),mess, Color.BLUE,1000L);
            engine.addMessage(message);
            setExpiry(engine,SINGLEPRESS);
        }
    }
}