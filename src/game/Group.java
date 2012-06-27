package game;

import java.util.Vector;

public class Group {
	private int id;
	private String name;
	Vector<Player> members;
	public Group(String name, int id){
		this.id = id;
		this.name = name;
		members = new Vector<Player>();
	}
	private void setID(int id) {
		this.id = id;
	}
	public int getID() {
		return this.id;
	}
	public String getName() {
	    return this.name;
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
	protected boolean remove(Player p){
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
