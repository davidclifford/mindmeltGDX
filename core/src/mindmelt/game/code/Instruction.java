package mindmelt.game.code;

import mindmelt.game.engine.Engine;

public class Instruction {
    private String command;

    public Instruction(String command) {
        this.command = command;
    }

    public boolean run(Engine engine) {
        System.out.println(command);
        return true;
    }
}
