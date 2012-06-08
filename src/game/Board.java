package game;

import java.util.Random;
import java.util.Vector;

public class Board{
	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;
	public static final int MAX_TURNS  = 60;
	private int[][] board = new int[WIDTH+2][HEIGHT+2];
	private Vector<Point> PlayersPos = new Vector<Point>();
	private Vector<Point> MovablePos[] = new Vector[MAX_TURNS+1];
	private Vector<Player> Players;
	private int turns; // 手数(0からはじまる)
	private int current_player_id;
	private int ENEMY_NUM = 1;
	
	public Board(){
		// Vectorの配列を初期化
		for(int i=0; i<=MAX_TURNS; i++){
			MovablePos[i] = new Vector<Point>();
		}
		
//		initBoard(10);
	}
	public Board(int num, Vector<Player> players){
		ENEMY_NUM = num;
		Players = players;
		// Vectorの配列を初期化
		for(int i=0; i<=MAX_TURNS; i++){
			MovablePos[i] = new Vector<Point>();
		}
		initBoard(10, players);
	}
	public void initBoard(long seed, Vector<Player> players){
		Random rand = new Random(seed);
		turns = 0;
		current_player_id = 0;
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
		for(int i=0;i<players.size();i++){
			while(true){
				x = (int)(rand.nextDouble()*WIDTH)+1;
				y = (int)(rand.nextDouble()*HEIGHT)+1;
				if(board[x][y] == Piece.EMPTY){
					break;
				}
			}
			PlayersPos.add(new Point(x, y));
//			無理やり型判定
			if(players.get(i) instanceof AIEnemy){
//				board[x][y] = (int)((AIEnemy)players.get(i)).getType();
				board[x][y] = players.get(i).getType();
			}else if(players.get(i) instanceof AIPlayer){
//				board[x][y] = (int)((AIPlayer)players.get(i)).TYPE;
				board[x][y] = players.get(i).getType();
			}
		}
		initMovable();
	}
	private void initMovable(){
		MovablePos[turns].clear();
		Player player = Players.get(current_player_id);
		Boolean me = true;
		Point pos;
		if(player instanceof AIEnemy){
			me = false;
			pos = PlayersPos.get((int)((AIEnemy)player).getID());
			System.out.println(((AIEnemy)player).name+":"+current_player_id);
		}else{
			pos = PlayersPos.get((int)((AIPlayer)player).getID());
			System.out.println(player.getName()+":"+current_player_id);
		}
		for(int i=0;i<3;i++){
			for(int j=0;j<3;j++){
				if(board[pos.x-1+i][pos.y-1+j] == Piece.EMPTY){
					MovablePos[turns].add(new Point(pos.x-1+i, pos.y-1+j));
				}
			}
		}
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
	public Vector<Point> getMovablePos()
	{
		for(int i=0;i<=turns;i++){
			System.out.print(MovablePos[i]+" ,");
		}
		return MovablePos[turns];
	}
	public boolean move(Point point)
	{
		System.out.println("move");
		if(point.x <= 0 || point.x > WIDTH) return false;
		if(point.y <= 0 || point.y > HEIGHT) return false;
//		if(MovableDir[turns][point.x][point.y] == Piece.EMPTY) return false;
		Player player = Players.get(current_player_id);
		Boolean me = true;
		Point pos;
		if(player instanceof AIEnemy){
			me = false;
			pos = PlayersPos.get((int)((AIEnemy)player).getID());
		}else{
			pos = PlayersPos.get((int)((AIPlayer)player).getID());
		}
		int tmp = 0;
//		flipDiscs(point);
		if(board[point.x][point.y] == Piece.EMPTY){
			tmp = board[pos.x][pos.y];
			board[pos.x][pos.y] = Piece.EMPTY;
			board[point.x][point.y] = tmp;
			PlayersPos.get(current_player_id).x = point.x;
			PlayersPos.get(current_player_id).y = point.y;
		}
		turns++;
		current_player_id = ++current_player_id % (ENEMY_NUM+1);
//		CurrentColor = -CurrentColor;
		System.out.println(turns+"@"+ current_player_id);
		initMovable();

		return true;
	}
	public boolean pass()
	{
		// 打つ手があればパスできない
		if(MovablePos[turns].size() != 0) return false;
		// ゲームが終了しているなら、パスできない
		if(isGameOver()) return false;
//
//		CurrentColor = -CurrentColor;
//		
//		UpdateLog.add(new Vector());
		turns++;
		current_player_id = ++current_player_id % (ENEMY_NUM+1);
		initMovable();
		return true;
	}

}
