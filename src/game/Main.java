package game;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

interface Player{
    public static final int MAX_ENERGY = 1000;
    public static final int THROW_VAL = 300;
    public static final int MOVE_VAL = 200;
    public static final int REFRESH_VAL = 1000;
    public Object clone();
    public int getType();
    public int getID(); //このプレーヤーIDを返す
    public int getGroupID(); //このプレーヤーが所属するグループIDを返す
    public int getDamage(); //このプレーヤーが玉を受けた数を返す
    public int getHitCount(); //このプレーヤーが玉を当てた数を返す
    public float getAngle(); //このプレーヤーの向きを返す（0~360度）
    public float setAngle(float angle);
    public String getName(); //このプレーヤーの名前を返す
    public int getEnergy();
    public int damage();
    public int hit();
    public boolean refresh();
    public boolean spendEnergy(int energy);
    public void onTurn(Board board) throws Exception;
}
class UndoException extends Exception{private static final long serialVersionUID = 1L;}
class ExitException extends Exception{private static final long serialVersionUID = 1L;}
class GameOverException extends Exception{private static final long serialVersionUID = 1L;}
class AIPlayer implements Player, Cloneable
{
    private AI Ai = null;
    private int id;
    private int group_id;
    private int damage;
    private int hitcount;
    private int energy;
    private String name = "AI";
    private float angle = 0.0f;
    private int type = Piece.ME;
    public AIPlayer(int id){
	Ai = new AiAlgorithm();
	this.id = id;
	this.group_id = id;
	this.damage = 0;
	this.hitcount = 0;
	this.energy = MAX_ENERGY;
    }
    public AIPlayer(String name, int id)
    {
	Ai = new AiAlgorithm();
	this.name = name;
	this.id = id;
	this.group_id = id;
	this.damage = 0;
	this.hitcount = 0;
	this.energy = MAX_ENERGY;
    }
    public AIPlayer(String name, int id, int group_id)
    {
	Ai = new AiAlgorithm();
	this.name = name;
	this.id = id;
	this.group_id = group_id;
	this.damage = 0;
	this.hitcount = 0;
	this.energy = MAX_ENERGY;
    }
    public AIPlayer(AI Ai, int id){
	// Ai = new AiAlgorithm();
	this.Ai = Ai;
	this.id = id;
	this.group_id = id;
	this.damage = 0;
	this.hitcount = 0;
	this.energy = MAX_ENERGY;
    }
    public AIPlayer(AI Ai, String name, int id)
    {
	// Ai = new AiAlgorithm();
	this.Ai = Ai;
	this.name = name;
	this.id = id;
	this.group_id = id;
	this.damage = 0;
	this.hitcount = 0;
	this.energy = MAX_ENERGY;
    }
    public AIPlayer(AI Ai, String name, int id, int group_id)
    {
	// Ai = new AiAlgorithm();
	this.Ai = Ai;
	this.name = name;
	this.id = id;
	this.group_id = group_id;
	this.damage = 0;
	this.hitcount = 0;
	this.energy = MAX_ENERGY;
    }
    public void onTurn(Board board) throws GameOverException
    {
	System.out.println(name+"が思考中...");
	this.refresh();
	long start = System.currentTimeMillis();
	Ai.move(board);
	board.turnEnd();
	System.out.println(" 完了 思考時間："+(System.currentTimeMillis()-start)/1000.0+"秒");
	if(board.isGameOver()) throw new GameOverException();
    }
    @Override
	public Object clone() {	//throwsを無くす
	try {
	    return super.clone();
	} catch (CloneNotSupportedException e) {
	    throw new InternalError(e.toString());
	}
    }
    @Override
	public int getID(){return this.id;}
    @Override
	public int getGroupID(){return this.group_id;}
    @Override
	public int getType(){return this.type;}
    @Override
	public int getDamage(){return this.damage;}
    @Override
	public int getHitCount(){return this.hitcount;}
    @Override
	public int getEnergy(){return this.energy;}
    @Override
	public String getName(){return this.name;}
    @Override
	public float getAngle() {return this.angle;}
    @Override
	public float setAngle(float angle) {return this.angle = angle;}
    @Override
	public int damage(){this.damage++; return this.damage;}
    @Override
	public int hit(){this.hitcount++; return this.hitcount;}
    @Override
	public boolean refresh(){
	this.energy += REFRESH_VAL;
	if (this.energy > MAX_ENERGY) {
	    this.energy = MAX_ENERGY;
	    return true;
	}
	return false;
    }
    @Override
	public boolean spendEnergy(int energy){
	if (this.energy - energy < 0) {
	    System.out.println("too tired!");
	    this.energy = 0;
	    return false;
	}
	this.energy -= energy;
	return true;
    }
};
public class Main implements Runnable{
    final static int ENEMY_NUM = 1;
    final static int FRIEND_NUM = 1;
    final static int INDENT_NUM = 0;
    final static int GROUP_NUM = 2;
    int current_turn = 0;
    Vector<Player> players;
    Vector<Group> groups;
    private Board board;
    long start, stop, diff;
    private static Main main;
    public static Window window;
    private static int SLEEP_TIME  = 100;
    public static boolean iswindow = true;
    public static volatile boolean running = true;

    public Main(){
	System.out.println("Program start");
	groups = new Vector<Group>();
	for(int i=0;i<GROUP_NUM;i++){
	    System.out.println("Create group "+i);
	    groups.add(new Group("Group"+i, i));
	}
	players = new Vector<Player>();
	for(int i=0; i<FRIEND_NUM; i++){
	    System.out.println("join up AI"+i);
	    players.add(new AIPlayer(new AiAlgorithm(), "AI"+i, i, 0));
	    groups.get(0).join(players.get(i));
	}
	for(int i=FRIEND_NUM; i<ENEMY_NUM+FRIEND_NUM; i++){
	    System.out.println("join up ENEMY"+i);
	    players.add(new AIPlayer(new EnemyAlgorithm(), "Enemy"+i, i, 1));
	    groups.get(1).join(players.get(i));
	}
	board = new Board(ENEMY_NUM, players, this);
	window = null;
	start = System.currentTimeMillis();
    }
    public Board getBoard(){
	return this.board;
    }

    public static void main(String[] args) {
	System.out.println("Start");
	main = new Main();
	if(main.isWindow(args)){
	    main.iswindow = false;
	}
	if(iswindow){
	    window = new Window(true, main.getBoard());
	    SLEEP_TIME = 100;
	}else{
	    SLEEP_TIME = 0;
	}
	main.mainLoop();
    }
    Boolean isWindow(String[] args){
	String arg_tmp = "";
	Boolean flag = false;
	for(int i=0; i<args.length; i++){
	    arg_tmp = args[i];
	    if(arg_tmp.equals("nw") || arg_tmp.equals("nowindow")){
		flag = true;
	    }
	}
	return flag;
    }
    private void mainLoop(){
	int execFlag = 0;
	while (running) {
	    execFlag = main.coreExec();
	    if(execFlag != 0){running = false;}
	}
    }
    @Override
	public void run() {
	coreExec();
    }
    public int coreExec(){
	try{
	    System.out.println(current_turn);
	    ((Player) players.get(current_turn)).onTurn(board);
	    if(iswindow){
		window.repaint();
		board.showBoard();
	    }
	    board.showBoard();
	}
	catch(ExitException e)
	    {
		return 1;
	    }
	catch(GameOverException e)
	    {
		stop = System.currentTimeMillis();
		diff = stop - start;
		System.out.println("ゲーム終了");
		// board.judge();
		System.out.println("ゲーム時間 : "+diff/1000.0+"秒");
		String message = "Game set \n";
		Vector<Point> group_point = new Vector<Point>();
		for (int i = 0; i < groups.size(); i++) {
		    group_point.add(new Point(0,0));
		}
		for (int i = 0; i < players.size(); i++) {
		    Player p = players.get(i);
		    Point g = group_point.get(p.getGroupID());
		    g.x += p.getDamage();
		    g.y += p.getHitCount();
		}
		int tmp_id = 0;
		int tmp_val = 0;
		boolean draw_flag = false;
		for (int i = 0; i < groups.size(); i++) {
		    Point gp = group_point.get(i);
		    Group g = groups.get(i);
		    if(tmp_val == (gp.y - gp.x)){
			draw_flag = true;
		    }
		    if(tmp_val > (gp.y - gp.x)){
			tmp_val = gp.y - gp.x;
			tmp_id = i;
			draw_flag = false;
		    }
		}
		if(draw_flag){
		    message += "Draw game.\n\n";
		}else{
		    message += groups.get(tmp_id).getName()+" wins.\n\n";
		}
		for (int i = 0; i < groups.size(); i++) {
		    Point gp = group_point.get(i);
		    Group g = groups.get(i);
		    message += g.getName()+" The number of hit:"+gp.x + " , damage:"+gp.y + "\n";
		}
		if(iswindow){
		    JFrame frame = new JFrame();
		    JOptionPane.showMessageDialog(frame, message);
		}
		System.out.println(message);
		return 2;
	    }
	catch(Exception e)
	    {
		// 予期しない例外
		System.out.println("Unexpected exception: " + e);
		return 3;
	    }
	try{
	    Thread.sleep(SLEEP_TIME);
	}catch(InterruptedException e){}
	//		ターン交代
	current_turn = ++current_turn % players.size();
	return 0;
    }
}
