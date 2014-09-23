package actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.martinimoe.projektkarpador.Config;
import com.martinimoe.projektkarpador.GameContext;

public class EvilCrab extends Actor{
	private Animation walk = null;
	
	private Body body = null;
	private Color color = null;
	private float speed = 4f;
	private Vector2 moveForce = new Vector2();
	private boolean grounded = false;
	private float lastTakeOff = 0;
	private GameContext gameContext = null;
	private float angle = 0f;
	
	public boolean isGrounded() {
		return grounded;
	}

	public void setGrounded(boolean grounded) {
		this.grounded = grounded;
			
	}

	public EvilCrab(GameContext gameContext, float x, float y, Color color){
		setX(x);
		setY(y);
		this.color = color;
		this.gameContext = gameContext;
		// Textur laden
		Array<AtlasRegion> frames = gameContext.getAtlas().findRegions("Gegner/Krabbe");
		walk = new Animation(0.1f,frames);
		walk.setPlayMode(PlayMode.LOOP_PINGPONG);
		// Größe aus Textur generieren
		setWidth(frames.get(0).getRegionWidth());
		setHeight(frames.get(0).getRegionHeight());
		
		// Box2d driss
		BodyDef bdCrab = new BodyDef();
		bdCrab.bullet = true;
		bdCrab.type = BodyType.DynamicBody;
		bdCrab.position.set(((getX()-getWidth())/2)/Config.PIXELSPERMETER, (getY()+getHeight()/2)/Config.PIXELSPERMETER);
		body = gameContext.getWorld().createBody(bdCrab);
		PolygonShape pShape = new PolygonShape();
		pShape.setAsBox((getWidth()/4)/Config.PIXELSPERMETER, (getHeight()/4)/Config.PIXELSPERMETER);
		CircleShape cShape = new CircleShape();
		cShape.setRadius(getWidth()/4 / Config.PIXELSPERMETER);
		cShape.setPosition(new Vector2(0, (-getHeight()/4)/Config.PIXELSPERMETER));
		Fixture fc = body.createFixture(cShape,0);
		fc.setUserData(this);
		FixtureDef fDef = new FixtureDef();
		fDef.friction = 0f;
//		fDef.restitution = .01f;
		fDef.density = speed;
		fDef.shape = pShape;
		body.setFixedRotation(true);
		Fixture f = body.createFixture(fDef);
		f.setUserData(this);
//		body.setLinearDamping(100);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);

		setX(body.getPosition().x*Config.PIXELSPERMETER-getWidth()/2);
		setY(body.getPosition().y*Config.PIXELSPERMETER-getHeight()/1.5f);
		batch.setColor(color);
		batch.draw(walk.getKeyFrame(gameContext.getTimeElapsed(), true),
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
		
		batch.setColor(1,1, 1, 1);
	}
	
	@Override
	public void act(float delta) {
		move();
		System.out.println();
		
		this.angle = ( (-walk.getAnimationDuration()/2) + walk.getKeyFrameIndex(gameContext.getTimeElapsed()) )*5;
	}
	
	public void move() {
		
			//body.setLinearVelocity(new Vector2(1, .1f));
		if (this.grounded)
		{
		body.applyForceToCenter(speed, 0, true);
		if (body.getLinearVelocity().x*Config.PIXELSPERMETER > speed) body.applyForceToCenter(-body.getLinearVelocity().x*8f, 0, true);
		
		if (body.getLinearVelocity().x < speed){
			
			body.applyForceToCenter(speed*2, speed*2, true);
		}
		}
		
		
		
	}
	
	
}
