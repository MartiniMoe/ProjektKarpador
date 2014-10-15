package gamestates;

import hud.HealthBar;
import actors.Enemy;
import actors.Fish;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.martinimoe.projektkarpador.Config;
import com.martinimoe.projektkarpador.GameContext;
import com.martinimoe.projektkarpador.Terrain;

public class Game extends GameState implements ApplicationListener, ContactListener  {
	
	private Stage stage, hudStage;
	private Camera camera, hudCamera;
	
	private Terrain terrain = null;
	private Box2DDebugRenderer debugRenderer = null;

	String vertexShader;
    String fragmentShader;
    ShaderProgram waterShader, cloudShader;
    
    private Texture wasser = null;
    
    private HealthBar healthBar = null;
    
    private float[] cloudPos = null;
    private float[] cloudRatio = null;
    private float[] resolution = null;
    
	private SpriteBatch batch, lightbatch;
	private PolygonSpriteBatch pBatch;

	private GameContext gameContext;
	
	public Game(GameContext gameContext) {
		this.gameContext = gameContext;
	}
	
	@Override
	public void create() {
		super.create();
		
		batch = new SpriteBatch();
		lightbatch = new SpriteBatch(6);
		pBatch = new PolygonSpriteBatch();
		
		cloudPos = new float[4];
		cloudRatio = new float[4];
		resolution = new float[2];
		
		// Debugrenderer wird in render() angewandt
		if (Config.DEBUG) {
			debugRenderer = new Box2DDebugRenderer();
		}
		
	
		//Camera erzeugen 
		camera = new OrthographicCamera();
		
	    
		// Spieler (Fisch) erzeugen
		gameContext.setFish(new Fish(gameContext, 2500, 900));
		
		
		// Stage (Level) erzeugen und Fisch als Actor hinzufügen
		setStage ( new Stage(new StretchViewport(1920, 1080,camera)) );
		stage = getStage();
		
		hudCamera = new OrthographicCamera();
		
		hudStage = new Stage(new StretchViewport(1920, 1080,hudCamera));
		healthBar = new HealthBar(gameContext, hudStage.getWidth(), hudStage.getHeight()-64);
		
		hudStage.addActor(healthBar);
		
	    vertexShader = Gdx.files.internal("shader/vertex.glsl").readString();
	    fragmentShader = Gdx.files.internal("shader/wasser.glsl").readString();
	    waterShader = new ShaderProgram(vertexShader,fragmentShader);
	    
	    fragmentShader = Gdx.files.internal("shader/sonnenstrahlen.glsl").readString();
	    cloudShader = new ShaderProgram(vertexShader, fragmentShader);
	    
	    ShaderProgram.pedantic = false;
	    
	    waterShader.begin();
	    waterShader.setUniformf("time", gameContext.getTimeElapsed());
	    waterShader.end();

	    gameContext.setStage(stage);
	    stage.addActor(gameContext.getFish());
	    
	    
	    resolution[0] = 1920;
	    resolution[1] = 1080;
	    wasser = new Texture("terrain.png");

		// Gelände erzeugen
		terrain = new Terrain(16000, gameContext, 5);
		
		gameContext.getWorld().setContactListener(this);
		//Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render() {
		super.render();
		

		// Update Shaders
		waterShader.begin();
	    waterShader.setUniformf("time", gameContext.getTimeElapsed());
	    waterShader.end();
	    
	    cloudShader.begin();
	    cloudShader.setUniformf("time", gameContext.getTimeElapsed());
	    cloudPos[0] = .0f;
	    cloudPos[1] = .25f;
	    cloudPos[2] = .5f;
	    cloudPos[3] = .25f;
	    cloudRatio[0] = 10f;
	    cloudRatio[1] = 20f;
	    cloudRatio[2] = 10f;
	    cloudRatio[3] = 20f;
	    cloudShader.setUniform2fv("cloud_pos[0]", cloudPos,0,2);
	    cloudShader.setUniform2fv("cloud_pos[1]", cloudPos,2,2);
	    cloudShader.setUniform2fv("cloud_ratio[0]", cloudRatio,0,2);
	    cloudShader.setUniform2fv("cloud_ratio[1]", cloudRatio,2,2);
	    cloudShader.setUniform2fv("resolution", resolution, 0, 2);
	    cloudShader.end();
		
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

		    batch.setShader(waterShader);
		    batch.draw(wasser,0,0,stage.getWidth(),512);
	  batch.end();
	  lightbatch.begin();
		    //batch.setShader(null);
		  lightbatch.setBlendFunction(GL20.GL_DST_COLOR, GL20.GL_ZERO);
		  lightbatch.setShader(cloudShader);
		  lightbatch.draw(wasser,0,0,1920,1080);
		    
		    
		    // Box2d Debugger:
		    if (Config.DEBUG) {
			    Matrix4 cam = stage.getCamera().combined.cpy();
				debugRenderer.render(gameContext.getWorld(), cam.scl(Config.PIXELSPERMETER));
		    }
		
		    lightbatch.end();
		
		
		batch.setProjectionMatrix(hudCamera.combined);
		
		batch.begin();
			hudStage.act(Gdx.graphics.getDeltaTime());
	    	hudStage.draw();
		batch.end();
	}
	
	@Override
	public void update() {
		super.update();
		Gdx.input.setInputProcessor(this);
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
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
	public void resize(int width, int height) {
		super.resize(width, height);
		if (stage != null) stage.getViewport().update(width, height, true);
	}
	@Override
	public void dispose() {
		super.dispose();
		 stage.dispose();
	    waterShader.dispose();
	    pBatch.dispose();
	    batch.dispose();
	}
}
