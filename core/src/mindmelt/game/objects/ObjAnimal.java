/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mindmelt.game.objects;

import mindmelt.game.engine.Engine;

/**
 *
 * @author w18749
 */
public class ObjAnimal extends Obj {

    public void update(Engine engine, float delta) {
        updateMessage(engine);

        //don't move if next to player
        ObjPlayer player = engine.getPlayer();
        if(abs(x-player.getX())<=1 && abs(y-player.getY())<=1 ) return;

        int dx = x;
        int dy = y;

        if (rand.nextInt(2)==0) {
            dx += rand.nextInt(2)*2-1;
        } else {
            dy += rand.nextInt(2)*2-1;
        }

        if (isReady(engine)) {
            if (engine.canEnter(this, dx, dy, z)) {
                engine.moveObjToMap(this, dx, dy, z);
                resetWait(engine);
            }
        }
    }
}
