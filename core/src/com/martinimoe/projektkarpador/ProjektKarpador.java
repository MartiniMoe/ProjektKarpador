package com.martinimoe.projektkarpador;

import hud.HealthBar;
import actors.Enemy;
import actors.EvilCrab;
import actors.Fish;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
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
	private Stage stage, hudStage;
	private Camera camera, hudCamera;
	private GameContext gameContext = null;
	private Terrain terrain = null;
	private Box2DDebugRenderer debugRenderer = null;
	
	private Music music = null;
	
    String vertexShader;
    String fragmentShader;
    ShaderProgram shaderProgram;
    
    private Texture wasser = null;
    
    private HealthBar healthBar = null;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		pBatch = new PolygonSpriteBatch();
		

		
		// Debugrenderer wird in render() angewandt
		if (Config.DEBUG) {
			debugRenderer = new Box2DDebugRenderer();
		}
		
		// Texturen laden
		TextureAtlas atlas;
		atlas = new TextureAtlas(Gdx.files.internal("Karpador.pack"));
		
		//Musik!
		music = Gdx.audio.newMusic(Gdx.files.internal("Karpador2.wav"));
		music.setLooping(true);
		music.play();

		// GameContext hält globale Objekte
		gameContext = new GameContext(new World(new Vector2(0, -9f), false), atlas);
		gameContext.getWorld().setContactListener(this);
		
		//Camera erzeugen 
		camera = new OrthographicCamera();
		
	    
		// Spieler (Fisch) erzeugen
		gameContext.setFish(new Fish(gameContext, 2000, 700));
		
		
		// Stage (Level) erzeugen und Fisch als Actor hinzufügen
		stage = new Stage(new ExtendViewport(1920, 1080,camera));
		hudCamera = new OrthographicCamera();
		
		hudStage = new Stage(new ExtendViewport(1920, 1080,hudCamera));
		healthBar = new HealthBar(gameContext, hudStage.getWidth(), hudStage.getHeight()-64);
		
		hudStage.addActor(healthBar);
		
	    vertexShader = Gdx.files.internal("shader/vertex.glsl").readString();
	    fragmentShader = Gdx.files.internal("shader/wasser.glsl").readString();
	    
	    shaderProgram = new ShaderProgram(vertexShader,fragmentShader);
	    ShaderProgram.pedantic = false;
	    
	    shaderProgram.begin();
	    shaderProgram.setUniformf("time", gameContext.getTimeElapsed());
	    shaderProgram.end();
		
	    gameContext.setStage(stage);
	    stage.addActor(gameContext.getFish());
	    
	    
	    wasser = new Texture("terrain.png");

		// Gelände erzeugen
		terrain = new Terrain(16000, gameContext, 2);
	    
	    // Input aktivieren
		Gdx.input.setInputProcessor(this);
	    
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
		
		// Update Shaders
		shaderProgram.begin();
	    shaderProgram.setUniformf("time", gameContext.getTimeElapsed());
	    shaderProgram.end();
		
		// Camera auf Fish setzen
		stage.getCamera().position.set(gameContext.getFish().getX(), stage.getCamera().position.y, 0);
		stage.getCamera().update();
		
		pBatch.setProjectionMatrix(camera.combined);
		pBatch.begin();
			terrain.draw(pBatch);
		pBatch.end();
		
		batch.begin();

			stage.act(Gdx.graphics.getDeltaTime());
	
			
		    stage.draw();
		    
		    
		    batch.setShader(shaderProgram);
		    batch.draw(wasser,0,0,stage.getWidth(),512);
		    
		   
		    // Box2d Debugger:
		    if (Config.DEBUG) {
			    Matrix4 cam = stage.getCamera().combined.cpy();
				debugRenderer.render(gameContext.getWorld(), cam.scl(Config.PIXELSPERMETER));
		    }
		
		batch.end();
		
		
		batch.setProjectionMatrix(hudCamera.combined);
		
		batch.begin();
			hudStage.act(Gdx.graphics.getDeltaTime());
	    	hudStage.draw();
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
	    music.dispose();
	    shaderProgram.dispose();
	    pBatch.dispose();
	    batch.dispose();
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (screenX < stage.getViewport().getScreenWidth()/2)
			gameContext.getFish().move(-1);
		else
			gameContext.getFish().move(1);
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
		if ((contact.getFixtureA().getUserData().equals(gameContext.getFish()) &&
			contact.getFixtureB().getUserData().equals(terrain)) ||
			(contact.getFixtureB().getUserData().equals(gameContext.getFish()) &&
			contact.getFixtureA().getUserData().equals(terrain))) 
		{
			gameContext.getFish().jump();
		}
		else if (
				 contact.getFixtureB().getUserData().equals(terrain) ||
				 contact.getFixtureA().getUserData().equals(terrain)) 
		{
			Enemy tmpEnemy = null;
			if (contact.getFixtureA().getUserData() instanceof Enemy)
				tmpEnemy = (Enemy) contact.getFixtureA().getUserData();
			if (contact.getFixtureB().getUserData() instanceof Enemy)
				tmpEnemy = (Enemy) contact.getFixtureB().getUserData();
			if (tmpEnemy != null) tmpEnemy.setGrounded(true);
			
		}
		if ((contact.getFixtureA().getUserData().equals(gameContext.getFish()) &&
				contact.getFixtureB().getUserData() instanceof Enemy) ||
				(contact.getFixtureB().getUserData().equals(gameContext.getFish()) &&
					contact.getFixtureA().getUserData() instanceof Enemy)) 
			{
				if (contact.getFixtureA().getUserData().equals(gameContext.getFish())) gameContext.getFish().contact(contact.getFixtureB().getUserData());
					else gameContext.getFish().contact(contact.getFixtureA().getUserData());
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