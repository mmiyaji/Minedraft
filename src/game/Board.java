package game;

import java.util.Random;
import java.util.Vector;

public class Board implements Cloneable{
    public static final int WIDTH = 20; // フィールド横マスの数
    public static final int HEIGHT = 20; // フィールド縦マスの数
    public static final float tileSize = 1.0f; // フィールドマスの大きさ
    public static final int MAX_TURNS  = 60; // 最大ターン数
    private static int SLEEP_TIME  = 7; // 画面描画用スリープ変数
    private float WIND_DYNAMICS  = 0.002f; // 風の強さ
    private int WIND_DIRECTION  = 160; // 風の向き
    public static final int MAX_THROWTIME  = Integer.MAX_VALUE; // 玉の最大飛距離(存在できる時間)
    public static final int MAX_THROWTICKS  = 150; // 次自分のターンまでに玉がどれだけ飛ぶか
    private int THROWTICKS  = 100; // 一ターンに玉がどれだけの時間飛ぶか MAX_THROWTICKS / playerの数で初期化する
    private int[][] board = new int[WIDTH+2][HEIGHT+2];
    private Vector<Point> PlayersPos = new Vector<Point>();
    @SuppressWarnings("unchecked")
	private Vector<Point> MovablePos[] = new Vector[MAX_TURNS+1];
    private Vector<Player> Players;
    private Vector<float[]> Arrows;
    private Vector<Ball> Balls;
    private static Main main;
    private int turns; // 手数(0からはじまる)
    private int current_player_id;
    private boolean isthrowing = false;
    private int count_ball = 0;
    /*#######################################################
     * 便利関数
     ####################################################### */
    public Vector<Ball> getBalls(){
	/**
	   フィールド上に存在する玉のインスタンスを返す
	   不正防止の為、コピーしているが、
	   clone()はshallow copyなので全てコピーしてから返す
	 **/
	Vector<Ball> balls = new Vector<Ball>();
	for (int i = 0; i < Balls.size(); i++) {
	    balls.add((Ball)(Balls.get(i)).clone());
	}
	return balls;
    }
    public Vector<Point> getPlayers(){
	/**
	   すべてのプレーヤーの位置を返す
	**/
	return getPlayersPosition();
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
	for(int i=0; i<Board.HEIGHT+2; i++){
	    for(int j=0; j<Board.WIDTH+2; j++){
		System.out.print(this.board[j][i]+",");
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
    public int getMaxTurn(){
	/**
	   最大ターン数を返す
	**/
	return this.MAX_TURNS;
    }
    public Point getPosition(int id){
	/**
	   指定されたIDのオブジェクトがどこにいるのか返す
	**/
	// 下記の以前バージョンでは同じグループIDを持つオブジェクトで最初に見つかった位置を返している
	// グループメンバーが一人の場合だと動くけど、今後のことを考えて仕様変更
	// Point point = new Point();
	// for(int i=1;i<WIDTH;i++){
	//     for(int j=1;j<HEIGHT;j++){
	// 	if(board[i][j]==id){
	// 	    point.x = i;
	// 	    point.y = j;
	// 	    break;
	// 	}
	//     }
	// }
	for (int i = 0; i < Players.size(); i++) {
	    if (Players.get(i).getID() == id) {
		return PlayersPos.get(i);
	    }
	}
	// 見つからない場合はnullを返す
	return null;
    }
    public int getPoint(int x, int y){
	/**
	   指定したフィールド上に何がいるのか返す(空白 壁 グループID のどれか)
	**/
	return this.board[x][y];
    }
    public Player getPointPlayer(int x, int y){
	/**
	   指定した盤上の位置いるプレーヤーインスタンスを返す
	**/
	return (Player)getPointPlayerReal(x, y).clone();
    }
    private Player getPointPlayerReal(int x, int y){
	/**
	   指定した盤上の位置にいるプレーヤーを返す
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
	float now_angle = player.getAngle();
	int cost = (int)Math.abs(now_angle - angle) % 180;
	player.setAngle(angle);
	// player.spendEnergy(cost);
	return true;
    }
    public Point throwing(){
	return this.throwing((int)Players.get(current_player_id).getAngle());
    }
    public Point throwing(int angle){
	return this.throwing((float)(Math.PI*(angle/180.0)));
    }
    public Point throwing(float angle){
	/**
	   現在位置から引数で与えられた方向へ向けて、玉を投げる。
	   その際の当たり判定方法は、t秒 * Piece.DYNAMICS で得られる玉の位置と、
	   物体の位置が同一マス上にある場合は当たりとする。
	   なので、運良くカスる形で当たらないことがあるかも。

	   6/29追記
	   風の概念追加
	**/
	if(isthrowing){
	    return null;
	}
	isthrowing = true;
	count_ball++;
	this.angle((float)(angle*180f/Math.PI));
	System.out.println("throwing "+angle);
	Player player = Players.get(current_player_id);
	Point hit = new Point();
	if (player.spendEnergy(Player.THROW_VAL)) {
	    Ball ball = new Ball(count_ball, player,
				 getPosition(player.getID()).x*tileSize+tileSize/2,
				 getPosition(player.getID()).y*tileSize+tileSize/2,
				 angle);
	    System.out.println(ball);
	    Balls.add(ball);
	    // 玉(弓矢)オブジェクト生成 Arrows -> Balls に変更(最初は弓のつもりで作ってました)
	    hit = this.runBallThread(ball);
	    // Board bclone = (Board)this.clone();
	    // hit = bclone.runBallThread(ball, 0);
	    // 玉(弓矢)オブジェクト破棄 玉が同時に存在することが出来るようになったので不要に
	    // 前と同じモードにするには MAX_THROWTICKS を極端に大きくすればよい
	    // Balls.clear();
	}
	else{
	    this.runBallThread(0);
	    return null;
	}
	return hit;
    }
    private Point runBallThread(Ball ball){
	return this.runBallThread(ball.id, SLEEP_TIME);
    }
    private Point runBallThread(int id){
	return this.runBallThread(id, SLEEP_TIME);
    }
    private Point runBallThread(Ball ball, int sleep){
	return this.runBallThread(ball.id, sleep);
    }
    private Point runBallThread(int id, int sleep){
	/**
	   一定時間分だけ玉の位置を移動する
	   throwing関数 or ターン終了した時 呼ばれる
	 **/
	double dynamics = 0;
	Point hit = new Point();
	int ticks = 0;
	while(true){
	    if(Balls.size() > 0){
		ticks++;
		if(ticks > THROWTICKS){break;}
		for (int i = 0; i < Balls.size(); i++) {
		    Ball ball = Balls.get(i);
		    dynamics = (0.001)* ball.time* ball.time;
		    ball.time++;
		    if (dynamics > 1) {
			// dynamics = 1;
			dynamics = Math.log(ball.time);
		    }
		    // arrow[0] -> 玉(弓矢)のx座標，arrow[1] -> 玉のy座標 初期値は投げた人の中心座標
		    ball.x += Math.cos(ball.angle)*Piece.DYNAMICS
			+ Math.cos(Math.PI*((float)WIND_DIRECTION/180.0))*WIND_DYNAMICS * dynamics;
		    ball.y += Math.sin(ball.angle)*Piece.DYNAMICS
			+ Math.sin(Math.PI*((float)WIND_DIRECTION/180.0))*WIND_DYNAMICS * dynamics;
		    // 盤上でのポイントに変換
		    Point bpoint = convertRealToBoard(ball.x, ball.y);
		    if(ball.id == id){
			hit.x = bpoint.x;
			hit.y = bpoint.y;
		    }
		    if(getPoint(bpoint.x, bpoint.y) != Piece.EMPTY){
			// 壁かどうか判定
			if(getPoint(bpoint.x, bpoint.y) != Piece.WALL){
			    Player p = getPointPlayerReal(bpoint.x, bpoint.y);
			    // 玉を投げた人とあたった人が同一だった場合、無視
			    if(p.getID() != ball.getOwner().getID()){
				p.damage();
				ball.getOwner().hit();
				Balls.remove(ball);
				break;
			    }
			}else{
			    Balls.remove(ball);
			    break;
			}
		    }
		    // もしかしたらバグで外に出た場合終わらないはずなのでエラー処理
		    if(ball.x>(WIDTH+2)*tileSize || ball.x<0 ||
		       ball.y>(HEIGHT+2)*tileSize || ball.y<0 ){
			Balls.remove(ball);
			break;
		    }
		    // まずありえないけどint型のインクリメントオーバーフロー処理
		    if(ball.time >= MAX_THROWTIME){
			Balls.remove(ball);
			break;
		    }
		}
		// GUIモードの場合、描画処理
		if(main.window != null){
		    // main.window.paintArrow(Arrows);
		    main.window.paintBall(Balls);
		    main.window.repaint();
		    // アニメーションっぽくするため、適度にスリープ挟む
		    try{
			Thread.sleep(sleep);
		    }catch (InterruptedException e){}
		}
	    }
	    else{
		break;
	    }
	}
	return hit;
    }
    public Point convertRealToBoard(float x, float y){
	// 数値的な盤上の位置からフィールドにおけるポイントを返す
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
	    d.add(Players.get(i).getDamage());
	}
	return d;
    }
    public void turnEnd(){
	if(!this.isthrowing){
	    this.runBallThread(0);
	}
	turns++;
	current_player_id = main.getNextTurn();
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
    public Board(int num, Vector<Player> players, Main main, int wind_dis, float wind_dyn){
	Players = players;
	Board.main = main;
	if(!Main.iswindow){
	    SLEEP_TIME = 0;
	}
	// Vectorの配列を初期化
	for(int i=0; i<=MAX_TURNS; i++){
	    MovablePos[i] = new Vector<Point>();
	}
	initBoard(10, players, wind_dis, wind_dyn);
    }
    private void initBoard(long seed, Vector<Player> players){
	Random rand = new Random();
	this.initBoard(seed, players,
		       (int)(rand.nextDouble()*360),
		       rand.nextFloat()*(Piece.DYNAMICS/5f) + WIND_DYNAMICS/2);
    }
    private void initBoard(long seed, Vector<Player> players, int wind_dis, float wind_dyn){
	/**
	   初期化処理
	**/
	Random rand = new Random();
	Arrows = new Vector<float[]>();
	Balls = new Vector<Ball>();
	turns = 0;
	isthrowing = false;
	count_ball = 0;
	current_player_id = main.getCurrentTurn();
	THROWTICKS =(int)(MAX_THROWTICKS / players.size());
	WIND_DIRECTION = wind_dis;
	WIND_DYNAMICS = wind_dyn;
	System.out.println("WIND DYNAMICS " + WIND_DYNAMICS);
	System.out.println("WIND DERECTION " + WIND_DIRECTION);
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
	isthrowing = false;
	MovablePos[turns].clear();
	Player player = Players.get(current_player_id);
	Point pos = PlayersPos.get(current_player_id);
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
	// 障害物の位置を返す
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
	//自分が移動することができるすべての位置を返す
	return MovablePos[turns];
    }
    public boolean move(Point point)
    {
	if(point.x <= 0 || point.x > WIDTH) return false;
	if(point.y <= 0 || point.y > HEIGHT) return false;
	Vector<Point> mp = getMovablePos();
	boolean flag = false;
	for (int i = 0; i < mp.size(); i++) {
	    if(point.equals(mp.get(i))){
		    flag = true;
		    break;
	    }
	}
	if (flag) {
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
	else{
	    return false;
	}
    }
    @Override
	public Object clone() {
	// cloneを許可 エラーが出たら諦める
	try {
	    return super.clone();
	} catch (CloneNotSupportedException e) {
	    throw new InternalError(e.toString());
	}
    }
}
