package mindmelt.game.maps;

import mindmelt.game.code.Code;
import mindmelt.game.code.CodeStore;
import mindmelt.game.code.Instruction;
import mindmelt.game.code.Trigger;
import mindmelt.game.objects.Obj;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class World implements ITileAccess {

    public static final int MAP_SIZE = 80;
    public static final int LAYERS = 1;
    private TileType map[][][] = new TileType[LAYERS][MAP_SIZE+1][MAP_SIZE+1];
    private List<Obj> top[][][] = new ArrayList[LAYERS][MAP_SIZE+1][MAP_SIZE+1];
    private List<Area> nomonsters = new ArrayList<>();
    private List<EntryExit> entries = new ArrayList<>();
    private CodeStore codeStore = new CodeStore();

    private int id = 0;
    private int version = 0;
    private boolean light;
    private String name = "";
    private String description = "";
    private String filename = "";
    
    class Coord {
        int x;
        int y;
        int z;
        
        Coord(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
    
    class Area {
        Coord topleft;
        Coord botright;
        
        Area(int x1, int y1, int z1, int x2, int y2, int z2) {
            this.topleft = new Coord(x1,y1,z1);
            this.botright = new Coord(x2,y2,z2);
        }       
        
        Area(List<Integer> coords) {
            this.topleft = new Coord(coords.get(0),coords.get(1),coords.get(2));
            this.botright = new Coord(coords.get(3),coords.get(4),coords.get(5));
        }
    }

    @Override
    public TileType getTile(int x, int y, int level) {
        if (x<0 || y<0) return TileType.space;
        if (x>=MAP_SIZE || y>=MAP_SIZE) return TileType.space;
        if (map[level][y][x]==null) return TileType.space;
        return map[level][y][x];
    }     
    
    public List<Obj> getObjects(int x, int y, int level) {
        if (x<0 || y<0) return null;
        if (x>=MAP_SIZE || y>=MAP_SIZE) return null;
        return top[level][y][x];
    }
    
    public Obj getTopObject(int x, int y, int level) {
        if (x<0 || y<0) return null;
        if (x>=MAP_SIZE || y>=MAP_SIZE) return null;
        List<Obj> objects = top[level][y][x];
        if (objects==null || objects.size()==0) return null;
        return objects.get(objects.size()-1); //return last in list (ie the top)
    }    
    
    public void setTop(int x, int y, int level, Obj ob) {
        if (x<0 || y<0) return;
        if (x>=MAP_SIZE || y>=MAP_SIZE) return;
        List<Obj> objects = top[level][y][x];
        if (objects==null) {
            objects = new ArrayList<Obj>();
            top[level][y][x] = objects;
        }
        objects.add(ob);
    }  
    
    public void removeObject(Obj ob) {
        List<Obj> objects = getObjects(ob.x, ob.y, ob.z);
        ob.setCoords(0, 0, 0);
        ob.setMapId(0);
        if(objects!=null) {
            objects.remove(ob);
            if (objects.isEmpty())
                top[ob.z][ob.y][ob.x] = null;
        }
    }

    public List<Obj> removeAllObjects(int x, int y, int z) {
        List<Obj> objects = getObjects(x, y, z);
        if(objects!=null) {
            objects.stream().forEach(ob -> {
                ob.setCoords(0, 0, 0);
                ob.setMapId(0);
            });
        }
        top[z][y][x] = null;
        return objects;
    }

    public void addAllObjects(List<Obj> objects, int x, int y, int z) {
        if(objects==null) return;

        for(Obj ob : objects) {
            ob.setCoords(x,y,z);
            ob.setMapId(getId());
        }
        if(top[z][y][x]!=null) {
            top[z][y][x].addAll(objects);
        }
        top[z][y][x] = objects;
    }
    
    public void loadMap(String mapName) {
        filename = mapName;
        loadControl(mapName);
        loadWorld(mapName);
    }
    
    private String readLine(BufferedReader input) {
        String line;
        
        try {
            while ((line = input.readLine()) != null) {
               if (line.startsWith("//")) continue;
               return line;
            }   
        } catch(Exception e) {
            System.out.println(e.toString());
        }
        return null;
    }
        
    public void loadControl(String mapName) {
        String filename = "data/"+mapName+".control";
        String line = "";
        BufferedReader input = null;
        try {
            input = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException e) {
            System.out.println("Map "+mapName+" not found");
        }
        
        while ((line = readLine(input)) != null) {
            if (line.startsWith("@")) {
                String section = line.substring(1);
                switch (section) {
                    case "map":
                        readHeader(input);
                        break;
                    case "entry":
                        readEntry(input); 
                        break;
                    case "code":
                        readControl(input);
                        break;
                    case "talk":
                        readTalk(input);
                        break;
                    default: 
                        System.out.println("Unsupported control section @"+section);
                }
            }
        }
    }   
    
    private String[] keyValue(String line) {
        String words[] = line.split("=");
        return words;
    }
    
    private List<Integer> numbers(String value) {
        String values[] = value.split(",");
        List<Integer> vals = new ArrayList<>();
        for(String val : Arrays.asList(values)) {
            vals.add(new Integer(val));
        }
        return vals;
    }    
    
    private String[] strings(String value) {
        String values[] = value.split(",");
        return values;
    }
    
    private void readHeader(BufferedReader input) {
        String line="";
        String key="";
        String value="";
        while( (line=readLine(input))!= null) {
            if(line.startsWith("--"))
                return;
            String kv[] = keyValue(line);
            switch(kv[0]) {
                case "nomonster" :
                    List<Integer> area = numbers(kv[1]);
                    nomonsters.add(new Area(area));
                    break;
                case "id" :
                    id = Integer.parseInt(kv[1]);
                    break;
                case "version" :
                    version = Integer.parseInt(kv[1]);
                    break;                   
                case "name" :
                    name = kv[1];
                    break;                    
                case "description" :
                    description = kv[1];
                    break;
                case "light" :
                    light = kv[1].equals("true");
                    break;
                default:
                    System.out.println("Unknown control header keyword "+kv[0]);            }
        }
    }
    
    private void readEntry(BufferedReader input) {
        String line;
        while( (line=readLine(input))!= null) {
            int fx = 0;
            int fy = 0;
            int fz = 0;
            int tx = 0;
            int ty = 0;
            int tz = 0;
            String toMap = "";
            String desc = "";
            String coords[];
            while( !line.startsWith("-")) {
                String kv[] = keyValue(line);
                switch(kv[0]) {
                    case "filename" :
                        toMap = kv[1];
                        break;                    
                    case "desc" :
                        desc = kv[1];
                        break;
                    case "from" :
                        String from = kv[1];
                        coords = strings(from);
                        fx = Integer.parseInt(coords[0]);
                        fy = Integer.parseInt(coords[1]);
                        fz = Integer.parseInt(coords[2]);
                        break;
                    case "to" :
                        String to = kv[1];
                        coords = strings(to);
                        tx = Integer.parseInt(coords[0]);
                        ty = Integer.parseInt(coords[1]);
                        tz = Integer.parseInt(coords[2]);                        
                        break;                        
                    default:
                        System.out.println("Unknown entry keyword "+kv[0]);
                }
                line=readLine(input);
                if(line==null) break;
            }
            EntryExit entry = new EntryExit(fx, fy, fz, tx, ty, tz, toMap, desc);
            entries.add(entry);
            if(line==null || line.startsWith("--"))
                return;
        }
    }
    
    private void readControl(BufferedReader input) {
        String line;
        while( (line=readLine(input))!= null) {
            if(line.startsWith("--"))
                return;
            if(line.startsWith("//"))
                continue;
            Trigger trigger = new Trigger(line);
            Code code = new Code(trigger);
            while ( (line=readLine(input))!= null) {
                if(line.startsWith("-"))
                    break;
                if(line.startsWith("//"))
                    continue;
                Instruction instruction = new Instruction(line);
                code.add(instruction);
            }
            codeStore.add(code);
            if(line.startsWith("--"))
                return;
        }
    }
    
    private void readTalk(BufferedReader input) {
        String line;
        while( (line=readLine(input))!= null) {
            if(line.startsWith("--"))
                return;
        }        
    }
    
    private void loadWorld(String mapName) {
        String filename = "data/"+mapName+".map";
        
        try {
            BufferedReader input = new BufferedReader(new FileReader(filename));
            //Header
            int level = 0;
            List<Integer> coords;
            String line = "";
            int topleftX=1;
            int topleftY=1;
            
            while(line != null) {
                while ((line = input.readLine())!=null && !line.equals("--")) {
                    String kv[] = keyValue(line);
                    switch(kv[0]) {
                        case "topleft" :
                            coords = numbers(kv[1]);
                            topleftX = coords.get(0);
                            topleftY = coords.get(1);
                            break;
                        case "level" :
                            level = Integer.parseInt(kv[1]);
                            break;
                        default:
                            System.out.println("Unknown map keyword "+kv[0]);                } 
                }

                int y = topleftY;
                while ((line = input.readLine())!=null && !line.equals("--")) {
                    int x = topleftX;
                    for(int c=0; c<line.length();c++) {
                        char ch = line.charAt(c);
                        TileType tile = TileType.getFromChar(ch);
                        map[level][y][x] = tile;
                        x++;
                    }
                    y++;
                }
            }
        } catch (Exception e)   {
            System.out.println(e.toString());
        }
    }
    
    private void readMapHeader(BufferedReader input) throws IOException {
            String line;

    }
    
    public boolean canEnter(Obj ob, int x, int y, int z) {
        if (getTile(x,y,z).canEnter()) {
            if(getTopObject(x,y,z)== null || ob.isPlayer() || !getTopObject(x,y,z).isBlocked()) {
                if(ob.isMonster() && inNoMonsterArea(x,y,z)) {
                    return false;
                } else {
                    return true;        
                }
            }
        }
        return false;
    }
    
    private boolean inNoMonsterArea(int x, int y, int z) {
        for(Area a : nomonsters) {
            if (a.topleft.z == z && a.topleft.x <= x && a.topleft.y <= y && a.botright.x >= x && a.botright.y >= y) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isEntryExit(int x, int y, int z) {
        return !(getEntryExit(x, y, z)==null);
    }    
    
    public EntryExit getEntryExit(int x, int y, int z) {
        for (EntryExit ent : entries) {
            if (ent.isEntry(x,y,z))
                return ent;
        }
        return null;
    }
    
    public int getId() { 
        return id; 
    }

    public int getVersion() {
        return version;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getFilename() {
        return filename;
    }

    public boolean getLight() {
        return light;
    }
    
    public void setTile(int x, int y, int z, TileType tile)
    {
        map[z][y][x] = tile;
    }
    
    public void changeTile(int x, int y, int z, TileType tile) {
        if (x<0 || x>MAP_SIZE || y<0 || y>MAP_SIZE || z<0 || z>=LAYERS || tile == null) {
            return;
        }
        setTile(x,y,z,tile);
    }

    public CodeStore getCodeStore() {
        return codeStore;
    }
}
