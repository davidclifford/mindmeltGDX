package mindmelt.game.code;

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
}
