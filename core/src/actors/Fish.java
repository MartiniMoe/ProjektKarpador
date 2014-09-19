package actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.martinimoe.projektkarpador.Config;
import com.martinimoe.projektkarpador.GameContext;

public class Fish extends Actor {
	private Animation flounder = null;
	private float elapsed = 0;
	private Body body = null;
	private boolean doJump = false;
	private float jumpDelay = 0;
	
	public Fish(GameContext gameContext, float x, float y){
		setX(x);
		setY(y);
		
		// Textur laden
		Array<AtlasRegion> frames = gameContext.getAtlas().findRegions("Fisch/Fisch");
		flounder = new Animation(0.05f,frames);
		// Fischgröße aus Textur generieren
		setWidth(frames.get(0).getRegionWidth());
		setHeight(frames.get(0).getRegionHeight());
		
		// Box2d driss
		BodyDef bd = new BodyDef();
		bd.bullet = true;
		bd.type = BodyType.DynamicBody;
		bd.position.set(((getX()-getWidth())/2)/Config.PIXELSPERMETER, (getY()+getHeight()/2)/Config.PIXELSPERMETER);
		body = gameContext.getWorld().createBody(bd);
		PolygonShape pShape = new PolygonShape();
		pShape.setAsBox((getWidth()/3)/Config.PIXELSPERMETER, (getHeight()/4)/Config.PIXELSPERMETER);
//		CircleShape circle = new CircleShape();
//		circle.setRadius((getHeight()/2)/Config.PIXELSPERMETER);
		
		FixtureDef fDef = new FixtureDef();
		fDef.friction = .75f;
//		fDef.restitution = .5f;
		fDef.density = 2;
		fDef.shape = pShape;
		
		Fixture f = body.createFixture(fDef);
		f.setUserData(this);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);

		setX(body.getPosition().x*Config.PIXELSPERMETER-getWidth()/2);
		setY(body.getPosition().y*Config.PIXELSPERMETER-getHeight()/1.65f);
		batch.draw(flounder.getKeyFrame(elapsed, true),
					getX(),
					getY(),
					getWidth()/2,
					getHeight()/2,
					getWidth(),
					getHeight(),
					1f,
					1f,
					MathUtils.radDeg*body.getAngle()
					);
		elapsed += Gdx.graphics.getDeltaTime();
	}
	
	public void jump() {
		doJump = true;
		jumpDelay = 0;
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		if (doJump && jumpDelay > .2f) {
			body.applyForceToCenter(0, 400, true);
			doJump = false;
		}
		jumpDelay += delta;
	}

	public void move(int i) {
		System.out.println(body.getLinearVelocity());
		if (i == 1 && body.getLinearVelocity().x < 10 ||
			i == -1 && body.getLinearVelocity().x > -10)
		{
			body.applyForceToCenter(i*100, 0, true);
			body.applyAngularImpulse(-i*.2f, true);
		}
	}
	

}
