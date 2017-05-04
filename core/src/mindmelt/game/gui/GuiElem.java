package mindmelt.game.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import mindmelt.game.MindmeltGDX;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 3/05/2017.
 */
public abstract class GuiElem {
    static final int SZ = 32;

    protected int x;
    protected int y;
    protected int w;
    protected int h;
    protected GuiElem parent;
    protected String name;

    List<GuiElem> children = new ArrayList<>();
    Sound wilhelm = Gdx.audio.newSound(Gdx.files.internal("sound/wilhelm.ogg"));

    protected GuiElem(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    protected  int getX() {
        return x;
    }

    protected void setX(int x) {
        this.x = x;
    }

    protected int getY() {
        return y;
    }

    protected void setY(int y) {
        this.y = y;
    }

    protected int getWidth() {
        return w;
    }

    protected void setWidth(int w) {
        this.w = w;
    }

    protected int getHeight() {
        return h;
    }

    protected void setHeight(int h) {
        this.h = h;
    }

    protected GuiElem getParent() {
        return parent;
    }

    protected void setParent(GuiElem parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public GuiElem setName(String name) {
        this.name = name;
        return this;
    }

    public GuiElem addElement(GuiElem elem) {
        children.add(elem);
        elem.setParent(this);
        return this;
    }

    public GuiElem click(int x, int y) {
        int xx = x-this.x;
        int yy = y-this.y;
        Gdx.app.log(getName(),String.format("x=%d,y=%d",xx,yy));
        GuiElem el = findElem(xx,yy);
        if (el != null)
            return el.click(xx,yy);
        //do something
        Gdx.app.log("do it",String.format("%s:x=%d,y=%d",getName(),xx,yy));

        if (this instanceof Button ) wilhelm.play();

        return this;
    }

    private GuiElem findElem(int x, int y) {
        for(GuiElem el : children) {
            if (el.isInside(x,y))
                return el;
        }
        return null;
    }

    private boolean isInside(int x, int y) {
        return (x>=this.x && x<=(this.x+this.w) && y>=this.y && y<=(this.y+this.h));
    }

    public void render(Batch batch, MindmeltGDX game) {
        for(GuiElem child: children) {
            child.render(batch,game);
        }
    }

    public int getAbsX() {
        if(parent==null)
            return x;
        else
            return x+parent.getAbsX();
    }

    public int getAbsY() {
        if(parent==null)
            return y;
        else
            return y+parent.getAbsY();
    }
}
