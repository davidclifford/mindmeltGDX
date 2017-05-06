package mindmelt.game.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import mindmelt.game.MindmeltGDX;

/**
 * Created by David on 3/05/2017.
 */
public class Button extends GuiElem {

    public static final int OFF = 0;
    public static final int UP = 1;
    public static final int DOWN = 2;

    private int activeIcon;
    private int inactiveIcon = 149;
    private int state = OFF;

    public Button(int x, int y, int icon) {
        super(x, y, 1, 1);
        activeIcon = icon;
    }

    public int getActiveIcon() {
        return activeIcon;
    }

    public void setActiveIcon(int activeIcon) {
        this.activeIcon = activeIcon;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public void renderThis(MindmeltGDX game) {
        Batch batch = game.batch;
        int xx = getAbsX();
        int yy = getAbsY();
        if (state==UP) {
            batch.draw(game.getTile(activeIcon), xx, height - yy);
            batch.draw(game.getTile(150), xx, height - yy);
        } else if (state==DOWN) {
            batch.draw(game.getTile(activeIcon), xx, height - yy - 1);
            batch.draw(game.getTile(151), xx, height - yy);
        } else {
            batch.draw(game.getTile(inactiveIcon), xx, height - yy);
        }
    }

    @Override
    protected void activate(int x, int y, MindmeltGDX game) {

        if (state==UP)
            state = DOWN;
        else if (state==DOWN)
            state = UP;
        //Gdx.audio.newSound(Gdx.files.internal("sound/wilhelm.ogg")).play();
    }
}
