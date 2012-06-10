package physics;

import org.lwjgl.util.vector.Vector3f;

public class Arrow {
	
    private Vector3f point = new Vector3f(0, 0, 0);
    private Vector3f vector = new Vector3f(0, 0, 0);
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}
	Arrow(Vector3f point, Vector3f vector){
		this.point = point;
		this.vector = vector;
	}
	public Vector3f getPoint(){
		return point;
	}
	public Vector3f getVector(){
		return vector;
	}
}
