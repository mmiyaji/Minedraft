package game;
import java.util.*;

class EnemyAlgorithm extends AI
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
	if (rand.nextDouble()*10 > 5) {
	    board.move(p);
	}
	// 適当に物投げる
	// 敵の位置取得
	//		Vector<Point> players = board.getPlayers();
	//		Point playerPosition = players.get(0);
	//		Player player = board.getPointPlayer(playerPosition.x, playerPosition.y);
	// Vector<Player> players = board.getEnemiesObject();
	// for (int i = 0; i < players.size(); i++) {
	//     System.out.println("#########/"+players.get(i).getName());
	// }
	// 自分の位置取得
	//		Player me = (Player)board.getME();
	//		Point currentPosition = board.getPosition(me.getID());
		
	// 適当に向き変える
	float angle = rand.nextFloat()*360;
	board.angle(angle);
	// 適当に物投げる
	Point hit = board.throwing();
	System.out.println("Hit x:"+hit.x + ", y:"+ hit.y);
	// if (rand.nextDouble()*10 > 5) {
	//     board.throwing();
	// }

    }
}

