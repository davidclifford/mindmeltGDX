package mindmelt.game.spells;

import mindmelt.game.MindmeltGDX;
import mindmelt.game.engine.Engine;

/**
 * Created by David on 18/06/2017.
 */
public class Spell {
    private String name;
    private String description;

    private boolean active;
    private boolean learned;

    private long time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isLearned() {
        return learned;
    }

    public void setLearned(boolean learned) {
        this.learned = learned;
    }

    public void update(Engine engine) {

    }

    public void activate(Engine engine) {}

    public void reset(Engine engine) {}

    public void render(MindmeltGDX game, float delta) {



    }
}
