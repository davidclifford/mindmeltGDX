package mindmelt.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import mindmelt.game.MindmeltGDX;

/**
 * Created by David on 27/04/2017.
 */
public class StartScreen  implements Screen {
    private MindmeltGDX game;
    private Music music;

    public StartScreen(MindmeltGDX game) {
        this.game = game;
        music = game.manager.get("theme.ogg");
        music.play();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.draw(game.startScreen, 0, 0);
        game.batch.end();

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            dispose();
            game.dispose();
            System.exit(0);
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            game.setScreen(new PlayScreen(game));
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
    }
}
