package mindmelt.game.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import mindmelt.game.MindmeltGDX;
import mindmelt.game.engine.Engine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 3/05/2017.
 */
public abstract class GuiElem {
    static final public int SZ = 32;
    static final protected int pixel = 184;
    static final protected int height = 480 - 32;
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
        this.x = x * SZ;
        this.y = y * SZ;
        this.w = w * SZ;
        this.h = h * SZ;
    }

    protected int getX() {
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

    public GuiElem click(int x, int y, Engine engine) {
        int xx = x - this.x;
        int yy = y - this.y;
        Gdx.app.log(getName(), String.format("x=%d,y=%d", xx, yy));
        GuiElem el = findElem(xx, yy);
        if (el != null)
            return el.click(xx, yy, engine);
        //do something
        Gdx.app.log("do it", String.format("%s:x=%d,y=%d", getName(), xx / SZ, yy / SZ));

        activate(xx, yy, engine);

        return this;
    }

    protected void activate(int x, int y, Engine engine) {
        //do nowt for now
    }

    private GuiElem findElem(int x, int y) {
        for (GuiElem el : children) {
            if (el.isInside(x, y))
                return el;
        }
        return null;
    }

    private boolean isInside(int x, int y) {
        return (x >= this.x && x <= (this.x + this.w) && y >= this.y && y <= (this.y + this.h));
    }

    protected void renderChildren(MindmeltGDX game, float delta) {
        for (GuiElem child : children) {
            child.render(game, delta);
        }
    }

    protected void renderThis(MindmeltGDX game, float delta) {
        // draw lines
    }

    public void render(MindmeltGDX game, float delta) {
        renderChildren(game, delta);
        renderThis(game, delta);
    }

    public int getAbsX() {
        if (parent == null)
            return x;
        else
            return x + parent.getAbsX();
    }

    public int getAbsY() {
        if (parent == null)
            return y;
        else
            return y + parent.getAbsY();
    }

    protected void rectangle(int x, int y, int w, int h, MindmeltGDX game) {
        for (int xx = x; xx < x + w; xx++) {
            plot(xx, y, Color.CYAN, game);
            plot(xx, y + h, Color.RED, game);
        }
        for (int yy = y; yy < y + h; yy++) {
            plot(x, yy, Color.YELLOW, game);
            plot(x + w, yy, Color.MAGENTA, game);
        }
        game.batch.setColor(Color.WHITE);
    }

    protected void plot(int x, int y, Color color, MindmeltGDX game) {
        int xx = getAbsX() + x;
        int yy = getAbsY() + y;
        game.batch.setColor(color);
        game.batch.draw(game.getTile(pixel), xx, height - yy);
    }

    protected void plot2(int x, int y, Color color, MindmeltGDX game) {
        plot(x,y,color,game);
        plot(x+1,y,color,game);
        plot(x,y+1,color,game);
        plot(x+1,y+1,color,game);
    }

    protected void plot(int pixSize, int x, int y, Color color, MindmeltGDX game) {
        for(int p1=0;p1<pixSize;p1++) {
            for(int p2=0;p2<pixSize;p2++) {
                plot(x + p1, y + p2, color, game);
            }
        }
    }

    protected void drawText(int x, int y, Color color, String text, MindmeltGDX game) {
        game.font.setColor(color);
        game.font.draw(game.batch, text, getAbsX() + x, Gdx.graphics.getHeight() - getAbsY() - y);
    }

    protected void drawString(int x, int y, Color color, String text, MindmeltGDX game) {
        game.font.setColor(color);
        game.font.draw(game.batch, text, getAbsX() + x * SZ + 1, height - (getAbsY() + y * SZ - SZ / 2) - 1);
        game.font.draw(game.batch, text, getAbsX() + x * SZ - 1, height - (getAbsY() + y * SZ - SZ / 2) - 1);
        game.font.draw(game.batch, text, getAbsX() + x * SZ + 1, height - (getAbsY() + y * SZ - SZ / 2) + 1);
        game.font.draw(game.batch, text, getAbsX() + x * SZ - 1, height - (getAbsY() + y * SZ - SZ / 2) + 1);
        game.font.draw(game.batch, text, getAbsX() + x * SZ + 2, height - (getAbsY() + y * SZ - SZ / 2) - 2);
        game.font.draw(game.batch, text, getAbsX() + x * SZ - 2, height - (getAbsY() + y * SZ - SZ / 2) - 2);
        game.font.draw(game.batch, text, getAbsX() + x * SZ + 2, height - (getAbsY() + y * SZ - SZ / 2) + 2);
        game.font.draw(game.batch, text, getAbsX() + x * SZ - 2, height - (getAbsY() + y * SZ - SZ / 2) + 2);
        game.font.setColor(Color.WHITE);
        game.font.draw(game.batch, text, getAbsX() + x * SZ, height - (getAbsY() + y * SZ - SZ / 2));
    }


    protected void drawMidString(int x, int y, Color color, String text, MindmeltGDX game) {
        game.font.setColor(Color.BLACK);
        float offset = (text.length()) * 3f - SZ / 2f;
        game.font.draw(game.batch, text, getAbsX() + x * SZ + 1 - offset, height - (getAbsY() + y * SZ - SZ / 2));
        game.font.draw(game.batch, text, getAbsX() + x * SZ - 1 - offset, height - (getAbsY() + y * SZ - SZ / 2));
        game.font.draw(game.batch, text, getAbsX() + x * SZ + 2 - offset, height - (getAbsY() + y * SZ - SZ / 2));
        game.font.draw(game.batch, text, getAbsX() + x * SZ - 2 - offset, height - (getAbsY() + y * SZ - SZ / 2));
        game.font.draw(game.batch, text, getAbsX() + x * SZ - offset, height - (getAbsY() + y * SZ - SZ / 2) - 1);
        game.font.draw(game.batch, text, getAbsX() + x * SZ - offset, height - (getAbsY() + y * SZ - SZ / 2) + 1);
        game.font.setColor(color);
        game.font.draw(game.batch, text, getAbsX() + x * SZ - offset, height - (getAbsY() + y * SZ - SZ / 2));
        game.font.draw(game.batch, text, getAbsX() + x * SZ - offset + 1, height - (getAbsY() + y * SZ - SZ / 2));
    }

    protected void drawMidStringInBox(int x, int y, Color color, String text, MindmeltGDX game) {
        game.font.setColor(Color.BLACK);
        float offset = (text.length()) * 3f - SZ / 2f;
        drawBox(x, y, text.length(), color, game);
        game.font.setColor(color);
        game.font.draw(game.batch, text, getAbsX() + x * SZ - offset, height - (getAbsY() + y * SZ - SZ / 2));
        game.font.draw(game.batch, text, getAbsX() + x * SZ - offset + 1, height - (getAbsY() + y * SZ - SZ / 2));
    }

    protected void drawBox(int x, int y, int width, Color color, MindmeltGDX game) {
        float offset = (width) * 3f - SZ/2f + SZ/8f;
        Pixmap box;
        int w = width*7+7;
        box = new Pixmap(w, SZ, Pixmap.Format.RGBA8888);
        box.setColor(Color.BLACK);
        box.fillRectangle(0,0,w, SZ/2);
        box.setColor(color);
        box.drawRectangle(0, 0, w, SZ/2);
        Texture boxtex = new Texture( box );
        game.batch.draw(boxtex, getAbsX() + x*SZ - offset, height - (getAbsY()+y*SZ) - SZ/3);
        box.dispose();
    }

    protected void drawIcon(int x, int y, int icon, MindmeltGDX game) {
        drawIcon(x, y, icon, Color.WHITE, game);
    }

    protected void drawIcon(int x, int y, int icon, Color color, MindmeltGDX game) {
        game.batch.setColor(color);
        game.batch.draw(game.getTile(icon), x + getAbsX(), height - (y + getAbsY()));
    }

    protected void drawTile(int x, int y, int icon, float brightness, MindmeltGDX game) {
        Color color = new Color(brightness, brightness, brightness, 1);
        drawIcon(x * SZ, y * SZ, icon, color, game);
    }

    protected void drawTile(int x, int y, int icon, Color color, MindmeltGDX game) {
        drawIcon(x * SZ, y * SZ, icon, color, game);
    }

    protected void zap(int x1, int y1, int x2, int y2, MindmeltGDX game) {
        float dx = (x2-x1);
        float dy = (y2-y1);
        float dist = abs((int)dx)+abs((int)dy)*2;
        float xx1 = x1 * SZ + SZ / 2;
        float yy1 = y1 * SZ + SZ / 2;
        for(int i=0; i<dist; i++) {
            int rx = MathUtils.random(SZ)-SZ/2;
            int ry = MathUtils.random(SZ)-SZ/2;
            float xx2 = xx1+(dx/dist)*SZ+rx/2;
            float yy2 = yy1+(dy/dist)*SZ+ry/2;
            drawLine((int)xx1, (int)yy1, (int)xx2, (int)yy2, Color.CYAN, game);
            xx1=xx2;
            yy1=yy2;
        }
        drawLine((int)xx1, (int)yy1, (int)x2*SZ+SZ/2, (int)y2*SZ+SZ/2, Color.CYAN, game);
    }

    private void drawLine(int x0, int y0, int x1, int y1, Color colour, MindmeltGDX game) {
        if (abs(y1 - y0) < abs(x1 - x0)) {
            if (x0 > x1)
                plotLineLow(x1, y1, x0, y0, colour, game);
            else
                plotLineLow(x0, y0, x1, y1, colour, game);
        } else {
            if (y0 > y1)
                plotLineHigh(x1, y1, x0, y0, colour, game);
            else
                plotLineHigh(x0, y0, x1, y1, colour, game);
        }
    }

    private void plotLineLow(int x0, int y0, int x1, int y1, Color colour, MindmeltGDX game) {
        int dx = x1 - x0;
        int dy = y1 - y0;
        int yi = 1;
        if (dy < 0) {
            yi = -1;
            dy = -dy;
        }
        int d = 2 * dy - dx;
        int y = y0;

        for (int x = x0; x < x1; x++) {
            plot2(x,y,colour,game);
            if (d > 0) {
                y = y + yi;
                d = d - 2 * dx;
            }
            d = d + 2 * dy;
        }
    }

    private void plotLineHigh(int x0, int y0, int x1, int y1, Color colour, MindmeltGDX game) {
        int dx = x1 - x0;
        int dy = y1 - y0;
        int xi = 1;
        if (dx < 0) {
            xi = -1;
            dx = -dx;
        }
        int d = 2 * dx - dy;
        int x = x0;

        for (int y = y0; y < y1; y++) {
            plot2(x, y, colour, game);
            if (d > 0) {
                x = x + xi;
                d = d - 2 * dy;
            }
            d = d + 2 * dx;
        }
    }

    private int abs(int num) { return num<0?-num:num;}
}
