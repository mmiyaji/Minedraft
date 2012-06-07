package game;
import game.*;
import gui.Window;

import java.util.Random;
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
//		if(board.getMovablePos().isEmpty()){
//			// パス
//			System.out.println("あなたはパスです。");
//			board.pass();
//			return;
//		}
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while(true){
			System.out.print("手を\"f5\"のように入力、もしくは(U:取消/X:終了)を入力してください:");
			String in = br.readLine();
			if(in.equalsIgnoreCase("U")) throw new UndoException();
			if(in.equalsIgnoreCase("X")) throw new ExitException();
//			Point p;
//			try{
//				p = new Point(in);
//			}
//			catch(IllegalArgumentException e)
//			{
//				System.out.println("正しい形式で入力してください！");
//				continue;
//			}
//			if(!board.move(p))
//			{
//				System.out.println("そこには置けません！");
//				continue;
//			}
			
			if(board.isGameOver()) throw new GameOverException();
			break;
		}
	}
}
class AIEnemy implements Player
{
	private Enemy enemy = null;
	public String name = "Enemy";
	public void onTurn(Board board) throws GameOverException
	{
		System.out.println(name+"が思考中...");
		long start = System.currentTimeMillis();
		enemy.move(board);
		System.out.println(" 完了 思考時間："+(System.currentTimeMillis()-start)/1000.0+"秒");
		if(board.isGameOver()) throw new GameOverException();
	}
};
class AIPlayer implements Player
{
	private AI Ai = null;
	public String name = "AI";
	public AIPlayer()
	{
		Ai = new AiAlgorithm();
	}
	public AIPlayer(String name)
	{
		Ai = new AiAlgorithm();
		this.name = name;
	}
	// 時間制限あり(秒)
	public AIPlayer(String name, int time)
	{
		Ai = new AiAlgorithm(time);
		this.name = name;
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
	public static void main(String[] args) {
		System.out.print("Program start");
		Board board;
		Player[] player = new Player[2];
		board = new Board();
		Window window = new Window(true, board);
		Random rand = new Random();
		int x, y;
		while(true){
			window.repaint();
			x = (int)(rand.nextDouble()*Board.WIDTH)+1;
			y = (int)(rand.nextDouble()*Board.HEIGHT)+1;
			board.setPoint(x, y, Piece.ENEMY);
			window.setBoard(board);
			System.out.println(x+":"+y);
			try{
				Thread.sleep(200);
			}catch(InterruptedException e){}
		}
	}
}
