package mindmelt.game.maps;

import mindmelt.game.objects.Obj;

import java.util.ArrayList;
import java.util.List;

public class Area {
    private Coord topleft;
    private Coord botright;
    private int level;
    private TileType map[][];
    private List<Obj> topObject[][];
    private boolean nomonster = false;

    Area(int x1, int y1, int z, int x2, int y2) {
        this.level = z;
        this.topleft = new Coord(x1,y1);
        this.botright = new Coord(x2,y2);
        int width = botright.getX() - topleft.getX() + 1;
        int length = botright.getY() - topleft.getY() + 1;
        createArea(x1,y1,z,width,length);
    }

    // For nomonster areas (does not have tiles or topObjects)
    Area(List<Integer> coords) {
        this.topleft = new Coord(coords.get(0),coords.get(1));
        this.botright = new Coord(coords.get(2),coords.get(3));
        this.level = coords.get(4);
        this.nomonster = true;
    }

    private void createArea(int x, int y, int z, int w, int l) {
        map = new TileType[w+1][l+1];
        topObject = new List[w+1][l+1];
    }

    public Coord getTopleft() {
        return topleft;
    }

    public Coord getBotright() {
        return botright;
    }

    public boolean inArea(int x, int y, int z) {
        return (level==z && x>=topleft.getX() && x<=botright.getX() && y>=topleft.getY() && y<=botright.getY());
    }

    public TileType getTile(int x, int y) {
        int xx = x - getTopleft().getX() + 1;
        int yy = y - getTopleft().getY() + 1;
        if(map[xx][yy]==null) return TileType.grass;//System.out.println(String.format("NULL at %d,%d,%d",x,y,level));
        return map[xx][yy];
    }

    public Obj getTopObject(int x, int y) {
        int xx = x - getTopleft().getX() + 1;
        int yy = y - getTopleft().getY() + 1;
        if(topObject[xx][yy]==null) return null;
        return topObject[xx][yy].get(0);
    }

    public void setTopObject(Obj ob, int x, int y) {
        int xx = x - getTopleft().getX() + 1;
        int yy = y - getTopleft().getY() + 1;
        if(topObject[xx][yy] == null)
            topObject[xx][yy] = new ArrayList<>();
        topObject[xx][yy].add(ob);
    }

    public List<Obj> getObjects(int x, int y) {
        int xx = x - getTopleft().getX() + 1;
        int yy = y - getTopleft().getY() + 1;
        return topObject[xx][yy];
    }

    public void removeObject(Obj obj) {
        int xx = obj.x - getTopleft().getX() + 1;
        int yy = obj.y - getTopleft().getY() + 1;
        if (topObject[xx][yy]!=null) {
            topObject[xx][yy].remove(obj);
            if (topObject[xx][yy].isEmpty()) {
                topObject[xx][yy] = null;
            }
        }
        obj.setCoords(0, 0, 0);
        obj.setMapId(0);
    }

    public List<Obj> removeAllObjects(int x, int y) {
        int xx = x - getTopleft().getX() + 1;
        int yy = y - getTopleft().getY() + 1;
        if(topObject[xx][yy]==null) return null;
        List<Obj> objects = getObjects(x, y);
        if(objects!=null) {
            objects.stream().forEach(ob -> {
                ob.setCoords(0, 0, 0);
                ob.setMapId(0);
            });
        }
        topObject[xx][yy] = null;
        return objects;
    }

    public void addAllObjects(List<Obj> objects, int x, int y, int mapId) {
        if(objects==null) return;
        int xx = x - getTopleft().getX() + 1;
        int yy = y - getTopleft().getY() + 1;
        if(topObject[xx][yy]==null) {
            topObject[xx][yy] = new ArrayList<Obj>();
        }
        topObject[xx][yy].addAll(objects);
        objects.stream().forEach( ob -> {
            ob.setCoords(x,y,level);
            ob.setMapId(mapId);
        });
    }

    public void setTile(int x, int y, TileType tile) {
        int xx = x - getTopleft().getX() + 1;
        int yy = y - getTopleft().getY() + 1;
        map[xx][yy] = tile;
    }

    public int getLevel() {
        return level;
    }
}