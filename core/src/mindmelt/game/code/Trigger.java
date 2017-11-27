package mindmelt.game.code;

public class Trigger {
    private String trigger;
    private int x;
    private int y;
    private int z;

    public Trigger(String trigger) {
        this.trigger = trigger;
    }

    public Trigger(String trigger, int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.trigger = String.format("%s %d,%d,%d",trigger,x,y,z);
    }

    public boolean is(Trigger trig) {
        return (trig.trigger.equals(trigger));
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }
}
