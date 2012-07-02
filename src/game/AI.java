package game;
import java.util.*;

class AiAlgorithm extends AI
{
    public AiAlgorithm()
    {
	this.name = "AI";
	this.start_time = 0;
	this.limit_time = Long.MAX_VALUE;
    }	
    public void move(Board board)
    {
	// これ以降を工夫してAIを作る．
	Point p = null;
	Random rand = new Random();
	// 移動可能位置の取得
	Vector<Point> movables = board.getMovablePos();
	// 適当に移動
	p = (Point)movables.get((int)(rand.nextDouble()*movables.size()));
	System.out.println(p);
	board.move(p);
	Player me = board.getME();
	Point me_point = board.getPosition(me.getID());
	Vector<Player> enemies = board.getEnemiesObject();
	for (int i = 0; i < enemies.size(); i++) {
	    Point p2 = board.getPosition(enemies.get(i).getID());
	    System.out.println(enemies.get(i).getName()+"を "+p2+"で発見");
	}
	// // 適当に向き変える
	float angle = rand.nextFloat()*360;
	// // 適当に物投げる 返り値に着弾点
	Point hit = board.throwing((int)angle);
	System.out.println("玉の着弾点： "+hit);
    }
}

