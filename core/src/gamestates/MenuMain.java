package gamestates;

import actors.Button;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.martinimoe.projektkarpador.Config;
import com.martinimoe.projektkarpador.GameContext;

public class MenuMain extends GameState implements ApplicationListener, ContactListener  {

	private Table table;
	// For debug drawing
	private ShapeRenderer shapeRenderer;
	private GameContext gameContext;
	
	public MenuMain(GameContext gc) {
		gameContext = gc;
	}
	
	@Override
	public void create() {
		setStage(new Stage());

//	    Skin skin = new Skin(Gdx.files.internal("uiskin/uiskin.json"));
//	    skin.addRegions(new TextureAtlas(Gdx.files.internal("uiskin/uiskin.atlas")));
//	    
//	    TextButtonStyle largeButton = skin.get("default", TextButtonStyle.class);
//	    
//	    final TextButton btnPlayGame = new TextButton("  Play  ", largeButton);
//	    btnPlayGame.addListener(new ChangeListener() {
//	    	public void changed (ChangeEvent event, Actor actor) {
//		    	gameContext.setGameState(gameContext.getGame());
//	    	}
//    	});
//	    
//	    final TextButton btnMusic = new TextButton("  Music: On  ", skin);
//	    btnMusic.addListener(new ChangeListener() {
//	    	public void changed(ChangeEvent event, Actor actor) {
//	    		if (!gameContext.toggleMusic()) btnMusic.setText("  Music: Off  ");
//	    		else btnMusic.setText("  Music: On  ");
//	    	}
//	    });

//	    final TextButton btnQuit = new TextButton("  Quit  ", skin);
//	    btnQuit.addListener(new ChangeListener() {
//	    	public void changed(ChangeEvent event, Actor actor) {
//	    		Gdx.app.exit();
//	    	}
//	    });
	    
	    final Button tbPlay = new Button(gameContext,"Button/button_play.fw");
	    tbPlay.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				gameContext.setGameState(gameContext.getGame());
			}
		});
	    tbPlay.setVisible(true);
	    tbPlay.setScale(1f);
	    tbPlay.setPosition((getStage().getWidth()/2) - tbPlay.getWidth()/2, (getStage().getHeight()/2) - tbPlay.getHeight()/2);
	    getStage().addActor(tbPlay);
	    
	    final Button tbToggleMusic = new Button(gameContext, "Button/button_pause.fw");
	    tbToggleMusic.addListener(new ChangeListener() {
	    	@Override
	    	public void changed(ChangeEvent event, Actor actor) {
	    		gameContext.setMuted(!gameContext.isMuted());
	    		if (Config.DEBUG)
	    			System.out.println("Muted: " + gameContext.isMuted());
	    	}
	    });
	    tbToggleMusic.setVisible(true);
	    tbToggleMusic.setScale(1f);
	    tbToggleMusic.setPosition((getStage().getWidth()/2) - tbToggleMusic.getWidth()/2, (getStage().getHeight()/2) - tbPlay.getHeight() * 2f);
	    getStage().addActor(tbToggleMusic);
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	    getStage().act(Gdx.graphics.getDeltaTime());
	    getStage().draw();
	}
	
	@Override
	public void resize(int width, int height) {
		getStage().getViewport().update(width, height, true);
	}

	@Override
	public void beginContact(Contact contact) {
		
	}

	@Override
	public void endContact(Contact contact) {
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		
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
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
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
	public void dispose() {
		getStage().dispose();
	    shapeRenderer.dispose();
	}
}
