package com.starfish;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.MathUtils;
import java.util.ArrayList;

public class TurtleLevel extends BaseScreen
{
    private BaseActor ocean;
    private ArrayList<BaseActor> rockList;
    private ArrayList<BaseActor> starfishList;
    private PhysicsActor turtle;
    private int mapWidth = 800;
    private int mapHeight = 600;

    public TurtleLevel(Game g) {
        super(g);
    }

    public void create() {
        ocean = new BaseActor();
        ocean.setTexture(new Texture(Gdx.files.internal("water.jpg")));
        ocean.setPosition(0, 0);
        mainStage.addActor(ocean);

        BaseActor overlay = ocean.clone();
        overlay.setPosition(-50, -50);
        overlay.setColor(1, 1, 1, 0.25f);
        uiStage.addActor(overlay);
        overlay.toBack(); // necessary so it doesn't overlay and block access to buttons in UI

        BaseActor rock = new BaseActor();
        rock.setTexture(new Texture(Gdx.files.internal("rock.png")));
        rock.setEllipseBoundary();

        rockList = new ArrayList<BaseActor>();
        int[] rockCoords = {200, 0, 200, 100, 250, 200, 360, 200, 470, 200};
        for (int i = 0; i < 5; i++) {
            BaseActor r = rock.clone();
            r.setPosition(rockCoords[2 * i], rockCoords[2 * i + 1]);
            mainStage.addActor(r);
            rockList.add(r);
        }

        BaseActor starfish = new BaseActor();
        starfish.setTexture(new Texture(Gdx.files.internal("starfish.png")));
        starfish.setEllipseBoundary();

        starfishList = new ArrayList<BaseActor>();
        int[] starfishCoords = {400, 100, 100, 400, 650, 400};
        for (int i = 0; i < 3; i++) {
            BaseActor s = starfish.clone();
            s.setPosition(starfishCoords[2 * i], starfishCoords[2 * i + 1]);
            mainStage.addActor(s);
            starfishList.add(s);
        }

        turtle = new PhysicsActor();
        TextureRegion[] frames = new TextureRegion[6];
        for (int n = 1; n <= 6; n++) {
            String fileName = "turtle-" + n + ".png";
            Texture tex = new Texture(Gdx.files.internal(fileName));
            tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
            frames[n - 1] = new TextureRegion(tex);
        }
        Array<TextureRegion> framesArray = new Array<TextureRegion>(frames);

        Animation anim = new Animation(0.1f, framesArray, Animation.PlayMode.LOOP);
        turtle.storeAnimation("swim", anim);

        Texture frame1 = new Texture(Gdx.files.internal("turtle-1.png"));
        turtle.storeAnimation("rest", frame1);

        turtle.setOrigin(turtle.getWidth() / 2, turtle.getHeight() / 2);
        turtle.setPosition(20, 20);
        turtle.setRotation(90);
        turtle.setEllipseBoundary();
        turtle.setMaxSpeed(100);
        turtle.setDeceleration(200);
        mainStage.addActor(turtle);
    }

    public void update(float dt)
    {
        // process input
        turtle.setAccelerationXY(0,0);

        if (Gdx.input.isKeyPressed(Keys.LEFT))
            turtle.rotateBy(90 * dt);
        if (Gdx.input.isKeyPressed(Keys.RIGHT))
            turtle.rotateBy(-90 * dt);
        if (Gdx.input.isKeyPressed(Keys.UP))
            turtle.accelerateForward(100);

        // set correct animation
        if ( turtle.getSpeed() > 1 && turtle.getAnimationName().equals("rest") )
            turtle.setActiveAnimation("swim");
        if ( turtle.getSpeed() < 1 && turtle.getAnimationName().equals("swim") )
            turtle.setActiveAnimation("rest");

        // bound turtle to the screen
        turtle.setX( MathUtils.clamp( turtle.getX(), 0,  mapWidth - turtle.getWidth() ));
        turtle.setY( MathUtils.clamp( turtle.getY(), 0,  mapHeight - turtle.getHeight() ));

        for (BaseActor r : rockList)
        {
            turtle.overlaps(r, true);
        }

        ArrayList<BaseActor> removeList = new ArrayList<BaseActor>();
        for (BaseActor s : starfishList)
        {
            if ( turtle.overlaps(s, false) )
                removeList.add(s);
        }

        for (BaseActor b : removeList)
        {
            b.remove();             // remove from stage
            starfishList.remove(b); // remove from list used by update
        }

    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
