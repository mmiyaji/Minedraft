package game;

import java.util.Random;
import java.util.Vector;

public class Board{
	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;
	public static final int MAX_TURNS  = 60;
	private int[][] board = new int[WIDTH+2][HEIGHT+2];
	private Vector PlayersPos = new Vector();
	private Vector MovablePos[] = new Vector[MAX_TURNS+1];
	private boolean attack;
	private int turns; // 手数(0からはじまる)
	private int ENEMY_NUM = 1;
	
	public Board(){
		// Vectorの配列を初期化
		for(int i=0; i<=MAX_TURNS; i++){
			MovablePos[i] = new Vector();
		}
		initBoard(10);
	}
	public Board(int num){
		ENEMY_NUM = num;
		// Vectorの配列を初期化
		for(int i=0; i<=MAX_TURNS; i++){
			MovablePos[i] = new Vector();
		}
		initBoard(10);
	}
	public void initBoard(long seed){
		Random rand = new Random(seed);
		turns = 0;
//		全マスを空にする
		for(int i=0; i<WIDTH+2; i++){
			for(int j=0; j<WIDTH+2; j++){
				board[i][j] = Piece.EMPTY;
			}
		}
//		周りに壁を設定
		for(int i=0; i<WIDTH+2; i++){
			board[i][0] = Piece.WALL;
			board[i][HEIGHT+1] = Piece.WALL;
		}
		for(int j=1; j<=HEIGHT; j++){
			board[0][j] = Piece.WALL;
			board[WIDTH+1][j] = Piece.WALL;
		}
//		ランダムに障害物（壁）配置
		for(int i=1; i<=WIDTH; i++){
			board[i][(int)(rand.nextDouble()*HEIGHT)+1] = Piece.WALL;
		}
		int x = 0;
		int y = 0;
		for(int i=0;i<ENEMY_NUM;i++){
			while(true){
				x = (int)(rand.nextDouble()*WIDTH)+1;
				y = (int)(rand.nextDouble()*HEIGHT)+1;
				if(board[x][y] == Piece.EMPTY){
					break;
				}
			}
			PlayersPos.add(new Point(x, y));
			board[x][y] = Piece.ENEMY;
		}
	}
	private void initMovable(){
		MovablePos[turns].clear();
		
	}
	public Point getPosition(int id){
		Point point = new Point();
		for(int i=1;i<WIDTH;i++){
			for(int j=1;j<HEIGHT;j++){
				if(board[i][j]==id){
					point.x = i;
					point.y = j;
					break;
				}
			}
		}
		return point;
	}
	public int getPoint(int x, int y){
		return this.board[x][y];
	}
	public void setPoint(int x, int y, int object){
		this.board[x][y] = object;
	}
	public void showBoard(){
		for(int i=0; i<=Board.WIDTH; i++){
			for(int j=0; j<=Board.HEIGHT; j++){
				System.out.print(this.board[i][j]);
			}
			System.out.println("");
		}
	}
	public boolean isGameOver(){
		// 60手に達していたらゲーム終了
		if(turns == MAX_TURNS) return true;
		// 打てる手があるならゲーム終了ではない
//		if(MovablePos[turns].size() != 0) return false;
		//	現在の手番と逆の色が打てるかどうか調べる
//		Disc disc = new Disc();
//		disc.color = -CurrentColor;
//		for(int x=1; x<=BOARD_SIZE; x++)
//		{
//			disc.x = x;
//			for(int y=1; y<=BOARD_SIZE; y++)
//			{
//				disc.y = y;
//				// 置ける箇所が1つでもあればゲーム終了ではない
//				if(checkMobility(disc) != NONE) return false;
//			}
//		}
		return false;
	}
	public Vector getMovablePos()
	{
		return MovablePos[turns];
	}
	public boolean move(Point point){
		return true;
	}
	public boolean move(Point now_point, Point point)
	{
		if(point.x <= 0 || point.x > WIDTH) return false;
		if(point.y <= 0 || point.y > HEIGHT) return false;
//		if(MovableDir[turns][point.x][point.y] == Piece.EMPTY) return false;
		
//		flipDiscs(point);
		if(board[point.x][point.y] == Piece.EMPTY){
			board[now_point.x][now_point.y] = Piece.EMPTY;
			board[point.x][point.y] = Piece.ME;
		}
		turns++;
//		CurrentColor = -CurrentColor;

//		initMovable();

		return true;
	}
}
