package game;
import gui.Window;
import gui.Minedraft;

//import java.util.Random;
import java.util.Vector;
import java.io.*;

interface Player{
	public int getType();
	public int getID();
	public int getDamage();
	public int damage();
	public float getAngle();
	public float setAngle(float angle);
	public String getName();
	public int getEnergy();
	public void onTurn(Board board) throws Exception;
}
class UndoException extends Exception{private static final long serialVersionUID = 1L;}
class ExitException extends Exception{private static final long serialVersionUID = 1L;}
class GameOverException extends Exception{private static final long serialVersionUID = 1L;}
class HumanPlayer implements Player{
	private int id;
	private int damage;
	private int energy;
	public String name = "Human";
	private float angle = 0.0f;
	private int type = Piece.ME; 

	public void onTurn(Board board) throws Exception
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while(true){
			System.out.print("手を\"f5\"のように入力、もしくは(U:取消/X:終了)を入力してください:");
			String in = br.readLine();
			if(in.equalsIgnoreCase("U")) throw new UndoException();
			if(in.equalsIgnoreCase("X")) throw new ExitException();
			Point p;
			try{
				p = new Point(in);
			}
			catch(IllegalArgumentException e)
			{
				System.out.println("正しい形式で入力してください！");
				continue;
			}
			if(!board.move(p))
			{
				System.out.println("そこには置けません！");
				continue;
			}
			if(board.isGameOver()) throw new GameOverException();
			break;
		}
	}
	@Override
	public int getID(){return this.id;}
	@Override
	public int getType(){return this.type;}
	@Override
	public int getDamage(){return this.damage;}
	@Override
	public int damage(){this.damage++; return this.damage;}
	@Override
	public int getEnergy(){return this.energy;}
	@Override
	public String getName(){return this.name;}
	@Override
	public float getAngle() {return this.angle;}
	@Override
	public float setAngle(float angle) {return this.angle = angle;}
}
class AIEnemy implements Player
{
	private Enemy enemy = null;
	private int id;
	private int damage;
	private int energy;
	public String name = "Enemy";
	private Point position = new Point(5, 6);
	private float angle = 0.0f;
	private int type = Piece.ENEMY; 
	public AIEnemy(String name, int id)
	{
		enemy = new EnemyAlgorithm();
		this.name = name;
		this.id = id;
		this.damage = 0;
		this.energy = 1000;
	}
	public void onTurn(Board board) throws GameOverException
	{
		System.out.println(name+"が思考中...");
		long start = System.currentTimeMillis();
		enemy.move(board);
		board.turnEnd();
		System.out.println(" 完了 思考時間："+(System.currentTimeMillis()-start)/1000.0+"秒");
		if(board.isGameOver()) throw new GameOverException();
	}
	@Override
	public int getID(){return this.id;}
	@Override
	public int getType(){return this.type;}
	@Override
	public int getDamage(){return this.damage;}
	@Override
	public int damage(){this.damage++; return this.damage;}
	@Override
	public int getEnergy(){return this.energy;}
	@Override
	public String getName(){return this.name;}
	@Override
	public float getAngle() {return this.angle;}
	@Override
	public float setAngle(float angle) {return this.angle = angle;}
};
class AIPlayer implements Player
{
	private AI Ai = null;
	private int id;
	private int damage;
	private int energy;
	private String name = "AI";
	private float angle = 0.0f;
	private int type = Piece.ME; 
	public AIPlayer(int id){
		this.id = id;
		this.damage = 0;
		this.energy = 1000;
		Ai = new AiAlgorithm();
	}
	public AIPlayer(String name, int id)
	{
		Ai = new AiAlgorithm();
		this.name = name;
		this.id = id;
		this.damage = 0;
		this.energy = 1000;
	}
	public void onTurn(Board board) throws GameOverException
	{
		System.out.println(name+"が思考中...");
		long start = System.currentTimeMillis();
		Ai.move(board);
		board.turnEnd();
		System.out.println(" 完了 思考時間："+(System.currentTimeMillis()-start)/1000.0+"秒");
		if(board.isGameOver()) throw new GameOverException();
	}
	@Override
	public int getID(){return this.id;}
	@Override
	public int getType(){return this.type;}
	@Override
	public int getDamage(){return this.damage;}
	@Override
	public int damage(){this.damage++; return this.damage;}
	@Override
	public int getEnergy(){return this.energy;}
	@Override
	public String getName(){return this.name;}
	@Override
	public float getAngle() {return this.angle;}
	@Override
	public float setAngle(float angle) {return this.angle = angle;}
};
public class Main implements Runnable{
	final static int ENEMY_NUM = 1;
	int current_turn = 0;
	Vector<Player> players;
	private Board board;
	final Boolean is_window = true;
	long start, stop, diff;
	private static Main main;
	public static Window window;
	private static Minedraft minedraft;
	private static Thread mainThread;
    public static volatile boolean running = true;

	public Main(){
		System.out.print("Program start");
		players = new Vector<Player>();
		players.add(new AIPlayer(0));
		for(int i=1;i<=ENEMY_NUM;i++){
			players.add(new AIEnemy("Enemy"+i, i));
		}
		board = new Board(ENEMY_NUM, players, this);
		window = null;
		minedraft = null;
		start = System.currentTimeMillis();
	}
	public Board getBoard(){
		return this.board;
	}
 
	public static void main(String[] args) {
		System.out.println("Start");
    	main = new Main();
    	mainThread = new Thread(main);
    	window = new Window(true, main.getBoard());
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
			if(is_window){
				window.repaint();
				window.setBoard(board);
			}else{
				board.showBoard();
			}
		}
		catch(ExitException e)
		{
			return 1;
		}
		catch(GameOverException e)
		{
			stop = System.currentTimeMillis();
			diff = stop - start;
			System.out.println("ゲーム時間 : "+diff/1000.0+"秒");
			
			return 2;
		}
		catch(Exception e)
		{
			// 予期しない例外
			System.out.println("Unexpected exception: " + e);
			return 3;
		}
		try{
			Thread.sleep(100);
		}catch(InterruptedException e){}
//		ターン交代
		current_turn = ++current_turn % players.size();
		return 0;
	}
}
