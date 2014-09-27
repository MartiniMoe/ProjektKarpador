package com.martinimoe.projektkarpador;

import actors.EvilCrab;
import actors.Fish;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class ProjektKarpador extends ApplicationAdapter implements ApplicationListener, InputProcessor, ContactListener {
	private SpriteBatch batch;
	private PolygonSpriteBatch pBatch;
	private Fish myFish;
	private Stage stage;
	private Camera camera;
	private GameContext gameContext = null;
	private Terrain terrain = null;
	@SuppressWarnings("unused")
	private Box2DDebugRenderer debugRenderer = null;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		pBatch = new PolygonSpriteBatch();
		
		// Debugrenderer wird in render() angewandt
		debugRenderer = new Box2DDebugRenderer();
		
		// Texturen laden
		TextureAtlas atlas;
		atlas = new TextureAtlas(Gdx.files.internal("KarpadorPack.pack"));

		// GameContext hält globale Objekte
		gameContext = new GameContext(new World(new Vector2(0, -9f), false), atlas);
		gameContext.getWorld().setContactListener(this);

		// Gelände erzeugen
		terrain = new Terrain(8000, gameContext);
		
		// Spieler (Fisch) erzeugen
		myFish = new Fish(gameContext, 4000, 800);
		
		
		
		//Camera erzeugen 
		camera = new OrthographicCamera();
		
		// Stage (Level) erzeugen und Fisch als Actor hinzufügen
		stage = new Stage(new ExtendViewport(1920, 1080,camera));
	    stage.addActor(myFish);
	    for (int i=0;i<20;i++) stage.addActor(new EvilCrab(gameContext, 4200+(550*i), 800, new Color(255f/255f,0f/255f,0f/255f,1f), 4f));
	    
	    // Input aktivieren
		Gdx.input.setInputProcessor(this);
	    
	    // Vsync
		Gdx.graphics.setVSync(true);
		Gdx.graphics.getGL20();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.55f, .81f, 0.95f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gameContext.addDelta(Gdx.graphics.getDeltaTime());
		// World step
		gameContext.getWorld().step(Gdx.graphics.getDeltaTime(), 6, 2);
		
		// Camera auf Fish setzen
		stage.getCamera().position.set(myFish.getX(), stage.getCamera().position.y, 0);
		stage.getCamera().update();
		
		pBatch.setProjectionMatrix(camera.combined);
		pBatch.begin();
		terrain.draw(pBatch);
		pBatch.end();
		
		batch.begin();
		stage.act(Gdx.graphics.getDeltaTime());
	    stage.draw();
	    // Box2d Debugger:
	    //Matrix4 cam = stage.getCamera().combined.cpy();
		//debugRenderer.render(gameContext.getWorld(), cam.scl(Config.PIXELSPERMETER));
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
		if (screenX < stage.getViewport().getScreenWidth()/2)
			myFish.move(-1);
		else
			myFish.move(1);
		return false;
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
		if ((contact.getFixtureA().getUserData().equals(myFish) &&
			contact.getFixtureB().getUserData().equals(terrain)) ||
			(contact.getFixtureB().getUserData().equals(myFish) &&
			contact.getFixtureA().getUserData().equals(terrain))) 
		{
			myFish.jump();
		}
		else if (
				 contact.getFixtureB().getUserData().equals(terrain) ||
				 contact.getFixtureA().getUserData().equals(terrain)) 
		{
			EvilCrab tmpCrab = null;
			if (contact.getFixtureA().getUserData().getClass().equals(EvilCrab.class))
				tmpCrab = (EvilCrab) contact.getFixtureA().getUserData();
			if (contact.getFixtureB().getUserData().getClass().equals(EvilCrab.class))
				tmpCrab = (EvilCrab) contact.getFixtureB().getUserData();
			if (tmpCrab != null) tmpCrab.setGrounded(true);
			
		}
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