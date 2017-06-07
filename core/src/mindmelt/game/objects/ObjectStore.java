package mindmelt.game.objects;

import com.opencsv.CSVReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import mindmelt.game.maps.World;

public class ObjectStore {
    static public final int PLAYER_ID = 1;
    static public final int NUM_OBJECTS = 256;
    private List<Obj> current; 
    private Obj[] objects = new Obj[NUM_OBJECTS];

    public void convertObjects(String fileFrom, String fileTo) {
        try {
            CSVReader reader = new CSVReader(new FileReader("data/"+fileFrom),',','\'',1);
            File file = new File("data/"+fileTo);
            BufferedWriter output = new BufferedWriter(new FileWriter(file));

            String line[];
            output.write("id,name,desc,type,x,y,z,map,inside,order,strength,icon\n");
            while ( (line = reader.readNext()) != null) {
                String id = line[0].trim();
                String x = line[1].trim();
                String y = line[2].trim();
                String up = line[3].trim();
                String down = line[4].trim();
                String map = line[5].trim();
                Integer i = new Integer(line[6].trim())+60;
                String icon = i.toString();
                String str = line[7].trim();
                output.write(id+",name,desc,type,"+x+","+y+",0,"+map+",0,0,"+str+","+icon+"\n");
            }
            reader.close();
            output.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void loadObjects(String filename) {
        Random rand = new Random();
        try {
            CSVReader reader = new CSVReader(new FileReader("data/"+filename+".obj"),',','\'',1);
            String line[];
            while ( (line = reader.readNext()) != null) {
                int id = Integer.parseInt(line[0]);
                String name = line[1];
                String desc = line[2];
                String type = line[3];
                int x = Integer.parseInt(line[4]);
                int y = Integer.parseInt(line[5]);
                int z = Integer.parseInt(line[6]);
                int mapId = Integer.parseInt(line[7]);
                int inside = Integer.parseInt(line[8]);
                int order = Integer.parseInt(line[9]);
                int strength = Integer.parseInt(line[10]);
                Obj in = objects[inside];
                int icon = Integer.parseInt(line[11]);
                Obj ob = Obj.builder(type).id(id).name(name).description(desc).setCoords(x,y,z).inside(in).mapId(mapId).order(order).strength(strength).icon(icon);
                ob.setSpeed(0.2f);
                objects[id] = ob;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void initMap(World world) {
        int mapId = world.getId();
        current = new ArrayList<Obj>();
        for (int i=1; i<NUM_OBJECTS; i++) {
            if (objects[i]==null) 
                continue;
            Obj ob = objects[i];
            if (ob.getMapId() == mapId) {
                current.add(ob);
                if (ob.isInMap())
                    ob.moveToMap(world);
            }
        }
    }
    
    public Obj getPlayer() {
        return objects[PLAYER_ID];
    }
    
    public List<Obj> getActiveObjects() {
        return current;
    }
    
    public void addToActiveObjects(Obj ob) {
        if (!ob.isInMap()) 
            return;
        if(!current.contains(ob))
            current.add(ob);
    }
}
