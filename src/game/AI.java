package game;
import java.util.*;

abstract class AI
{
    abstract public void move(Board board);
    // 一手思考にかける最大時間(ミリ秒)を設定．
    // 今回は思考時間に制限を設けていないので無視してよし
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
    public void move(Board board)
    {
	// これ以降を工夫してAIを作る．
	Point p = null;
	Random rand = new Random();
	// 移動可能位置の取得
	Vector<Point> movables = board.getMovablePos();
	// 適当に移動
	p = (Point)movables.get((int)(rand.nextDouble()*movables.size()));
	if (rand.nextDouble()*10 > 5) {
	    board.move(p);
	}
	// すべての敵のインスタンス取得
	Vector<Player> players = board.getEnemiesObject();
	for (int i = 0; i < players.size(); i++) {
	    System.out.println(players.get(i).getName());
	}
	// 自分の位置取得
	Player me = (Player)board.getME();
	Point currentPosition = board.getPosition(me.getID());
	// 適当に向き変える
	float angle = rand.nextFloat()*360;
	// board.angle((float)(Math.PI*(100.0/180.0)));
	board.angle(angle);
	// 適当に物投げる
	Point hit = board.throwing();
	this.ah.hoge();
	System.out.println("Hit "+hit);
    }
    // public class AIHistory {
    // 	int count = 0;
    // 	public AIHistory(){
    // 	    System.out.println("hoge");
    // 	    count = 0;
    // 	}
    // 	public void hoge(){
    // 	    System.out.println("huga"+count);
    // 	    count++;
    // 	}
    // }
}

