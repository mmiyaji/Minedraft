package game;
import java.util.*;

class TAiAlgorithm extends AI
{
    public TAiAlgorithm()
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
	Player me = board.getME();
	Point me_point = board.getPosition(me.getID());
	Vector<Player> enemies = board.getEnemiesObject();
	double dis_val = Double.MAX_VALUE;
	double dis_tmp;
	int dis_index = 0;
	Point dis_point = new Point();
	for (int i = 0; i < enemies.size(); i++) {
	    Point p2 = board.getPosition(enemies.get(i).getID());
	    dis_tmp = calcDistance(me_point, p2);
	    if(dis_tmp < dis_val){
		dis_val = dis_tmp;
		dis_index = i;
		dis_point = p2;
	    }
	}
	System.out.println(dis_point);
	int kakudo = kakudo((double)(dis_point.x - me_point.x), (double)(dis_point.y - me_point.y));
	System.out.println(kakudo);
	Point hit = board.throwing((int)kakudo);
	System.out.println("Hit "+hit);
    }
    double calcDistance(Point p1, Point p2){
	double distance = 0.0;
	distance = Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
	System.out.println("D: "+distance);
	return distance;
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

