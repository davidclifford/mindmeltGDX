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
public class ObjThing extends Obj {

    @Override
    public void update(Engine engine, float delta) {
        updateMessage(engine);

        //throwing
        if(!inAir || !isReady(engine)) {
            return;
        }
        if(throwTime < engine.getSystemTime()) {
            inAir = false;
            engine.moveObjToMap(this, x,y,z);
            return;
        }

        //move or drop
        int xn = x+ix;
        int yn = y+iy;
        if(engine.canEnter(this,xn,yn,z)) {
            engine.moveObjToMap(this,xn,yn,z);
        } else {
            inAir = false;
            engine.moveObjToMap(this,x,y,z);
        }
        resetWait(engine);
    }
}
