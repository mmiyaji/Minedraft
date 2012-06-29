package game;

import game.Board;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.UIManager;

public class Window extends JFrame{

	private static final long serialVersionUID = 1L;
	public Field field;
	public Window(Boolean isVisible){
		this.init(new Board());
		this.setVisible(isVisible);
	}
	public Window(Boolean isVisible, Board board){
		this.init(board);
		this.setVisible(isVisible);
	}
	@Override
	public void repaint(){
		super.repaint();
		field.repaint();
	}
	private void init(Board board) {
		setTitle("Minedraft");	// ウィンドウのタイトル
		initBounds();		// ウィンドウの位置の初期化
		initLookFeel();		// ウィンドウの外観の初期化
		setResizable(true);	// ウィンドウサイズを可変にする
		field = new Field(board);
		this.add(field, BorderLayout.CENTER);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	/**
	 * 画面の中央にウィンドウを表示
	 */
	private void initBounds() {
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize(); //画面全体のサイズ
		int w = d.width-50;
		int h = d.height-50;
		if(w > h){
		    w = h;
		}else{
		    h = w;
		}
		if(h > 900){
		    w = h = 900;
		}
		int x = (d.width - w) / 2;
		int y = (d.height - h) / 2;
		setLocation(x, y);
		setSize(w, h);
	}
	/**
	 * 外観をOSのものに似せる
	 */
	private void initLookFeel() {
		try {
			String look =
				// "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
				UIManager.getSystemLookAndFeelClassName();
			UIManager.setLookAndFeel(look);
		} catch (Exception e) {
			// 駄目なときは諦める
			e.printStackTrace();
		}
	}
	public void setBoard(Board board){
		field.setBoard(board);
	}
	public void paintArrow(Vector<float[]> arrows){
		field.paintArrow(arrows);
	}

}