package game;

import java.util.Random;
import java.util.Vector;

public class Board{
    public static final int WIDTH = 9;
    public static final int HEIGHT = 9;
    private static float tileSize = 1.0f;
    public static final int MAX_TURNS  = 60;
    private static int SLEEP_TIME  = 10;
    private int[][] board = new int[WIDTH+2][HEIGHT+2];
    private Vector<Point> PlayersPos = new Vector<Point>();
    @SuppressWarnings("unchecked")
	private Vector<Point> MovablePos[] = new Vector[MAX_TURNS+1];
    private Vector<Player> Players;
    private Vector<float[]> Arrows;
    private static Main main;
    private int turns; // 手数(0からはじまる)
    private int current_player_id;
    /*#######################################################
     * 便利関数
     ####################################################### */
    public Vector<Point> getPlayers(){
	return  getPlayersPosition();
    }
    @SuppressWarnings("unchecked")
	public Vector<Player> getPlayersObject(){
	/**
	   すべてのプレーヤー インスタンスを返す
	**/
	return (Vector<Player>)Players.clone();
    }
    @SuppressWarnings("unchecked")
	public Vector<Point> getPlayersPosition(){
	/**
	   すべてのプレーヤーの位置を返す
	**/
	return (Vector<Point>)PlayersPos.clone();
    }
    public Vector<Point> getEnemies(){
	/**
	   敵(自分以外のグループに所属しているすべてのプレーヤー)の位置を返す
	**/
	return this.getEnemiesPositon();
    }
    public Vector<Player> getEnemiesObject(){
	/**
	   敵(自分以外のグループに所属しているすべてのプレーヤー)のインスタンスを返す
	**/
	Vector<Player> p = new Vector<Player>();
	int current_group_id = Players.get(current_player_id).getGroupID();
	for(int i=1;i<=WIDTH;i++){
	    for(int j=1;j<=HEIGHT;j++){
		if (this.getPoint(i, j) != Piece.EMPTY &&
		    this.getPoint(i, j) != Piece.WALL) {
		    if(this.getPoint(i, j) != current_group_id){
			p.add(getPointPlayer(i, j));
		    }
		}
	    }
	}
	return p;
    }
    public Vector<Point> getEnemiesPositon(){
	/**
	   敵(自分以外のグループに所属しているすべてのプレーヤー)の位置を返す
	**/
	Vector<Point> p = new Vector<Point>();
	int current_group_id = Players.get(current_player_id).getGroupID();
	for(int i=1;i<=WIDTH;i++){
	    for(int j=1;j<=HEIGHT;j++){
		if (this.getPoint(i, j) != Piece.EMPTY &&
		    this.getPoint(i, j) != Piece.WALL) {
		    if(this.getPoint(i, j) != current_group_id){
			p.add(new Point(i, j));
		    }
		}
	    }
	}
	return p;
    }
    public Vector<Point> getMembers(){
	/**
	   自分(と同じグループに所属しているプレーヤー)の位置を返す
	**/
	return (Vector<Point>)this.getMembersPositon();
    }
    public Vector<Player> getMembersObject(){
	/**
	   自分(と同じグループに所属しているプレーヤー)の位置を返す
	**/
	int current_group_id = Players.get(current_player_id).getGroupID();
	Vector<Player> p = new Vector<Player>();
	for(int i=1;i<=WIDTH;i++){
	    for(int j=1;j<=HEIGHT;j++){
		if(this.getPoint(i, j)==current_group_id){
		    p.add(getPointPlayer(i, j));
		}
	    }
	}
	return p;
    }
    public Vector<Point> getMembersPositon(){
	/**
	   自分(と同じグループに所属しているプレーヤー)の位置を返す
	**/
	int current_group_id = Players.get(current_player_id).getGroupID();
	Vector<Point> p = new Vector<Point>();
	for(int i=1;i<=WIDTH;i++){
	    for(int j=1;j<=HEIGHT;j++){
		if(this.getPoint(i, j)==current_group_id){
		    p.add(new Point(i, j));
		}
	    }
	}
	return p;
    }
    public Player getME(){
	/**
	   自分のプレーヤーインスタンスを返す
	**/
	return (Player) Players.get(current_player_id).clone();
    }
    public void showBoard(){
	/**
	   フィールド情報を出力
	**/
	for(int i=0; i<Board.WIDTH+2; i++){
	    for(int j=0; j<Board.HEIGHT+2; j++){
		System.out.print(this.board[i][j]);
	    }
	    System.out.println("");
	}
    }
    public int[][] getBoard(){
	/**
	   フィールドを返す(コピーなのでこれを直接弄ってもゲームに影響はない)
	**/
	return (int[][])this.board.clone();
    }
    public int getTurn(){
	/**
	   今 何ターン目か返す
	**/
	return this.turns;
    }
    public Point getPosition(int id){
	/**
	   指定されたIDのオブジェクトがどこにいるのか返す
	**/
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
	/**
	   指定したフィールド上に何がいるのか返す
	**/
	return this.board[x][y];
    }
    public Player getPointPlayer(int x, int y){
	/**
	   指定した盤上の位置いるプレーヤーを返す
	**/
	return (Player)getPointPlayerReal(x, y).clone();
    }
    private Player getPointPlayerReal(int x, int y){
	/**
	   指定した盤上の位置いるプレーヤーを返す
	**/
	int tmp = 0;
	for(int i=0;i<PlayersPos.size();i++){
	    if(PlayersPos.get(i).x == x &&
	       PlayersPos.get(i).y == y){
		tmp = i;
		break;
	    }
	}
	return Players.get(tmp);
    }
    public boolean angle(float angle){
	/**
	   現在のプレーヤーの向きをセットし直す
	**/
	System.out.println("angle " + angle);
	Player player = Players.get(current_player_id);
	player.setAngle(angle);
	return true;
    }
    public boolean throwing(){
	return throwing(Players.get(current_player_id).getAngle());
    }
    @SuppressWarnings("static-access")
	public boolean throwing(float angle){
	/**
	   現在位置から引数で与えられた方向へ向けて、玉を投げる。
	   その際の当たり判定方法は、t秒 * Piece.DYNAMICS で得られる玉の位置と、
	   物体の位置が同一マス上にある場合は当たりとする。
	   なので、運良くかする形で当たらないことがあるかも。
	**/
	System.out.println("throwing "+angle);
	Player player = Players.get(current_player_id);
	System.out.println(player.getEnergy());
	if (!player.spendEnergy(Player.THROW_VAL)) {
	    return false;
	}
	float arrow[] = {
	    PlayersPos.get(player.getID()).x*tileSize+tileSize/2,
	    PlayersPos.get(player.getID()).y*tileSize+tileSize/2
	};
	// 玉(弓矢)オブジェクト生成、今は同時に飛ぶことがないからいらない
	Arrows.add(arrow);
	while(true){
	    arrow[0] += Math.cos(angle)*Piece.DYNAMICS;
	    arrow[1] += Math.sin(angle)*Piece.DYNAMICS;
	    // 盤上でのポイントに変換
	    Point bpoint = convertRealToBoard(arrow[0], arrow[1]);
	    if(getPoint(bpoint.x, bpoint.y) != Piece.EMPTY){
		// getPoint(bpoint.x, bpoint.y) != player.getType()
		// 壁かどうか判定
		if(getPoint(bpoint.x, bpoint.y) != Piece.WALL){
		    Player p = getPointPlayerReal(bpoint.x, bpoint.y);
		    // 玉を投げた人とあたった人が同一だった場合、無視
		    if(p.getID() != player.getID()){
			p.damage();
			break;
		    }
		}else{
		    break;
		}
	    }
	    // もしかしたらバグで外に出た場合終わらないはずなのでエラー処理
	    if(arrow[0]>(WIDTH+2)*tileSize || arrow[0]<0 ||
	       arrow[1]>(HEIGHT+2)*tileSize || arrow[1]<0 ){
		break;
	    }
	    // GUIモードの場合、描画処理
	    if(main.window != null){
		main.window.paintArrow(Arrows);
		main.window.repaint();
		// アニメーションっぽくするため、適度にスリープ挟む
		try{
		    Thread.sleep(SLEEP_TIME);
		}catch (InterruptedException e){}
	    }
	}
	// 玉(弓矢)オブジェクト破棄
	Arrows.clear();
	return true;
    }
    public Point convertRealToBoard(float x, float y){
	int bx = (int)(x/tileSize);
	int by = (int)(y/tileSize);
	return new Point(bx, by);
    }
    public String getPointPlayerName(int i, int j){
	return this.getPointPlayer(i, j).getName();
    }
    public Vector<Object> getPlayersDamage(){
	Vector<Object> d = new Vector<Object>();
	for(int i=0;i<Players.size();i++){
	    d.add(Players.get(i).getEnergy());
	}
	return d;
    }
    public void turnEnd(){
	turns++;
	current_player_id = ++current_player_id % (Players.size());
	initMovable();
    }
    public Board(){
	// Vectorの配列を初期化
	for(int i=0; i<=MAX_TURNS; i++){
	    MovablePos[i] = new Vector<Point>();
	}
	//		initBoard(10);
    }
    public Board(int num, Vector<Player> players, Main main){
	Players = players;
	Board.main = main;
	if(!Main.iswindow){
	    SLEEP_TIME = 0;
	}
	// Vectorの配列を初期化
	for(int i=0; i<=MAX_TURNS; i++){
	    MovablePos[i] = new Vector<Point>();
	}
	initBoard(10, players);
    }
    public void initBoard(long seed, Vector<Player> players){
	/**
	   初期化処理
	**/
	Random rand = new Random(seed);
	Arrows = new Vector<float[]>();
	turns = 0;
	current_player_id = 0;
	int x = 0;
	int y = 0;
	// 全マスを空にする
	for(int i=0; i<WIDTH+2; i++){
	    for(int j=0; j<HEIGHT+2; j++){
		board[i][j] = Piece.EMPTY;
	    }
	}
	// 周りに壁を設定
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
	    board[x][y] = players.get(i).getGroupID();
	}
	// ランダムに障害物（壁）配置
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
	return false;
    }
    public Vector<float[]> getArrow(){
	return this.Arrows;
    }
    public float getPointPlayerAngle(int x, int y){
	return getPointPlayer(x, y).getAngle();
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
    public boolean isGameOver(){
	// 規定ターンに達していたらゲーム終了
	if(turns == MAX_TURNS) return true;
	return false;
    }
    public int judge(){
	Player player;
	int tmp_val = Integer.MAX_VALUE;
	int tmp_id = 0;
	for(int i=0;i<Players.size();i++){
	    player = Players.get(i);
	    System.out.println(player.getName() + " :"+player.getDamage());
	    if(tmp_val <= player.getDamage()){
		tmp_val = player.getDamage();
		tmp_id = i;
	    }
	}
	System.out.println(Players.get(tmp_id).getName()+"の勝利");
	return 0;
    }
    public Vector<Point> getMovablePos()
    {
	return MovablePos[turns];
    }
    public boolean move(Point point)
    {
	if(point.x <= 0 || point.x > WIDTH) return false;
	if(point.y <= 0 || point.y > HEIGHT) return false;
	Point pos = PlayersPos.get(current_player_id);
	int tmp = 0;
	if(board[point.x][point.y] == Piece.EMPTY){
	    if (!Players.get(current_player_id).spendEnergy(Player.MOVE_VAL)) {
		return false;
	    }
	    tmp = board[pos.x][pos.y];
	    board[pos.x][pos.y] = Piece.EMPTY;
	    board[point.x][point.y] = tmp;
	    PlayersPos.set(current_player_id, point);
	}
	return true;
    }
    public boolean pass()
    {
	// 打つ手があればパスできない
	if(MovablePos[turns].size() != 0) return false;
	// ゲームが終了しているなら、パスできない
	if(isGameOver()) return false;
	//		UpdateLog.add(new Vector());
	turns++;
	current_player_id = ++current_player_id % (Players.size());
	initMovable();
	return true;
    }
}
