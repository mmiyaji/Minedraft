package game;

import java.awt.Color;

public abstract class Piece {
	public static final int WIDTH = 20;
	public static final int HEIGHT = 10;
	public static final int EMPTY = 0;
	public static final int WALL = 1;
	public static final int ENEMY = 2;
	public static final int ME = 3;
	public static final Color[] COLORS = 
	{	new Color(255,	255,	255,	  0),
		new Color(255,	  0,	  0,	100),
		new Color(  0,	255,	  0,	100),
		new Color(  0,	  0,	255,	100)};
}
