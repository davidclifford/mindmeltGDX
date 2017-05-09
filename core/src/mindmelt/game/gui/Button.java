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

    protected static final int upButton = 150;
    protected static final int downButton = 151;

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
    public void renderThis(MindmeltGDX game, float delta) {
        Batch batch = game.batch;
        if (state==UP) {
            drawIcon(0,0,activeIcon,game);
            drawIcon(0,0,upButton,game);
        } else if (state==DOWN) {
            drawIcon(0,1,activeIcon, game);
            drawIcon(0,0,downButton,game);
        } else {
            drawIcon(0,0,inactiveIcon,game);
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
