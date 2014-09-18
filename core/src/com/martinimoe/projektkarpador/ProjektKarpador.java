package com.martinimoe.projektkarpador;

import actors.Fish;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ShortArray;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class ProjektKarpador extends ApplicationAdapter implements ApplicationListener, InputProcessor {
	private SpriteBatch batch;
	private PolygonSpriteBatch pBatch;
	private Fish myFish;
	private Stage stage;
	private PolygonRegion pRegion;
	private float screensPerLevel = 2;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		pBatch = new PolygonSpriteBatch();
		
		// Texturen laden
		TextureAtlas atlas;
		atlas = new TextureAtlas(Gdx.files.internal("KarpadorPack.pack"));
		
		// Spieler (Fisch) erzeugen
		myFish = new Fish(atlas);
		
		// Stage (Level) erzeugen und Fisch als Actor hinzufügen
		stage = new Stage(new ScreenViewport());
	    Gdx.input.setInputProcessor(stage);
	    stage.addActor(myFish);
		
	    // kA, stand im Tutorial
		Gdx.input.setInputProcessor(this);
		
		// Landscape generieren. Hochkompliziert!
		float[] vertices = new float[40];
		// Der erste Punkt ist am Koordinatenursprung
		vertices[0] = 0;
		vertices[1] = 0;
		for (int i = 2; i < vertices.length-2; i++) {
			// Jede Zweite Koordinate ist x-Koordinate
			// Diese sollen gleichmäßig verteilt sein.
			if(i%2 == 0){
				vertices[i] = i * ((stage.getWidth() * screensPerLevel) / vertices.length);
			}
			// Sonst Höhe zufällig berechnen
			else{
				vertices[i] = MathUtils.random(10, 100);
			}
		}
		// Der letzte ist punkt ist an ( Levelende | 0 )
		vertices[vertices.length-2] = stage.getWidth() * screensPerLevel;
		vertices[vertices.length-1] = 0;
		
		// Der ECT berechnet aus den koordinaten die polygone, danach wird konvertiert
		EarClippingTriangulator ect = new EarClippingTriangulator();
		ShortArray sa = ect.computeTriangles(vertices);
		short[] shortarray = new short[sa.size];
		for(int i = 0; i < sa.size; i++) {
			shortarray[i] = sa.get(i);
		}
		// Die PolygonRegion enthält den Levelboden
		pRegion = new PolygonRegion(atlas.findRegion("Terrain/terrain"), vertices, shortarray);
		
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		pBatch.begin();
		pBatch.draw(pRegion, 0, 0);
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
		// TODO Auto-generated method stub
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
	}
	
	public void dispose() {
	    stage.dispose();
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
//		imgMove = false;
		
		return false;
	}
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
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