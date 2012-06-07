package game;

import java.util.Random;

public class Board extends Piece{

	private int[][] board;
	private boolean attack;
	private int count;
	public Board(){
		initBoard(10);
	}
	public void initBoard(long seed){
		board = new int[WIDTH][HEIGHT];
		Random rand = new Random(seed);
		for(int i=0; i<WIDTH; i++){
			board[i][0] = WALL;
			board[i][HEIGHT-1] = WALL;
		}
		for(int j=1; j<HEIGHT-1; j++){
			board[0][j] = WALL;
			board[WIDTH-1][j] = WALL;
		}
		for(int i=1; i<WIDTH-1; i++){
			board[i][(int)(rand.nextDouble()*HEIGHT)] = WALL;
		}
	}
	public int getPoint(int x, int y){
		return this.board[x][y];
	}
	public void setPoint(int x, int y, int object){
		this.board[x][y] = object;
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
