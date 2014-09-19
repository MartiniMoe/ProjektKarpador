package com.martinimoe.projektkarpador;

import actors.Fish;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class ProjektKarpador extends ApplicationAdapter implements ApplicationListener, InputProcessor {
	private SpriteBatch batch;
	private PolygonSpriteBatch pBatch;
	private Fish myFish;
	private Stage stage;
	private Camera camera;
	private GameContext gameContext = null;
	private Terrain terrain = null;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		pBatch = new PolygonSpriteBatch();
		
		// Texturen laden
		TextureAtlas atlas;
		atlas = new TextureAtlas(Gdx.files.internal("KarpadorPack.pack"));

		gameContext = new GameContext(new World(new Vector2(0, -9f), false), atlas);
		
		terrain = new Terrain(8000, gameContext);
		
		// Spieler (Fisch) erzeugen
		myFish = new Fish(gameContext, 500, 500);
		
		//Camera erzeugen 
		camera = new OrthographicCamera();
		// Stage (Level) erzeugen und Fisch als Actor hinzufügen
		stage = new Stage(new ExtendViewport(1280, 720,camera));
	    Gdx.input.setInputProcessor(stage);
	    stage.addActor(myFish);
	    
	    // kA, stand im Tutorial
		Gdx.input.setInputProcessor(this);
		
		
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		gameContext.getWorld().step(Gdx.graphics.getDeltaTime(), 6, 2);
		
		//stage.getCamera().translate(-1f, 0, 0);
		stage.getCamera().update();
		pBatch.setProjectionMatrix(camera.combined);
		pBatch.begin();
		terrain.draw(pBatch);
		pBatch.end();
		
		batch.begin();
		stage.act(Gdx.graphics.getDeltaTime());
	    stage.draw();
		batch.end();
	}
	
	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
	
	public void dispose() {
	    stage.dispose();
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
//		int w = Gdx.graphics.getWidth();
//		int h = Gdx.graphics.getHeight();
		
//		if (screenX >= imgX && screenX < imgX + fish.packedWidth &&
//		    (h - screenY) >= imgY && (h - screenY) < imgY + fish.packedHeight)
//		{
//			imgMove = true;
//			
//			imgMoveOffsetX = screenX - imgX;
//			imgMoveOffsetY = (h - screenY) - imgY;
//		}
		
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
//		imgMove = false;
		
		return false;
	}
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
//		if (imgMove)
//		{
//			int w = Gdx.graphics.getWidth();
//			int h = Gdx.graphics.getHeight();
//			
//			if (screenX >= 0 && screenX <= w &&
//				screenY >= 0 && screenY <= h) 
//			{
//				imgX = screenX - imgMoveOffsetX;
//				imgY = (h - screenY) - imgMoveOffsetY;
//			}
//		}
		
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
	

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}
}