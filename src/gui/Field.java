package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import game.*;
import javax.swing.JPanel;

public class Field extends JPanel{

	/**
	 * Fieldクラス
	 * メインバトルフィールドを描画するクラス
	 */
	private static final long serialVersionUID = 1L;
	public static void main(String[] args) {
		Window window = new Window(true);
	}
	private Board board;
	public Field(){
		setBoard(new Board());
	}
	public Field(Board board){
		setBoard(board);
	}
	public void setBoard(Board board){
		this.board = board;
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
		for(int i = 0;i < Board.WIDTH;i++){
			g2.drawLine((int)((float)(w*i)/Board.WIDTH), 0, (int)((float)(w*i)/Board.WIDTH), h);
		}
		for(int i = 0;i < Board.HEIGHT;i++){
			g2.drawLine(0, (int)((float)(h*i)/Board.HEIGHT), w, (int)((float)(h*i)/Board.HEIGHT));
		}
		
		for(int i=0; i< Board.WIDTH; i++){
			for(int j=0; j<Board.HEIGHT; j++){
				if(board.getPoint(i, j) != Board.EMPTY){
					g.setColor(Board.COLORS[board.getPoint(i, j)]);
					g2.fillRect((int)((float)(w*(i))/Board.WIDTH), (int)((float)(h*(j))/Board.HEIGHT), w/Board.WIDTH+1, h/Board.HEIGHT+1);
				}
			}
		}
	}
}
