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
		// 敵の位置取得
		Vector<Point> players = board.getPlayers();
		Point playerPosition = players.get(0);
		Player player = board.getPointPlayer(playerPosition.x, playerPosition.y);
		
		// 自分の位置取得
		Player me = (Player)board.getME();
		Point currentPosition = board.getPosition(me.getID());
		
		// 敵のいる方向計算
		float targetAngle = 0.0f;
		targetAngle=(float)kakudo((playerPosition.x-currentPosition.x), (playerPosition.y-currentPosition.y));
		System.out.println(targetAngle+" "+playerPosition.x+"/"+currentPosition.x);
		board.throwing(targetAngle);
	}
	public int kakudo(double x,double y) { /*ベクトルの角度を計算*/
		  double s;
		  int deg;
		  s=Math.acos(x/Math.sqrt(x*x+y*y)); /*角度θを求める*/
		  s=(s/Math.PI)*180.0; /* ラジアンを度に変換 */
		  if (y<0) /* θ＞πの時 */
		      s=360-s;
		  deg=(int)Math.floor(s);
		  if ((s-deg)>=0.5) /*小数点を四捨五入*/
		      deg++;
		  return deg; /*角度θを返す*/
	}
}

