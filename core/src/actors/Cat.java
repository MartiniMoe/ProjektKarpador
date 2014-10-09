package actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.martinimoe.projektkarpador.Config;
import com.martinimoe.projektkarpador.GameContext;

public class Cat extends Enemy{

	public Cat(GameContext gameContext, float x, float y, Color color, float speed) {
		super(gameContext, x, y, color, speed);

		loadTexture("Gegner/Katze", 1/speed, PlayMode.LOOP_PINGPONG);
		
		this.grounded = true;
		
		// Box2d driss
		BodyDef bdCat = new BodyDef();
		bdCat.bullet = true;
		bdCat.type = BodyType.DynamicBody;
		bdCat.position.set(((getX()-getWidth())/2)/Config.PIXELSPERMETER, (getY()+getHeight()/2)/Config.PIXELSPERMETER);
		body = gameContext.getWorld().createBody(bdCat);
		PolygonShape pShape = new PolygonShape();
		pShape.setAsBox((getWidth()/4)/Config.PIXELSPERMETER, (getHeight()/4)/Config.PIXELSPERMETER);
//		CircleShape cShape = new CircleShape();
//		cShape.setRadius(getWidth()/2 / Config.PIXELSPERMETER);
//		cShape.setPosition(new Vector2(x, (-getHeight()/4)/Config.PIXELSPERMETER));
//		Fixture fc = body.createFixture(cShape,0);
//		fc.setUserData(this);
		FixtureDef fDef = new FixtureDef();
		fDef.friction = 0f;
//				fDef.restitution = .01f;
		fDef.density = speed/2;
		fDef.shape = pShape;
		body.setFixedRotation(true);
		Fixture f = body.createFixture(fDef);
		f.setUserData(this);
	}
	
	private float jumpDelay = 0f;
	@Override
	public void act(float delta) {
		super.act(delta);
		
		setX(body.getPosition().x*Config.PIXELSPERMETER-getWidth()/2);
		setY(body.getPosition().y*Config.PIXELSPERMETER-getHeight()/2);
		
		if (this.getX() > gameContext.getFish().getX() && speed > 0) {
			speed = speed * -1;
		}
		if (this.getX() < gameContext.getFish().getX() && speed < 0){
			speed = speed * -1;
		}
		
		if (jumpDelay > 5f) {
			body.applyForceToCenter(speed * 200, 8000, true);
			jumpDelay = 0;
		}
		jumpDelay += delta;
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
		
		batch.setColor(1,1, 1, 1);
	}
}
