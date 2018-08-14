package mindmelt.game.spells;

import mindmelt.game.engine.Engine;
import mindmelt.game.objects.Obj;
import mindmelt.game.objects.ObjPerson;
import mindmelt.game.objects.ObjPlayer;

public class MindMeltSpell extends Spell {

    private static final int NOONE = 0;
    private static final int WRONG = 1;
    private static final int MINDMELT = 2;
    private static final int ALREADY = 3;

    public void activate(Engine engine) {
        String message ="Wrong Person!";
        int status = checkSurround(engine);
        if(status==MINDMELT) {
            message = "Mindmelted!";
            engine.getPlayer().levelUp();
        } else if(status==NOONE) {
            message = "No-one to Mindmelt!";
        } else if(status==ALREADY) {
            message = "Already Mindmelted!";
        }
//        EntryExit circle = new EntryExit(0, 0, 0, 40, 39, 0, "world", "Circle");
//        engine.moveToMap(circle);
        engine.addMessage(message);
    }

    private int checkSurround(Engine engine) {
        ObjPlayer player = engine.getPlayer();
        int numPeople = 0;
        for(int y=-1;y<=1;y++) {
            for (int x = -1; x <= 1; x++) {
                Obj person = engine.getTopObject(player.getX() + x, player.getY() + y, player.getZ());
                if (person != null) {
                    if (person.isPerson()) {
                        numPeople++;
                        int status = mindmelt((ObjPerson)person, player);
                        if(status==ALREADY || status==MINDMELT) return status;
                    } else if (person.isOmgra()) {
                        engine.finishGame();
                    }
                }
            }
        }
        if(numPeople>0) return WRONG;
        return NOONE;
    }

    private int mindmelt(ObjPerson person, ObjPlayer player) {
        if (person.getId()>=40 && person.getId()<=45) {
            if (player.alreadyMindmelted(person))
                return ALREADY;
            else {
                player.setMindmelted(person);
                return MINDMELT;
            }
        }
        return WRONG;
    }

}
