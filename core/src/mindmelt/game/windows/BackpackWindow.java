package mindmelt.game.windows;

import mindmelt.game.MindmeltGDX;
import mindmelt.game.gui.Window;
import mindmelt.game.objects.Inventory;
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
        int xx=0;
        int yy=0;
        for(int pos=0; pos<24; pos++) {
            Obj item = player.inventory.getObject(pos);
            drawIcon(xx,yy,152,game);
            if(item!=null)
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
        Inventory inv = game.player.inventory;
        if(inv.getHandObject()==null) {
            inv.inventoryToHand(pos);
        } else {
            if(inv.getObject(pos)==null)
                inv.handToInventory(pos);
        }
    }
}
