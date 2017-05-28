package mindmelt.game.windows;

import mindmelt.game.MindmeltGDX;
import mindmelt.game.gui.Window;
import mindmelt.game.objects.Obj;
import mindmelt.game.objects.ObjPlayer;

import java.util.List;

/**
 * Created by David on 28/05/2017.
 */
public class BackpackWindow extends Window {
    public BackpackWindow(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    @Override
    protected void renderThis(MindmeltGDX game, float delta) {
        ObjPlayer player = game.player;
        List<Obj> backpack = player.getObjects();
        if(backpack==null) return;
        int xx=0;
        int yy=0;
        for(Obj item : backpack) {
            drawIcon(xx,yy,item.getIcon(),game);
            if((xx+=SZ)>=w) {
                xx=0;
                yy+=SZ;
            }
        }
    }

    @Override
    protected void activate(int x, int y, MindmeltGDX game) {
        int pos = ((x/SZ)+(y/SZ)*(w/SZ));
        System.out.println(String.format("Backpack %d,%d = %d",x/SZ,y/SZ,pos));
        List<Obj> backpack = game.player.getObjects();
        if(backpack==null || pos+1>backpack.size()) return;
        Obj item = backpack.get(pos);
        System.out.println("item = "+item.getId());
        item.moveToMap(game.player.getX(),game.player.getY(),game.player.getZ(),game.world,game.objects);
    }
}
