package mindmelt.game.maps;

public class EntryExit {
    private String description;
    
    private int fromX;
    private int fromY;
    private int fromZ;
    
    private int toX;
    private int toY;
    private int toZ;
    private String toMap;
    
    public EntryExit(int fx, int fy, int fz, int tx, int ty, int tz, String tmap, String desc) {
        description = desc;
        
        fromX = fx;
        fromY = fy;
        fromZ = fz;
        
        toX = tx;
        toY = ty;
        toZ = tz;
        toMap = tmap;
    }

    public String getDescription() {
        return description;
    }

    public int getFromX() {
        return fromX;
    }

    public int getFromY() {
        return fromY;
    }

    public int getFromZ() {
        return fromZ;
    }

    public int getToX() {
        return toX;
    }

    public int getToY() {
        return toY;
    }

    public int getToZ() {
        return toZ;
    }

    public String getToMap() {
        return toMap;
    }

    
    public boolean isEntry(int x, int y, int z) {
        return (fromX==x && fromY == y && fromZ == z);
    }
}
