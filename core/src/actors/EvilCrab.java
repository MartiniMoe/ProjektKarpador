package actors;

import com.badlogic.gdx.graphics.Color;
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

public class EvilCrab extends Enemy{
	
	private float directionChangeTime = 1;
	public static final float CHANGE_DIRECTION_INTERVAL = 5;

	public EvilCrab(GameContext gameContext, float x, float y, Color color, float speed){
		super(gameContext, x, y, color, speed);
		
//		loadTexture("Gegner/Krabbe", 0.1f, PlayMode.LOOP_PINGPONG);
		loadTexture("Gegner/Krabbe", 1/speed, PlayMode.LOOP_PINGPONG);
		
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
		cShape.setPosition(new Vector2(x, (-getHeight()/4)/Config.PIXELSPERMETER));
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
		directionChangeTime = gameContext.getTimeElapsed() + CHANGE_DIRECTION_INTERVAL;
		
	}
	
	@Override
	public void act(float delta) {
		move();
		setX(body.getPosition().x*Config.PIXELSPERMETER-getWidth()/2);
		setY(body.getPosition().y*Config.PIXELSPERMETER-getHeight()/1.5f);
		
		if (gameContext.getTimeElapsed() > directionChangeTime)
		{
			directionChangeTime = gameContext.getTimeElapsed() + MathUtils.random(CHANGE_DIRECTION_INTERVAL, CHANGE_DIRECTION_INTERVAL*3);
			speed = speed * -1;
		}
		this.angle = ( (move.getKeyFrames().length) - move.getKeyFrameIndex(gameContext.getTimeElapsed())*1.5f )*2;
	}
}
