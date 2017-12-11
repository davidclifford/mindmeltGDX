package mindmelt.game.code.instructions;

import mindmelt.game.engine.Engine;

public class ExecuteInstruction extends  Instruction {
    public ExecuteInstruction(String text) {
        this.text = text;
    }

    public boolean run(Engine engine) {
        engine.addTrigger(String.format("Routine %s",text));
        return true;
    }
}
