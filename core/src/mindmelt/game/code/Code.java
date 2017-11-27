package mindmelt.game.code;

import mindmelt.game.engine.Engine;

import java.util.ArrayList;
import java.util.List;

public class Code {
    private Trigger trigger;
    private List<Instruction> instructions = new ArrayList<>();

    public Code(Trigger trigger) {
        this.trigger = trigger;
    }

    public void add(Instruction instruction) {
        instructions.add(instruction);
    }

    public boolean isTrigger(Trigger trig) {
        return (trigger.is(trig));
    }

    public void runCode(Engine engine) {
        for(Instruction instruction:instructions) {
            boolean cont = instruction.run(trigger, engine);
            if (cont==false) break;
        }
    }
}
