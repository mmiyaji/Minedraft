package game;

public class Ball implements Cloneable{
    int id;
    int time;
    float x;
    float y;
    Player owner;
    float angle;
    public Ball(int id, Player p, float x, float y, float angle){
	this.id = id;
	this.time = 1;
	this.owner = p;
	this.x = x;
	this.y = y;
	this.angle = angle;
    }
    public String toString()
    {
	String coord = new String();
	coord += "Ball owner:"+owner.getName();
	coord += ", time:"+time;
	coord += ", x:"+x;
	coord += ", y:"+y;
	coord += ", angle:"+angle;
	return coord;
    }
    @Override
	public Object clone() {	//throwsを無くす
	try {
	    return super.clone();
	} catch (CloneNotSupportedException e) {
	    throw new InternalError(e.toString());
	}
    }
}

