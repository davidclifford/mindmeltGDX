package mindmelt.game.maps;

import mindmelt.game.code.Code;
import mindmelt.game.code.CodeStore;
import mindmelt.game.code.Instruction;
import mindmelt.game.code.Trigger;
import mindmelt.game.engine.Engine;
import mindmelt.game.objects.Obj;
import mindmelt.game.objects.ObjPlayer;
import mindmelt.game.talk.Dialogue;
import mindmelt.game.talk.Talk;
import mindmelt.game.talk.TalkStore;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class World implements ITileAccess {

    private List<EntryExit> entries = new ArrayList<>();
    private CodeStore codeStore = new CodeStore();
    private List<Area> areas = new ArrayList<>();
    private List<Area> nomonsters = new ArrayList<>();
    private TalkStore talkStore;

    private int id = 0;
    private int version = 0;
    private boolean light;
    private String name = "";
    private String description = "";
    private String filename = "";
    private int deathX = 0;
    private int deathY = 0;
    private int deathZ = 0;

    private Area getArea(int x, int y, int z) {
        for(Area area: areas) {
            if(area.inArea(x,y,z))
                return area;
        }
        return null;
    }

    @Override
    public TileType getTile(int x, int y, int level) {
        Area area = getArea(x,y,level);
        if(area == null) return TileType.space;
        return area.getTile(x,y);
    }
    
    public List<Obj> getObjects(int x, int y, int level) {
        Area area = getArea(x,y,level);
        if(area == null) return null;
        return area.getObjects(x,y);

    }
    
    public Obj getTopObject(int x, int y, int level) {
        Area area = getArea(x,y,level);
        if(area == null) return null;
        return area.getTopObject(x,y);
    }    
    
    public void setTop(int x, int y, int level, Obj ob) {
        Area area = getArea(x,y,level);
        if(area == null) return;
        area.setTopObject(ob,x,y);
    }
    
    public void removeObject(Obj ob) {
        Area area = getArea(ob.x,ob.y,ob.z);
        if(area==null) return;
        area.removeObject(ob);
    }

    public List<Obj> removeAllObjects(int x, int y, int z) {
        Area area = getArea(x,y,z);
        if(area==null) return null;
        return area.removeAllObjects(x,y);
    }

    public void addAllObjects(List<Obj> objects, int x, int y, int z) {
        if(objects==null) return;
        Area area = getArea(x,y,z);
        if(area==null) return;
        area.addAllObjects(objects,x,y,id);
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
                case "death" :
                    String[] death = kv[1].split(",");
                    deathX = Integer.parseInt(death[0]);
                    deathY = Integer.parseInt(death[1]);
                    deathZ = Integer.parseInt(death[2]);
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
                Instruction instruction = new Instruction(trigger, line);
                code.add(instruction);
            }
            codeStore.add(code);
            if(line==null || line.startsWith("--"))
                return;
        }
    }
    
    private void readTalk(BufferedReader input) {
        boolean finished = false;
        talkStore = new TalkStore();
        String line;
        int id = 0;
        boolean sayfirst = false;
        boolean talkfirst = false;
        while (!finished) {
            while ((line = readLine(input)) != null && !line.startsWith("-")) {
                String kv[] = keyValue(line);
                switch (kv[0]) {
                    case "id":
                        id = Integer.parseInt(kv[1]);
                        break;
                    case "sayfirst":
                        sayfirst = Integer.parseInt(kv[1])==1;
                        break;
                    case "talkfirst":
                        talkfirst = Integer.parseInt(kv[1])==1;
                        break;
                    default:
                        System.out.println(String.format("Unknown talk property %s", kv[1]));
                        return;
                }
            }
            Talk talk = new Talk(id);
            talk.setSayfirst(sayfirst);
            talk.setTalkfirst(talkfirst);
            talkStore.addTalk(talk);
            while ((line = readLine(input)) != null && !line.startsWith("-")) {
                String keyword = "";
                String reply = "";
                int replyCode = 0;
                String params[] = line.split(",");
                if(params.length>=2) {
                    keyword = params[0];
                    reply = params[1];
                }
                if (params.length==3) {
                    replyCode = Integer.parseInt(params[2]);
                }
                Dialogue dialogue = new Dialogue(keyword, reply, replyCode);
                talk.addDialogue(dialogue);
            }
            if(line==null || line.startsWith("--"))
                finished = true;
        }
    }
    
    private void loadWorld(String mapName) {
        String filename = "data/"+mapName+".map";
        boolean finished = false;
        
        try {
            BufferedReader input = new BufferedReader(new FileReader(filename));
            //Header
            int level = 0;
            List<Integer> coords;
            String line = "";
            int topleftX=1;
            int topleftY=1;
            int botrightX = 1;
            int botrightY = 1;

            while(!finished) {
                while (line != null) {
                    while ((line = input.readLine()) != null && !line.startsWith("-")) {
                        String kv[] = keyValue(line);
                        switch (kv[0]) {
                            case "topleft":
                                coords = numbers(kv[1]);
                                topleftX = coords.get(0);
                                topleftY = coords.get(1);
                                break;
                            case "botright":
                                coords = numbers(kv[1]);
                                botrightX = coords.get(0);
                                botrightY = coords.get(1);
                                break;
                            case "level":
                                level = Integer.parseInt(kv[1]);
                                break;
                            default:
                                System.out.println("Unknown map keyword " + kv[0]);
                        }
                    }

                    Area area = new Area(topleftX, topleftY, level, botrightX, botrightY);
                    areas.add(area);
                    int y = topleftY;
                    while ((line = input.readLine()) != null && !line.equals("-") && !line.equals("--")) {
                        int x = topleftX;
                        for (int c = 0; c < line.length(); c++) {
                            char ch = line.charAt(c);
                            TileType tile = TileType.getFromChar(ch);
                            area.setTile(x, y, tile);
                            x++;
                        }
                        y++;
                    }
                    if(line == null || line.equals("--"))
                        finished = true;
                }
            }
        } catch (Exception e)   {
            System.out.println(e.toString());
        }
    }
    
    private void readMapHeader(BufferedReader input) throws IOException {
            String line;

    }

    public boolean canSmell(int x, int y, int z) {
        TileType tile = getTile(x,y,z);
        if (tile.canEnter()) {
            //if(getTopObject(x,y,z) == null || !getTopObject(x,y,z).isBlocked()) {
                return !(inNoMonsterArea(x,y,z));
            //}
        }
        return false;
    }
    
    public boolean canEnter(Obj ob, int x, int y, int z) {
        TileType tile = getTile(x,y,z);
        if (tile.canEnter()) {
            if(getTopObject(x,y,z) == null || ob.isPlayer() || !getTopObject(x,y,z).isBlocked()) {
                return !(ob.isMonster() && inNoMonsterArea(x,y,z));
            }
        }
        if(ob.isPlayer()) {
            ObjPlayer player = (ObjPlayer) ob;
            if (tile == TileType.water && player.isWater()) return true;
            if ((tile == TileType.forcefield || tile == TileType.hiddenff) && player.isForcefield()) return true;
        }
        return false;
    }
    
    private boolean inNoMonsterArea(int x, int y, int z) {
        for(Area a : nomonsters) {
            if (a.getLevel() == z && a.getTopleft().getX() <= x && a.getTopleft().getY() <= y && a.getBotright().getX() >= x && a.getBotright().getY() >= y) {
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
        Area area = getArea(x,y,z);
        if(area==null) return;
        area.setTile(x,y,tile);
    }
    
    public void changeTile(int x, int y, int z, TileType tile) {
        setTile(x,y,z,tile);
    }

    public void setIcon(int x, int y, int z, int icon) {
        TileType tile = TileType.getTileType(icon);
        setTile(x,y,z,tile);
    }

    public CodeStore getCodeStore() {
        return codeStore;
    }

    public String talkTo(int id, String word, Engine engine) {
        return talkStore.getReply(id, word, engine);
    }

    public boolean talkFirst(int id) {
        return talkStore.replyFirst(id);
    }

    public void talkTo(Engine engine, int other, int x, int y, int z) {
        engine.getPlayer().startTalking(engine, other, x, y, z);
    }

    public void replyTo(Engine engine, String saying) {
        String reply = engine.getWorld().talkTo(id, saying, engine);
    }

    public int getDeathX() {
        return deathX;
    }

    public int getDeathY() {
        return deathY;
    }

    public int getDeathZ() {
        return deathZ;
    }
}
