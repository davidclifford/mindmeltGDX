package mindmelt.game.windows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import mindmelt.game.MindmeltGDX;
import mindmelt.game.engine.Engine;
import mindmelt.game.gui.Window;
import mindmelt.game.objects.ObjPlayer;

public class StatusWindow extends Window {
    public StatusWindow(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    @Override
    protected void renderThis(MindmeltGDX game, float delta) {
        Engine en = game.engine;

        drawText(4,4, Color.GREEN,"Strength "+(int)(en.getPlayer().getStrength()), game);
        drawText(4,16, Color.CYAN,"Level "+(int)((ObjPlayer)en.getPlayer()).getLevel(), game);
        drawText(4,28, Color.RED, game.world.getDescription(), game);
        drawText(4,40, Color.YELLOW, en.getPlayerHandObject()==null?"":en.getPlayerHandObject().getDescription(), game);
        drawText(4,52, Color.MAGENTA, en.getToolTip(), game);

        drawText(3*SZ,4, Color.WHITE,""+(en.isCheat()?"C":"c")+(en.isSeeall()?"S":"s")+(en.isXray()?"X":"x"), game);
        drawText(6*SZ,52, Color.GRAY,"fps: " + Gdx.graphics.getFramesPerSecond(), game);
    }
}
