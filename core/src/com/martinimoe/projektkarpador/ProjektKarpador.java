package com.martinimoe.projektkarpador;

import gamestates.Game;
import gamestates.MenuMain;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

public class ProjektKarpador extends ApplicationAdapter implements ApplicationListener, InputProcessor, ContactListener {

	private GameContext gameContext = null;
	
	private Music music = null;
	
	
	@Override
	public void create () {
		// Texturen laden
		TextureAtlas atlas;
		atlas = new TextureAtlas(Gdx.files.internal("Karpador.pack"));
				
		// GameContext hält globale Objekte
		gameContext = new GameContext(new World(new Vector2(0, -9f), false), atlas);
		
		gameContext.setMenuMain(new MenuMain(gameContext));
		gameContext.getMenuMain().create();
		gameContext.setGame(new Game(gameContext));
		gameContext.getGame().create();
		gameContext.setGameState(gameContext.getMenuMain());
		
		//Musik!
		music = Gdx.audio.newMusic(Gdx.files.internal("Karpador2.wav"));
		music.setLooping(true);
		if (!gameContext.isMute()) music.play();
	    
	    // Vsync
		//Gdx.graphics.setVSync(true);
		Gdx.graphics.getGL20();
		
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.55f, .81f, 0.95f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gameContext.addDelta(Gdx.graphics.getDeltaTime());
		// World step
		gameContext.getWorld().step(Gdx.graphics.getDeltaTime(), 6, 2);
		gameContext.getGameState().render();
	}
	
	@Override
	public void resize(int width, int height) {
		gameContext.getGameState().resize(width, height);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
	
	public void dispose() {
	    music.dispose();
	    gameContext.getGame().dispose();
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return gameContext.getGame().touchDown(screenX, screenY, pointer, button);
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
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

	@Override
	public void beginContact(Contact contact) {
	}

	@Override
	public void endContact(Contact contact) {

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}
}