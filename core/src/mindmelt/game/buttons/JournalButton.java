package mindmelt.game.buttons;

import mindmelt.game.actions.Action;
import mindmelt.game.engine.Engine;
import mindmelt.game.gui.Button;

public class JournalButton extends Button {

    protected long time;
    protected static final long SINGLEPRESS = 1000000000L/5L;
    protected Action action;

    public JournalButton(int x, int y, int icon, String name, Action action) {
        super(x, y, icon, name);
        this.action = action;
        state = UP;
    }

    public void activate(int x, int y, Engine engine) {
        state = DOWN;
        setExpiry(engine,SINGLEPRESS);
        action.doAction(engine);
    }

    @Override
    public void update(Engine engine) {
        if(time>engine.getSystemTime()) {
            state = DOWN;
        } else {
            state = UP;
        }
    }

    protected void setExpiry(Engine engine, long expiry) {
        time = engine.getSystemTime() + expiry;
    }
}
