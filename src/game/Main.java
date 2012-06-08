package game;
import game.*;
import gui.Window;

import java.util.Random;
import java.util.Vector;
import java.io.*;

interface Player{
	public void onTurn(Board board) throws Exception;
}
class UndoException extends Exception{}
class ExitException extends Exception{}
class GameOverException extends Exception{}
class HumanPlayer implements Player{
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
}
class AIEnemy implements Player
{
	private Enemy enemy = null;
	public static int id;
	public String name = "Enemy";
	
	public AIEnemy(String name, int id)
	{
		this.name = name;
		this.id = id;
		enemy = new EnemyAlgorithm();
	}
	public void onTurn(Board board) throws GameOverException
	{
		System.out.println(name+"が思考中...");
		long start = System.currentTimeMillis();
//		enemy.move(board);
		System.out.println(" 完了 思考時間："+(System.currentTimeMillis()-start)/1000.0+"秒");
		if(board.isGameOver()) throw new GameOverException();
	}
};
class AIPlayer implements Player
{
	private AI Ai = null;
	public static int id;
	public String name = "AI";
	
	public AIPlayer(int id)
	{
		this.id = id;
		Ai = new AiAlgorithm();
	}
	public AIPlayer(String name, int id)
	{
		Ai = new AiAlgorithm();
		this.name = name;
		this.id = id;
	}
	public void onTurn(Board board) throws GameOverException
	{
		System.out.println(name+"が思考中...");
		long start = System.currentTimeMillis();
		Ai.move(board);
		System.out.println(" 完了 思考時間："+(System.currentTimeMillis()-start)/1000.0+"秒");
		if(board.isGameOver()) throw new GameOverException();
	}
};
public class Main{
	final static int ENEMY_NUM = 2;
	public static void main(String[] args) {
		System.out.print("Program start");
		Vector players = new Vector();
		int current_turn = 0;
		players.add(new AIPlayer(0));
		for(int i=1;i<=ENEMY_NUM;i++){
			players.add(new AIEnemy("Enemy"+i, i));
		}
		Board board = new Board(ENEMY_NUM);
		Window window = null;
		final Boolean is_window = true;
		if(is_window){
			window = new Window(true, board);
		}
		long seed = 1000;
		Random rand = new Random(seed);
		int x, y;
		long start, stop, diff;
		start = System.currentTimeMillis();
		while(true){
			try{
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
				return;
			}
			catch(GameOverException e)
			{
				stop = System.currentTimeMillis();
				diff = stop - start;
				System.out.println("ゲーム時間 : "+diff/1000.0+"秒");
				return;
			}
			catch(Exception e)
			{
				// 予期しない例外
				System.out.println("Unexpected exception: " + e);
				return;
			}

			try{
				Thread.sleep(200);
			}catch(InterruptedException e){}
//			ターン交代
			current_turn = ++current_turn % players.size();
		}
	}
	Boolean isWindow(String[] args){
		String arg_tmp = "";
		Boolean flag = false;
		for(int i=0; i<args.length; i++){
			arg_tmp = args[i];
			System.out.println(arg_tmp+";"+arg_tmp.charAt(0));
			if(arg_tmp.equals("nw") || arg_tmp.equals("nowindow")){
				flag = true;
			}
		}
		return flag;
	}
}
