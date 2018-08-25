package mindmelt.game.gui;

import mindmelt.game.MindmeltGDX;
import mindmelt.game.engine.Engine;

/**
 * Created by David on 3/05/2017.
 */
public class Button extends GuiElem {

    public static final int OFF = 0;
    public static final int UP = 1;
    public static final int DOWN = 2;

    protected static final int upButton = 150;
    protected static final int downButton = 151;

    protected int activeIcon;
    protected int inactiveIcon = 149;
    protected int state = OFF;

    public Button(int x, int y, int icon, String name) {
        super(x, y, 1, 1);
        activeIcon = icon;
        this.name = name;
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
        update(game.engine);
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
    protected void activate(int x, int y, Engine engine) {

        if (state==UP) {
            state = DOWN;
        } else if (state==DOWN) {
            state = UP;
        }
        if (activeIcon==148) {
            engine.debugChangeTiles();
        }
    }

    public void update(Engine engine) {}

    public void reset() {
        state = UP;
    }
}
