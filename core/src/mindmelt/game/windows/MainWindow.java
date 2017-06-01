package mindmelt.game.windows;

import com.badlogic.gdx.graphics.Color;
import mindmelt.game.MindmeltGDX;
import mindmelt.game.gui.Window;

/**
 * Created by David on 8/05/2017.
 */
public class MainWindow extends Window {

    public MainWindow(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    @Override
    public void renderThis(MindmeltGDX game, float delta) {
        //super.renderThis(game,delta);
        game.batch.setColor(Color.WHITE);
        game.batch.draw(game.mainWindow, getAbsX(), height-getAbsY()-game.mainWindow.getHeight()+32);
        //super.render(game,delta);
    }
}
