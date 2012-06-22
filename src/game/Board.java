package game;

import gui.Window;

import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

public class Board{
	public static final int WIDTH = 11;
	public static final int HEIGHT = 11;
    private static int gridSizeX = WIDTH;
    private static int gridSizeY = HEIGHT;
    private static float tileSize = 1.0f;
	public static final int MAX_TURNS  = 60;
	private int[][] board = new int[WIDTH+2][HEIGHT+2];
	private Vector<Point> PlayersPos = new Vector<Point>();
	private Vector<Point> MovablePos[] = new Vector[MAX_TURNS+1];
	private Vector<Player> Players;
	private Vector Arrows;
	private static Main main;
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
	public Board(int num, Vector<Player> players, Main main){
		ENEMY_NUM = players.size();
		Players = players;
		this.main = main;
		// Vectorの配列を初期化
		for(int i=0; i<=MAX_TURNS; i++){
			MovablePos[i] = new Vector<Point>();
		}
		initBoard(10, players);
	}
	public void initBoard(long seed, Vector<Player> players){
		Random rand = new Random(seed);
		Arrows = new Vector();
		turns = 0;
		current_player_id = 0;
		int x = 0;
		int y = 0;
//		全マスを空にする
//		Arrays.fill(board, Piece.EMPTY);
		for(int i=0; i<WIDTH+2; i++){
			for(int j=0; j<HEIGHT+2; j++){
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
		// player配置
		for(int i=0;i<players.size();i++){
			while(true){
				x = (int)(rand.nextDouble()*WIDTH)+1;
				y = (int)(rand.nextDouble()*HEIGHT)+1;
				if(board[x][y] == Piece.EMPTY){
					break;
				}
			}
			PlayersPos.add(new Point(x, y));
			board[x][y] = players.get(i).getType();
		}
//		ランダムに障害物（壁）配置
		for(int i=1; i<=WIDTH; i++){
			y = i;
			for(int j=0;j<1;j++){
				while(true){
					x = (int)(rand.nextDouble()*WIDTH)+1;
					if(board[x][y] == Piece.EMPTY || checkLabeling(x,y)){
						break;
					}
				}
				board[x][y] = Piece.WALL;
			}
		}
		initMovable();
	}
	private void initMovable(){
		MovablePos[turns].clear();
		Player player = Players.get(current_player_id);
		Point pos = PlayersPos.get(player.getID());
		for(int i=0;i<3;i++){
			for(int j=0;j<3;j++){
				if(board[pos.x-1+i][pos.y-1+j] == Piece.EMPTY){
					MovablePos[turns].add(new Point(pos.x-1+i, pos.y-1+j));
				}
			}
		}
	}
	public boolean checkLabeling(int x, int y){
//		int[][] label = new int[Board.WIDTH][Board.HEIGHT];
////		Arrays.fill(label, 0);
//		for(int i=0; i<WIDTH; i++){
//			for(int j=0; j<HEIGHT; j++){
//				label[i][j] = 0;
//			}
//		}
//		for(int i=1;i<=Board.WIDTH;i++){
//			for(int j=0;j<=Board.HEIGHT;j++){
//				if(label[x][y] == 0){}
//			}
//		}
		return false;
	}
	public int getTurn(){
		return this.turns;
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
	public Vector getArrow(){
		return this.Arrows;
	}
	public int getPoint(int x, int y){
		return this.board[x][y];
	}
	public Player getPointPlayer(int x, int y){
		int tmp = 0;
		for(int i=0;i<PlayersPos.size();i++){
			if(PlayersPos.get(i).x == x && PlayersPos.get(i).y == y){
				tmp = i;
				break;
			}
		}
		return Players.get(tmp);
	}

	public float getPointPlayerAngle(int x, int y){
		int tmp = 0;
		for(int i=0;i<PlayersPos.size();i++){
			if(PlayersPos.get(i).x == x && PlayersPos.get(i).y == y){
				tmp = i;
				break;
			}
		}
		return Players.get(tmp).getAngle();
	}
	public void setPoint(int x, int y, int object){
		this.board[x][y] = object;
	}
	public Vector<Point> getHazard(){
		Vector<Point> p = new Vector<Point>();
		for(int i=1;i<=WIDTH;i++){
			for(int j=1;j<=HEIGHT;j++){
				if(this.getPoint(i, j)==Piece.WALL){
					p.add(new Point(i, j));
				}
			}
		}
		return p;
	}
	public Vector<Point> getEnemy(){
		Vector<Point> p = new Vector<Point>();
		for(int i=1;i<=WIDTH;i++){
			for(int j=1;j<=HEIGHT;j++){
				if(this.getPoint(i, j)==Piece.ENEMY){
					p.add(new Point(i, j));
				}
			}
		}
		return p;
	}
	public void showBoard(){
		for(int i=0; i<Board.WIDTH+2; i++){
			for(int j=0; j<Board.HEIGHT+2; j++){
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
//		for(int i=0;i<=turns;i++){
//			System.out.print(MovablePos[i]+" ,");
//		}
		return MovablePos[turns];
	}
	public boolean move(Point point)
	{
		System.out.println("move");
		if(point.x <= 0 || point.x > WIDTH) return false;
		if(point.y <= 0 || point.y > HEIGHT) return false;
//		if(MovableDir[turns][point.x][point.y] == Piece.EMPTY) return false;
		Player player = Players.get(current_player_id);
//		Boolean me = true;
		Point pos = PlayersPos.get(current_player_id);
//		if(player instanceof AIEnemy){
//			me = false;
//		}else{
//			pos = PlayersPos.get((int)((AIPlayer)player).getID());
//		}
		int tmp = 0;
//		flipDiscs(point);
		if(board[point.x][point.y] == Piece.EMPTY){
			tmp = board[pos.x][pos.y];
			board[pos.x][pos.y] = Piece.EMPTY;
			board[point.x][point.y] = tmp;
			PlayersPos.set(current_player_id, point);
		}

		return true;
	}
	public boolean angle(float angle){
		System.out.println("angle " + angle);
		Player player = Players.get(current_player_id);
		player.setAngle(angle);
		return true;
	}
	public boolean throwing(float angle){
		System.out.println("throwing "+angle);
		Player player = Players.get(current_player_id);
		float arrow[] = {PlayersPos.get(player.getID()).x*tileSize+tileSize/2, PlayersPos.get(player.getID()).y*tileSize+tileSize/2};
		Arrows.add(arrow);
		while(true){
			arrow[0] += Math.cos(angle)*Piece.DYNAMICS;
			arrow[1] += Math.sin(angle)*Piece.DYNAMICS;
			Point bpoint = convertRealToBoard(arrow[0], arrow[1]);
			if(getPoint(bpoint.x, bpoint.y) != Piece.EMPTY && getPoint(bpoint.x, bpoint.y) != player.getType()){
				if(getPoint(bpoint.x, bpoint.y) != Piece.WALL){
					Player p = getPointPlayer(bpoint.x, bpoint.y);
					p.damage();
				}
				break;
			}
			if(arrow[0]>WIDTH*tileSize || arrow[0]<0 ||
					arrow[1]>HEIGHT*tileSize || arrow[1]<0 ){
				break;
			}
			main.window.paintArrow(Arrows);
			main.window.repaint();
			try{
				  Thread.sleep(10);
				}catch (InterruptedException e){
				}
		}
		Arrows.clear();
		return true;
	}
	public Point convertRealToBoard(float x, float y){
		int bx = (int)(x/tileSize);
		int by = (int)(y/tileSize);
		return new Point(bx, by);
	}
	public Vector getPlayersDamage(){
		Vector d = new Vector();
		for(int i=0;i<Players.size();i++){
			d.add(Players.get(i).getDamage());
		}
		return d;
	}
	public void turnEnd(){
		turns++;
		current_player_id = ++current_player_id % (ENEMY_NUM);
		System.out.println(turns+"@"+ current_player_id);
		initMovable();
	}
	public boolean pass()
	{
		// 打つ手があればパスできない
		if(MovablePos[turns].size() != 0) return false;
		// ゲームが終了しているなら、パスできない
		if(isGameOver()) return false;
//		UpdateLog.add(new Vector());
		turns++;
		current_player_id = ++current_player_id % (ENEMY_NUM);
		initMovable();
		return true;
	}

}
