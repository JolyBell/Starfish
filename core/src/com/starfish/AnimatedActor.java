package com.starfish;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import java.util.HashMap;

public class AnimatedActor extends BaseActor
{
    private float elapsedTime;
    private Animation activeAnim;
    private String activeName;
    private HashMap<String,Animation> animationStorage;

    public AnimatedActor()
    {
        super();
        elapsedTime = 0;
        animationStorage = new HashMap<String,Animation>();
    }

    public void storeAnimation(String name, Animation anim)
    {
        animationStorage.put(name, anim);
        if (activeName == null)
            setActiveAnimation(name);
    }

    public void storeAnimation(String name, Texture tex)
    {
        TextureRegion reg = new TextureRegion(tex);
        TextureRegion[] frames = { reg };
        Animation anim = new Animation(1.0f, frames);
        storeAnimation(name, anim);
    }

    public void setActiveAnimation(String name)
    {
        if ( !animationStorage.containsKey(name) )
        {
            System.out.println("No animation: " + name);
            return;
        }

        activeName = name;
        activeAnim = animationStorage.get(name);
        elapsedTime = 0;

        TextureRegion tex = (TextureRegion) activeAnim.getKeyFrame(0);
        setWidth( tex.getRegionWidth() );
        setHeight( tex.getRegionHeight() );
    }

    public String getAnimationName()
    {
        return activeName;
    }

    public void act(float dt)
    {
        super.act( dt );
        elapsedTime += dt;
    }

    public void draw(Batch batch, float parentAlpha)
    {
        region.setRegion((TextureRegion) activeAnim.getKeyFrame(elapsedTime));
        super.draw(batch, parentAlpha);
    }
}
