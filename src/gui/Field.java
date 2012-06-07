package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import javax.swing.JPanel;

public class Field extends JPanel{

	/**
	 * Fieldクラス
	 * メインバトルフィールドを描画するクラス
	 */
	private static final long serialVersionUID = 1L;
	@Override
	public void paintComponent(Graphics g) {
	super.paintComponent(g);
	Graphics2D g2 = (Graphics2D)g;
	int w = this.getWidth();
	int h = this.getHeight();
	for(int i = 0;i < 10;i++){
		Ellipse2D shape = new Ellipse2D.Double(0,0,w,h - i * (w / 10));
		g2.setPaint(new Color(0,0,255,25));
		g2.fill(shape);
		}
	}
}
