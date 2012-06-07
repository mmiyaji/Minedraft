/**
 * @author mmiyaji
 * 
 * Created by Masahiro MIYAJI on 2012-06-06.
 * Copyright (c) 2012 ISDL. All rights reserved.
 */

import game.Board;
import game.Piece;
import gui.Window;

import java.util.Random;

public class Main{

	public static Board board;
	static Thread mainThread;

	public static void main(String[] args) {
		System.out.print("Program start");
		mainThread = new Thread();
		board = new Board();
		Window window = new Window(true, board);
		Random rand = new Random();
		int x, y;
		while(true){
			window.repaint();
			x = (int)(rand.nextDouble()*Board.WIDTH);
			y = (int)(rand.nextDouble()*Board.HEIGHT);
			board.setPoint(x, y, Piece.ENEMY);
			window.setBoard(board);
			System.out.println(x+":"+y);
			try{
				Thread.sleep(200);
			}catch(InterruptedException e){}
		}
	}
}
