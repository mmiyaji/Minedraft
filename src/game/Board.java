package game;

import java.util.Random;

public class Board extends Piece{

	int[][] board;
	boolean attack;
	int count;
	public Board(){
		board = new int[WIDTH][HEIGHT];
		Random rand = new Random();
		for(int i=0; i<WIDTH; i++){
			board[i][(int)(rand.nextDouble()*HEIGHT)] = WALL;
		}
	}
	public int getPiece(int x, int y){
		return this.board[x][y];
	}
	public void showBoard(){
		for(int i=0; i< Board.WIDTH; i++){
			for(int j=0; j<Board.HEIGHT; j++){
				System.out.print(this.board[i][j]);
			}
			System.out.println("");
		}
	}
}
