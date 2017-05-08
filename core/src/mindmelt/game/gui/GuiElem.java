package mindmelt.game.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import mindmelt.game.MindmeltGDX;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 3/05/2017.
 */
public abstract class GuiElem {
    static final protected int SZ = 32;
    static final protected int pixel = 184;
    static final protected int height = 480-32;
    static final protected boolean debug = false;

    static final protected int SQUARE = 124;
    static final protected int TOP = 125;
    static final protected int LEFT = 126;
    static final protected int BOTTOM = 127;
    static final protected int RIGHT = 128;

    protected int x;
    protected int y;
    protected int w;
    protected int h;
    protected GuiElem parent;
    protected String name;

    List<GuiElem> children = new ArrayList<>();
    Sound wilhelm = Gdx.audio.newSound(Gdx.files.internal("sound/wilhelm.ogg"));

    protected GuiElem(int x, int y, int w, int h) {
        this.x = x*SZ;
        this.y = y*SZ;
        this.w = w*SZ;
        this.h = h*SZ;
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

    public GuiElem click(int x, int y, MindmeltGDX game) {
        int xx = x-this.x;
        int yy = y-this.y;
        Gdx.app.log(getName(),String.format("x=%d,y=%d",xx,yy));
        GuiElem el = findElem(xx,yy);
        if (el != null)
            return el.click(xx,yy, game);
        //do something
        Gdx.app.log("do it",String.format("%s:x=%d,y=%d",getName(),xx,yy));

        activate(x,y,game);

        return this;
    }

    protected void activate(int x, int y, MindmeltGDX game) {
        //do nowt for now
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

    protected void renderChildren(MindmeltGDX game, float delta) {
        for(GuiElem child: children) {
            child.render(game,delta);
        }
    }

    protected void renderThis(MindmeltGDX game, float delta) {
        // draw lines
    }

    public void render(MindmeltGDX game, float delta) {
        renderChildren(game,delta);
        renderThis(game,delta);
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

    protected void rectangle(int x, int y, int w, int h, MindmeltGDX game) {
        for (int xx=x; xx<x+w; xx++) {
            plot(xx,y,Color.CYAN,game);
            plot(xx,y+h, Color.RED,game);
        }
        for (int yy=y; yy<y+h; yy++) {
            plot(x,yy,Color.YELLOW,game);
            plot(x+w,yy, Color.MAGENTA,game);
        }
        game.batch.setColor(Color.WHITE);
    }

    protected void plot(int x, int y, Color color, MindmeltGDX game) {
        game.batch.setColor(color);
        game.batch.draw(game.getTile(pixel), x, height - y);
    }

    protected void drawIcon(int x, int y, int icon, MindmeltGDX game) {
        drawIcon(x,y,icon,Color.WHITE,game);
    }

    protected void drawIcon(int x, int y, int icon, Color color, MindmeltGDX game) {
        game.batch.setColor(color);
        game.batch.draw(game.getTile(icon), x, height - y);
    }

    protected void drawTile(int x, int y, int icon, int brightness, MindmeltGDX game) {
        Color color = new Color(brightness/256,brightness/256,brightness/256,1);
        drawIcon(x*SZ,y*SZ,icon,color,game);
    }

    protected void drawTile(int x, int y, int icon, Color color, MindmeltGDX game) {
        drawIcon(x*SZ,y*SZ,icon,color,game);
    }
}
