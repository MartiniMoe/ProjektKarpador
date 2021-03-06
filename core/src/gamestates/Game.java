package gamestates;

import hud.HealthBar;
import actors.Button;
import actors.Enemy;
import actors.Fish;
import actors.House;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
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
    /*
    private float[] cloudPos = null;
    private float[] cloudRatio = null;
    private float[] resolution = null;
    */
    
	private SpriteBatch batch;
	private PolygonSpriteBatch pBatch;
	

	private GameContext gameContext;
	
	private Label lbGameOver = null;
	private Label lbVictory = null;
	private Button tbPlayAgain = null;
	
	private House house = null;
	
	private Label lbFPS = null;
	
	private boolean musicLoopStarted = false;
	
	
	
	
	public Game(GameContext gameContext) {
		this.gameContext = gameContext;
	}
	
	@Override
	public void create() {
		super.create();
		
		batch = new SpriteBatch();
		
		pBatch = new PolygonSpriteBatch();
		
		/*
		cloudPos = new float[4];
		cloudRatio = new float[4];
		resolution = new float[2];
		*/
		
		// Debugrenderer wird in render() angewandt
		if (Config.DEBUG) {
			debugRenderer = new Box2DDebugRenderer();
		}
		
	
		//Camera erzeugen 
		camera = new OrthographicCamera();
		
	    
		// Spieler (Fisch) erzeugen
		gameContext.setFish(new Fish(gameContext, 600, 800));
		
		
		// Stage (Level) erzeugen und Fisch als Actor hinzufügen
		setStage ( new Stage(new ExtendViewport(1920, 1080,camera)) );
		stage = getStage();
		
		hudCamera = new OrthographicCamera();
		
		hudStage = new Stage(new ExtendViewport(1920, 1080,hudCamera));
		healthBar = new HealthBar(gameContext, hudStage.getWidth(), hudStage.getHeight()-64);
		
		hudStage.addActor(healthBar);
		
		gameContext.setSky(new Stage(new ExtendViewport(1920, 1080,new OrthographicCamera())));
		
	    vertexShader = Gdx.files.internal("shader/vertex.glsl").readString();
	    fragmentShader = Gdx.files.internal("shader/wasser.glsl").readString();
	    waterShader = new ShaderProgram(vertexShader,fragmentShader);
	    
	    //fragmentShader = Gdx.files.internal("shader/sonnenstrahlen.glsl").readString();
	    //cloudShader = new ShaderProgram(vertexShader, fragmentShader);
	    
	    ShaderProgram.pedantic = false;
	    
	    waterShader.begin();
	    waterShader.setUniformf("time", gameContext.getTimeElapsed());
	    waterShader.end();

	    gameContext.setStage(stage);
	    stage.addActor(gameContext.getFish());
	    
	    
	    //resolution[0] = 1920;
	    //resolution[1] = 1080;
	    wasser = new Texture("terrain.png");

		// Gelände erzeugen
		terrain = new Terrain(16000, gameContext, 3);
		
		gameContext.getWorld().setContactListener(this);
		//Gdx.input.setInputProcessor(this);
		
	    Skin skin = new Skin(Gdx.files.internal("uiskin/uiskin.json"));
	    skin.addRegions(new TextureAtlas(Gdx.files.internal("uiskin/uiskin.atlas")));
	    
	    lbGameOver = new Label("Game Over", skin);
	    lbGameOver.setFontScale(4f);
	    lbGameOver.setPosition(hudStage.getWidth()/2-lbGameOver.getWidth()/2, hudStage.getHeight()/2);
	    lbGameOver.setVisible(false);
	    hudStage.addActor(lbGameOver);
	    
	    lbVictory = new Label("Victory!", skin);
	    lbVictory.setFontScale(3f);
	    lbVictory.setPosition(hudStage.getWidth()/2, hudStage.getHeight()/2);
	    lbVictory.setVisible(false);
	    hudStage.addActor(lbVictory);
	    
	    if (Config.DEBUG) {
	    	lbFPS = new Label("FPS: ", skin);
		    lbFPS.setPosition(hudStage.getWidth()-100, hudStage.getHeight()-32);
		    lbFPS.setVisible(Config.DEBUG);
		    hudStage.addActor(lbFPS);
	    }
	    
	    tbPlayAgain = new Button(gameContext,"Button/button_restart");
	    tbPlayAgain.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				reset();
			}
		});
	    tbPlayAgain.setScale(1f);
	    tbPlayAgain.setPosition(hudStage.getWidth()/2-tbPlayAgain.getWidth()/2, hudStage.getHeight()/3);
	    tbPlayAgain.setVisible(false);
	    
	    hudStage.addActor(tbPlayAgain);
	    
	    house = new House(gameContext, 0, 800);
	    
	    house.setZIndex(999);
	    stage.addActor(house);
	    
	    gameContext.getFish().getBody().applyForceToCenter(750, 200, true);
	    gameContext.getCat().getBody().applyForceToCenter(5000, 2000, true);
	}
	
	public void reset()
	{
		stage.clear();
		gameContext.setTimeElapsed(0);
		gameContext.setWorld(new World(gameContext.getWorld().getGravity(), true));
		gameContext.setFish(new Fish(gameContext, 200, 800));
		gameContext.getWorld().setContactListener(this);
		stage.addActor(gameContext.getFish());
		
		house = new House(gameContext, 0, 800);
//	    house.setScale(5);
	    
	    stage.addActor(house);
		
		terrain = new Terrain(16000, gameContext, 3);
		gameContext.setGameState(this);
		lbGameOver.setVisible(false);
		lbVictory.setVisible(false);
		tbPlayAgain.setVisible(false);
		
		gameContext.stopMusic();
		musicLoopStarted = false;
		introStarted = true;
		
		gameContext.getFish().getBody().applyForceToCenter(750, 200, true);
	    gameContext.getCat().getBody().applyForceToCenter(5000, 3000, true);
		
	}

	private boolean introStarted = true;
	@Override
	public void render() {
		super.render();
		
		if (gameContext.getFish().getX() > 1500) house.setActive(true);
		
		if (!gameContext.isMuted() && gameContext.getGameState().equals(this) && !introStarted) {
			gameContext.playIntro();
			introStarted = true;
		}
		
		if (!gameContext.isMuted() && introStarted && !gameContext.getIntro().isPlaying() && !musicLoopStarted)
			gameContext.startMusic();
		
		if (Config.DEBUG) lbFPS.setText("FPS: " + 1 / Gdx.graphics.getDeltaTime());
		
		// Update Shaders
		waterShader.begin();
	    waterShader.setUniformf("time", gameContext.getTimeElapsed());
	    waterShader.end();
	    
	    /*
	    cloudShader.begin();
	    cloudShader.setUniformf("time", gameContext.getTimeElapsed());
	    cloudPos[0] = -.20f;
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
	    cloud.setPosition(stage.getWidth()/2-cloudPos[2]*stage.getWidth(), stage.getHeight()/2+cloudPos[3]*stage.getHeight());
	    */
	    
		// Camera auf Fish setzen
		stage.getCamera().position.set(MathUtils.clamp(gameContext.getFish().getX(), 800, 999999999), stage.getCamera().position.y, 0);
		
		stage.getCamera().update();
		
		pBatch.setProjectionMatrix(camera.combined);
		pBatch.begin();
			terrain.draw(pBatch);
		pBatch.end();
		
		gameContext.getSky().getCamera().position.x = stage.getCamera().position.x / 2;
		
		batch.begin();
			gameContext.getSky().act();
			gameContext.getSky().draw();
			
			stage.act(Gdx.graphics.getDeltaTime());
		    stage.draw();
		    
		    
		    batch.setShader(waterShader);
		    batch.draw(wasser,0,0,stage.getWidth(),128);
		batch.end();
		    
		/*
	    lightbatch.begin();
		//batch.setShader(null);
		lightbatch.setBlendFunction(GL20.GL_DST_COLOR, GL20.GL_ZERO);
		lightbatch.setShader(cloudShader);
		lightbatch.draw(wasser,0,0,1920,1080);
  
	    lightbatch.end();*/
		   
	    //Box2d Debugger:
	    if (Config.DEBUG) {
		    Matrix4 cam = stage.getCamera().combined.cpy();
			debugRenderer.render(gameContext.getWorld(), cam.scl(Config.PIXELSPERMETER));
	    }
	
		
		batch.setProjectionMatrix(hudCamera.combined);
		
		batch.begin();
			hudStage.act(Gdx.graphics.getDeltaTime());
			if (gameContext.getFish().getY() < - 100)
			{
				lbVictory.setVisible(true);
				tbPlayAgain.setVisible(true);
				Gdx.input.setInputProcessor(hudStage);
			}
	    	if (gameContext.getFish().getHealth() <= 0) 
	    		{
	    			tbPlayAgain.setVisible(true);
	    			lbGameOver.setVisible(true);
	    			Gdx.input.setInputProcessor(hudStage);
	    		}
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
//		super.resize(width, height);
		if (stage != null) stage.getViewport().update(width, height, true);
		hudStage.getViewport().update(width, height, true);
		tbPlayAgain.setPosition(hudStage.getWidth()/2, hudStage.getHeight()/2-128);
//		gameContext.getMenuMain().resize(width, height);
		healthBar.setPosition(hudStage.getWidth(), hudStage.getHeight()-64);
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
