package mindmelt.game.code;

public class Trigger {
    private String trigger;

    public Trigger(String trigger) {
        this.trigger = trigger;
    }

    public Trigger(String trigger, int x, int y, int z) {
        this.trigger = String.format("%s %d,%d,%d",trigger,x,y,z);
    }

    public boolean is(Trigger trig) {
        return (trig.trigger.equals(trigger));
    }
}
