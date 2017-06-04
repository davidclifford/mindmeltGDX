package mindmelt.game.windows;

import com.badlogic.gdx.graphics.Color;
import mindmelt.game.MindmeltGDX;
import mindmelt.game.gui.Window;

/**
 * Created by david_000 on 04/06/2017.
 */
public class StatusWindow extends Window {
    public StatusWindow(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    @Override
    protected void renderThis(MindmeltGDX game, float delta) {
        //super.renderThis(game, delta);
        drawText(4,4,Color.GREEN,"Strength "+(int)(game.engine.getPlayer().getSpeed()*100),game);
        drawText(4,16,Color.CYAN,"Level "+(int)(game.engine.getPlayer().getSpeed()*5),game);
        drawText(4,28,Color.RED, game.world.getDescription(), game);
        drawText(4,40,Color.YELLOW,game.engine.getPlayerHandObject()==null?"":game.engine.getPlayerHandObject().getDescription(),game);

    }
}
