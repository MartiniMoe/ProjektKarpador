package actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.martinimoe.projektkarpador.Config;
import com.martinimoe.projektkarpador.GameContext;

public abstract class Enemy extends Actor{
	
	protected Animation move = null;
	protected Body body = null;
	protected Color color = null;
	protected float speed = 0f;
	protected boolean grounded = false;
	protected GameContext gameContext = null;
	protected float angle = 0f;
	protected BitmapFont font;
	private float damage = 25f;

	public Enemy(GameContext gameContext, float x, float y, Color color, float speed){
		setX(x);
		setY(y);
		this.color = color;
		this.gameContext = gameContext;
		this.speed = speed;
		
		if (Config.DEBUG) {
	        font = new BitmapFont();
	        font.setScale(4);
	        font.setColor(Color.BLACK);
		}
	}

	protected void loadTexture(String regionName, float animationSpeed, PlayMode playMode) {
		// Textur laden
		Array<AtlasRegion> frames = gameContext.getAtlas().findRegions(regionName);
		move = new Animation(animationSpeed, frames);
		move.setPlayMode(playMode);
		// Größe aus Textur generieren
		setWidth(frames.get(0).getRegionWidth());
		setHeight(frames.get(0).getRegionHeight());
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);

		batch.setColor(color);
		batch.draw(move.getKeyFrame(gameContext.getTimeElapsed(), true),
					getX(),
					getY(),
					getWidth()/2,
					getHeight()/2,
					getWidth(),
					getHeight(),
					1f,
					1f,
					this.angle
					);
		
		if (Config.DEBUG) {
	        CharSequence str = MathUtils.round(getX()) + " : " + MathUtils.round(getY());
	        font.draw(batch, str, getX(), getY());
		}
		
		batch.setColor(1,1, 1, 1);
	}
	
	@Override
	public void act(float delta) {
		move();
	}
	
	public void move() {
		//body.setLinearVelocity(new Vector2(1, .1f));
		if (this.grounded) {
			body.applyForceToCenter(speed, 0, true);
			if (Math.abs(body.getLinearVelocity().x*Config.PIXELSPERMETER) > Math.abs(speed))
				body.applyForceToCenter(-body.getLinearVelocity().x*8f, 0, true);
			if (Math.abs(body.getLinearVelocity().x) < Math.abs(speed))
				body.applyForceToCenter(speed*2, speed*2, true);
		}
	}
	
	public boolean isGrounded() {
		return grounded;
	}

	public void setGrounded(boolean grounded) {
		this.grounded = grounded;
	}

	public GameContext getGameContext() {
		return gameContext;
	}

	public void setGameContext(GameContext gameContext) {
		this.gameContext = gameContext;
	}

	public float getDamage() {
		return damage;
	}

	public void setDamage(float damage) {
		this.damage = damage;
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}
	
	
}
