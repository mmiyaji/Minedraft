package game;

import java.awt.Color;

public abstract class Piece {
    public static final int EMPTY = -1;
    public static final int WALL = -2;
    public static final int ME = 1;
    public static final int ENEMY = 2;
    public static final float DYNAMICS = 0.05f;
	
    public static final Color[] COLORS =
    {	new Color(255,	255,	255,	  0),	// EMPTY
	new Color(  0,	  0,	  0,	255),	// WALL
	new Color(  0,	255,	  0,	100),	// ENEMY
	new Color(  0,	  0,	255,	100),	// ME
	new Color(  255,	  0,	0,	150),	// Angle
	new Color(  255,	  0,	0,	150)	// Arrow
    };
    public static Color COLORS(int id){
	if(id < 0){
	    return COLORS[-id-1];
	}
	else{
	    return COLORS[id % 2 + 2];
	}
    }
}
