package mindmelt.game.buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import mindmelt.game.engine.Engine;
import mindmelt.game.engine.Message;
import mindmelt.game.objects.ObjPlayer;


public class DirectionSpellButton extends SpellButton{

    private static final String[] dirs = {"North","East","South","West"};
    public DirectionSpellButton(int x, int y, int icon) {
        super(x, y, icon);
    }

    @Override
    protected void activate(int x, int y, Engine engine) {
        if (state==UP) {
            state = DOWN;
            Gdx.app.log("Spell","Direction");
            ObjPlayer player = engine.getPlayer();
            int dir = player.getDirection();
            String direction = dirs[dir];
            String mess = String.format("You are facing %s",direction);
            Message message = new Message(player.getX(),player.getY(),player.getZ(),mess, Color.FIREBRICK,1000L);
            engine.addMessage(message);
            setExpiry(engine,SINGLEPRESS);
        }
    }
}
