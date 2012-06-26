package game;

import java.util.Vector;

public class Group {
	private int id;
	Vector<Player> members;
	public Group(int id){
		this.setID(id);
		members = new Vector<Player>();
	}
	private void setID(int id) {
		this.id = id;
	}
	public int getID() {
		return id;
	}
	public boolean join(Player p){
		for(int i=0;i<this.members.size();i++){
			if(this.members.get(i).getID() == p.getID()){
				System.out.println("Already joined");
				return false;
			}
		}
		this.members.add(p);
		return true;
	}
	public boolean remove(Player p){
		for(int i=0;i<this.members.size();i++){
			if(this.members.get(i).getID() == p.getID()){
				this.members.remove(p.getID());
				return true;
			}
		}
		System.out.println("Already removed");
		return false;
	}
	public Vector<Player> getMenbers(){
		return this.members;
	}
}
