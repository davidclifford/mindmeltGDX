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
    }
}
