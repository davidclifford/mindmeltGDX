package mindmelt.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import mindmelt.game.MindmeltGDX;

/**
 * Created by David on 27/04/2017.
 */
public class StartScreen  implements Screen {
    private MindmeltGDX game;
    private Music music;
    private Stage stage;
    private Skin skin;
    private TextButton startGame;
    private TextButton exitGame;
    private TextButton loadGame;
    private TextButton saveGame;

    public StartScreen(MindmeltGDX game) {
        this.game = game;
        this.skin = game.skin;

        music = game.manager.get("sound/theme.ogg");
        music.play();

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        startGame = button("Start NEW Game", skin);
        loadGame = button("Load Game",skin);
        saveGame = button("Save Game", skin);
        exitGame = button("Exit Game",skin);

        startGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                StartScreen.this.game.setScreen(new PlayScreen(game,"new"));
                StartScreen.this.dispose();
            }
        });

        loadGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                StartScreen.this.game.setScreen(new PlayScreen( game,"load"));
                StartScreen.this.dispose();
            }
        });

        saveGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                StartScreen.this.game.setScreen(new PlayScreen( game,"save"));
                StartScreen.this.dispose();
            }
        });

        exitGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                StartScreen.this.game.dispose();
                StartScreen.this.game.exit();
            }
        });

        Table table = new Table();
        table.setWidth(stage.getWidth());
        table.align(Align.center | Align.top);
        table.setPosition(0,Gdx.graphics.getHeight());
        table.padTop(160);
        table.add(startGame);
        table.row().pad(10);
        table.add(loadGame);
        table.row().pad(10);
        table.add(saveGame);
        table.row().pad(10);
        table.add(exitGame);

        stage.addActor(table);
    }

    private TextButton button(String text, Skin skin) {
        TextButton but = new TextButton(text, skin);
        TextButton.TextButtonStyle tbs = new TextButton.TextButtonStyle(but.getStyle());
        NinePatch np = new NinePatch(skin.getPatch("default-round"));
        np.setColor(Color.MAGENTA);
        tbs.up = new NinePatchDrawable(np);
        np = new NinePatch(skin.getPatch("default-round"));
        np.setColor(Color.WHITE);
        tbs.down = new NinePatchDrawable(np);
        but.setStyle(tbs);
        return but;

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Batch batch = game.batch;
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(game.startScreen, 0, 0);
        batch.end();

        stage.act(delta);
        stage.draw();

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            dispose();
            game.exit();
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.L)) {
            game.setScreen(new PlayScreen(game,"load"));
            dispose();
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            game.setScreen(new PlayScreen(game,"save"));
            dispose();
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.N)) {
            game.setScreen(new PlayScreen(game,"new"));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        music.stop();
        stage.dispose();
    }
}
