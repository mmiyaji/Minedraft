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
	// 適当に向き変える
	float angle = rand.nextFloat()*360;
	// 適当に物投げる 返り値に着弾点
	Point hit = board.throwing((int)angle);
	System.out.println("Hit "+hit);
    }
}

