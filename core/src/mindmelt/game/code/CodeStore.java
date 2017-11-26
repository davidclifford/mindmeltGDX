package mindmelt.game.code;

import mindmelt.game.engine.Engine;

import java.util.ArrayList;
import java.util.List;

public class CodeStore {
    private List<Code> store = new ArrayList<>();

    public void add(Code code) {
        store.add(code);
    }

    public void runTriggerCode(Trigger trigger, Engine engine) {
        for(Code code: store) {
            if(code.isTrigger(trigger)) {
                code.runCode(engine);
            }
        }
    }
}
