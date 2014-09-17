package com.martinimoe.projektkarpador;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Scaling;

public class ProjektKarpador extends ApplicationAdapter implements ApplicationListener, InputProcessor {
	AtlasRegion grassLeft;
	AtlasRegion grassMiddle;
	AtlasRegion grassRight;
	AtlasRegion playerStanding;
	
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private int imgX = 20;
	private int imgY = 70;
	private boolean imgMove = false;
	private int imgMoveOffsetX = 0;
	private int imgMoveOffsetY = 0;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		
		TextureAtlas atlas;
		atlas = new TextureAtlas(Gdx.files.internal("KarpadorAtlas.pack"));
		grassLeft = atlas.findRegion("Tiles/grassLeft");
		grassMiddle = atlas.findRegion("Tiles/grassMid");
		grassRight = atlas.findRegion("Tiles/grassRight");
		playerStanding = atlas.findRegion("Player/p1_stand");
		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera(w, h);
		camera.setToOrtho(false, w * 100, h * 100);
		
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(grassLeft, 0, 0);
		batch.draw(grassMiddle, 70, 0);
		batch.draw(grassRight, 140, 0);
		batch.draw(playerStanding, imgX, imgY);
		batch.end();
	}
	
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		int w = Gdx.graphics.getWidth();
		int h = Gdx.graphics.getHeight();
		
		if (screenX >= imgX && screenX < imgX + playerStanding.packedWidth &&
		    (h - screenY) >= imgY && (h - screenY) < imgY + playerStanding.packedHeight)
		{
			imgMove = true;
			
			imgMoveOffsetX = screenX - imgX;
			imgMoveOffsetY = (h - screenY) - imgY;
		}
		
		System.out.println("pos: " + screenX + ", " + screenY);

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		imgMove = false;
		
		return false;
	}
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		if (imgMove)
		{
			int w = Gdx.graphics.getWidth();
			int h = Gdx.graphics.getHeight();
			
			imgX = screenX - imgMoveOffsetX;
			imgY = (h - screenY) - imgMoveOffsetY;
		}
		
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}
}