package gui;

import game.Board;
import game.Piece;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import javax.swing.JPanel;

public class Field extends JPanel{

    private static final long serialVersionUID = 1L;
    volatile Vector<float[]> arrows;
    private Board board;
    public Field(){
	setBoard(new Board());
    }
    public Field(Board board){
	arrows = new Vector<float[]>();
	setBoard(board);
    }
    public void setBoard(Board board){
	this.board = board;
    }
    public void paintArrow(Vector<float[]> arrows){
	this.arrows = arrows;
    }
    @Override
	public void paintComponent(Graphics g) {
	super.paintComponent(g);
	Graphics2D g2 = (Graphics2D)g;
	int w = this.getWidth();
	int h = this.getHeight();
	g.setColor(Color.white);
	g2.fillRect(0, 0, w, h);
	g2.setPaint(new Color(0,0,255,100));
	for(int i = 0;i < Board.WIDTH+2;i++){
	    g2.drawLine((int)((float)(w*i)/(Board.WIDTH+2)), 0, (int)((float)(w*i)/(Board.WIDTH+2)), h);
	}
	for(int i = 0;i < Board.HEIGHT+2;i++){
	    g2.drawLine(0, (int)((float)(h*i)/(Board.HEIGHT+2)), w, (int)((float)(h*i)/(Board.HEIGHT+2)));
	}
		
	for(int i=0; i< Board.WIDTH+2; i++){
	    for(int j=0; j<Board.HEIGHT+2; j++){
		if(board.getPoint(i, j) != Piece.EMPTY){
		    g.setColor(Piece.COLORS(board.getPoint(i, j)));
		    g2.fillRect((int)((float)(w*(i))/(Board.WIDTH+2)), (int)((float)(h*(j))/(Board.HEIGHT+2)), w/(Board.WIDTH+2)+1, h/(Board.HEIGHT+2)+1);
		    if(board.getPoint(i, j) != Piece.WALL){
			// if(board.getPoint(i, j) == Piece.ME || board.getPoint(i, j) == Piece.ENEMY){
			g.setColor(Piece.COLORS[4]);
			float angle = board.getPointPlayerAngle(i, j);
			float angleS = angle-50;
			g2.fillArc((int)((float)(w*(i))/(Board.WIDTH+2)),
				   (int)((float)(h*(j))/(Board.HEIGHT+2)), 
				   w/(Board.WIDTH+2)+1, h/(Board.HEIGHT+2)+1, 
				   (int)angleS, 100);
			g2.drawString(board.getPointPlayerName(i, j),
				      (int)((float)(w*(i))/(Board.WIDTH+2)),
				      (int)((float)(h*(j))/(Board.HEIGHT+2)) );
		    }
		}
	    }
	}
	for(int i=0;i<this.arrows.size();i++){
	    g.setColor(Piece.COLORS[5]);
	    float arrow[] = (float[])arrows.get(i);
	    g2.fillArc((int)((float)(w*(arrow[0]))/(Board.WIDTH+2)),
		       (int)((float)(h*(arrow[1]))/(Board.HEIGHT+2)), 
		       15, 15,
		       0, 360);
	}
	// テキスト描画 切り分けたい気持ちはある
	g.setColor(Color.white);
	g2.drawString("Turns: "+(board.getTurn())+" / "+ Board.MAX_TURNS, 5, 20);
	Vector<Object> d = board.getPlayersDamage();
	for(int i=0;i<d.size();i++){
	    //			g.setColor(Piece.COLORS[3-i]);
	    g2.drawString("Player"+i+" damage: "+d.get(i), 150+150*i, 20);
	}
    }
}
