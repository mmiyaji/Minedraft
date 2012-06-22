package game;
import java.util.*;

abstract class AI
{
	abstract public void move(Board board);

	// 一手思考にかける最大時間(ミリ秒)を設定．
	public long limit_time = 1000;
	public long start_time = 0;
}

class AiAlgorithm extends AI
{
	public AiAlgorithm()
	{
		this.start_time = 0;
		this.limit_time = Long.MAX_VALUE;
	}	
	// 一手思考にかける最大時間(秒で受け取ってミリ秒に変換)
	public AiAlgorithm(int limit_time)
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
		// 移動可能位置の取得
		Vector<Point> movables = board.getMovablePos();
		// これ以降を工夫してAIを作る．
		Point p = null;
		Random rand = new Random();
		// 適当に移動
		p = (Point)movables.get((int)(rand.nextDouble()*movables.size()));
		board.move(p);
		// 適当に向き変える
		board.angle(rand.nextFloat()*360);
		// 適当に物投げる
		board.throwing(rand.nextFloat()*360);
	}
}
