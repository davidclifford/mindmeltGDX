package mindmelt.game.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import mindmelt.game.MindmeltGDX;

/**
 * Created by David on 3/05/2017.
 */
public class Button extends GuiElem {

    private int upIcon;
    private int downIcon;

    public Button(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    public int getUpIcon() {
        return upIcon;
    }

    public void setUpIcon(int upIcon) {
        this.upIcon = upIcon;
    }

    public int getDownIcon() {
        return downIcon;
    }

    public void setDownIcon(int downIcon) {
        this.downIcon = downIcon;
    }

    @Override
    public void render(Batch batch, MindmeltGDX game) {
        int xx = getAbsX();
        int yy = getAbsY();
        batch.draw(game.getTile(upIcon), xx , Gdx.graphics.getHeight()-yy-h );


    }
}
