package mindmelt.game.engine;

/**
 * Created by David on 1/06/2017.
 */
public class ChangeTile {
    private int x;
    private int y;
    private int z;
    private int map;
    private int id;
    private int change;

    public ChangeTile(int x, int y, int z, int map, int id, int change) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.map = map;
        this.id = id;
        this.change = change;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public int getMap() {
        return map;
    }

    public void setMap(int map) {
        this.map = map;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChange() {
        return change;
    }

    public void setChange(int change) {
        this.change = change;
    }
}
