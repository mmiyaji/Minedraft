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
	// Point currentPosition = board.getPosition(me.getID());
	// 適当に向き変える
	float angle = rand.nextFloat()*360;
	// board.angle((float)(Math.PI*(100.0/180.0)));


	// 自分のインスタンス取得
	Player me = (Player)board.getME();
	// 自分の位置取得
	Point me_point =  board.getPosition(me.getID());
	// 玉の位置初期化 投げた人の中心座標
	float arrow[] = {
	    // arrow[0] -> 玉(弓矢)のx座標，arrow[1] -> 玉のy座標
	    me_point.x*board.tileSize+board.tileSize/2,
	    me_point.y*board.tileSize+board.tileSize/2
	};
	// 玉の移動に関する物理ダイナミクス分だけ移動する
	arrow[0] += Math.cos(angle)*Piece.DYNAMICS;
	arrow[1] += Math.sin(angle)*Piece.DYNAMICS;
	// 盤上の座標からフィールドにおけるポイントに変換
	Point bpoint = board.convertRealToBoard(arrow[0], arrow[1]);
	
	
	board.angle(angle);
	// 適当に物投げる
	Point hit = board.throwing();
	System.out.println("Hit "+hit);
    }
}

