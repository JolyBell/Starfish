package com.starfish;

import com.badlogic.gdx.Game;
public class TurtleGame extends Game
{
    public void create()
    {
        TurtleLevel tl = new TurtleLevel(this);
        setScreen( tl );
    }
}


