package game;
import java.util.*;

public abstract class Enemy
{
	abstract public void move(Board board);

	// 一手思考にかける最大時間(ミリ秒)を設定．
	public long limit_time = 1000;
	public long start_time = 0;
}

class EnemyAlgorithm extends Enemy
{
	public EnemyAlgorithm()
	{
		this.start_time = 0;
		this.limit_time = Long.MAX_VALUE;
	}	
	// 一手思考にかける最大時間(秒で受け取ってミリ秒に変換)
	public EnemyAlgorithm(int limit_time)
	{
		this.start_time = 0;
		// 秒->ミリ秒
		this.limit_time = (long)limit_time*1000;
	}	
	
	class Move extends Point
	{
		public int eval = 0;
		public Move()
		{
			super(0, 0);
		}

		public Move(int x, int y, int e)
		{
			super(x, y);
			eval = e;
		}
	};	
	public void move(Board board)
	{
		long now_time = 0;
		this.start_time = System.currentTimeMillis();
		Vector movables = board.getMovablePos();
		// 打てる箇所がない場合はパス
		if(movables.size() == 0)
		{
			// パス
//			board.pass();
			System.out.println("パスします");
			return;
		}
		if(movables.size() == 1)
		{
			// 打てる箇所が一カ所だけなら探索は行わず、即座に打って返る
			board.move((Point) movables.get(0));
			System.out.print((Point) movables.get(0));
			return;
		}

		// これ以降を工夫してAIを作る．
		// 例として今打てるすべての手のうち，自分の石を最大化するような手を探す方法を示す．
		int eval, eval_max = Integer.MIN_VALUE;
		Point p = null;
		for(int i=0; i<movables.size(); i++)
		{
			// 一旦 打ってみる
			board.move((Point) movables.get(i));
			// 評価値計算
			eval = -evaluate(board);
			// 元の状態に巻き戻し
//			board.undo();
			// 評価値が良かったら保存
			if(eval > eval_max){
				p = (Point) movables.get(i);
				eval_max = eval;
			}
			// 割合表示
			System.out.print((int)(100.0*(double)(i+1)/(double)movables.size())+"% ");
			// 制限時間以上ならループから抜けて現在の最も良い手を返す
			now_time = System.currentTimeMillis()-this.start_time;
			if(this.limit_time < now_time) break;
		}
		// 実際に手を進める
		board.move(p);
		System.out.print("\n"+p);
	}
	// この関数を工夫する
	private int evaluate(Board board)
	{
		int eval = 0;
//		eval = board.getCurrentColor()
//			* (board.countDisc(Disc.BLACK) - board.countDisc(Disc.WHITE));
		return eval;
	}

}
