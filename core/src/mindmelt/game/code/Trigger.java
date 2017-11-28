package mindmelt.game.code;

public class Trigger {
    private String trigger;
    private int x=0;
    private int y=0;
    private int z=0;

    public Trigger(String trigger) {
        this.trigger = trigger;
        //has coords?
        String parts[] = trigger.split(" ");
        if(parts.length<=1) return;
        String coord = parts[1];
        if (!coord.contains(",")) return;
        String nums[] = coord.split(",");
        if(nums.length>0) x = Integer.parseInt(nums[0]);
        if(nums.length>1) y = Integer.parseInt(nums[1]);
        if(nums.length>2) z = Integer.parseInt(nums[2]);
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
