package mindmelt.game.objects;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import mindmelt.game.maps.World;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
                if (line[0].startsWith("//")) continue;
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
                if (line[0].startsWith("//")) continue;
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
                int icon = Integer.parseInt(line[11]);
                long speed = Long.parseLong(line[12]);
                Obj ob = Obj.builder(type).id(id).name(name).description(desc).setCoords(x,y,z).insideId(inside).mapId(mapId).order(order).strength(strength).icon(icon).speed(speed);
                //ob.setSpeed(rand.nextInt(10)+5L);
                //if(id==1) ob.setSpeed(2L);
                objects[id] = ob;
            }
            reader.close();

            //add objects to inventories
            for(Obj ob : objects) {
                if(ob!=null && ob.insideId!=0) {
                    ob.inside = objects[ob.insideId];
                    ob.inside.addObject(ob);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void saveObjects(String filename) {
        try {
            CSVWriter writer = new CSVWriter(new FileWriter("data/"+filename+".obj"),',','\'','\\',"\n");
            String header[] = {"id","name","desc","type","x","y","z","map","inside","order","strength","icon","speed"};
            writer.writeNext(header,false);
            for(Obj obj : objects) {
                if(obj==null) continue;
                String line[] = new String[13];
                line[0] = ""+obj.getId();
                line[1] = ""+obj.getName();
                line[2] = ""+obj.getDescription();
                line[3] = ""+obj.getType();
                line[4] = ""+obj.getX();
                line[5] = ""+obj.getY();
                line[6] = ""+obj.getZ();
                line[7] = ""+obj.getMapId();
                line[8] = ""+(obj.getInside()==null ? 0 : obj.getInside().getId());
                line[9] = ""+obj.getOrder();
                line[10] = ""+obj.getStrength();
                line[11] = ""+obj.getIcon();
                line[12] = ""+obj.getSpeed();

                writer.writeNext(line,false);
            }
            writer.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void initMap(World world) {
        int mapId = world.getId();
        current = new ArrayList<>();
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

    public boolean isObjectAt(int x, int y, int z, int id) {
        if(!current.contains(objects[id])) return false;
        return objects[id].x==x && objects[id].y==y &&objects[id].z==z;
    }

    public boolean isObjectIn(int obj, Obj insideOf) {
        return (objects[obj].inside == insideOf);
    }

    public void addToActiveObjects(Obj ob) {
        if (!ob.isInMap()) 
            return;
        if(!current.contains(ob))
            current.add(ob);
    }

    public Obj getObject(int ob) {
        if(ob<0 || ob>NUM_OBJECTS-1) return null;
        return objects[ob];
    }

    public void resetZap() {
        current.stream().forEach(ob -> {
            if(ob.isMonster()) ((ObjMonster)ob).setZapped(false);
        } );
    }
}
