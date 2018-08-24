package mindmelt.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import mindmelt.game.MindmeltGDX;

import java.util.Random;

public class EndScreen  implements Screen {
    private MindmeltGDX game;
    private Music music;
    private Stage stage;
    private Skin skin;
    private BitmapFont font;
    private Random rand;

    public EndScreen(MindmeltGDX game) {
        this.game = game;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        font = game.font;
        font.getData().setScale(2);
        rand = new Random();
    }

    @Override
    public void render(float delta) {
        Batch batch = game.batch;
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(game.startScreen, 0, 0);

        int x = 160+rand.nextInt(8);
        int y = 300+rand.nextInt(8);
        font.setColor(Color.BLACK);
        font.draw(batch, "You have defeated Omgra!",x,y);
        font.setColor(Color.YELLOW);
        font.draw(batch, "You have defeated Omgra!",x-2,y-2);
        batch.end();

        stage.act(delta);
        stage.draw();

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new StartScreen(game));
            dispose();
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.L)) {
            game.setScreen(new PlayScreen(game,"load"));
            dispose();
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.N)) {
            game.setScreen(new PlayScreen(game,"new"));
            dispose();
        }
    }

    @Override
    public void dispose() {
        font.getData().setScale(1);
        stage.dispose();
    }

    @Override
    public void show() {

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
}
