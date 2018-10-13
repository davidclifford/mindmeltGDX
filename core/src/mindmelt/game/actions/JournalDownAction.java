package mindmelt.game.actions;

import mindmelt.game.engine.Engine;

public class JournalDownAction extends Action{
    @Override
    public void doAction(Engine engine) {
        engine.getJournal().scrollDown();
    }
}
