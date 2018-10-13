package mindmelt.game.actions;

import mindmelt.game.engine.Engine;

public class JournalTopAction extends Action{
    @Override
    public void doAction(Engine engine) {
        engine.getJournal().scrollTop();
    }
}
