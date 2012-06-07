package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.UIManager;

public class Window extends JFrame{

	private static final long serialVersionUID = 1L;
	
	public Window(Boolean isVisible){
		this.init();
		this.setVisible(isVisible);
	}
	
	public static void main(String[] args) {
		Window window = new Window(true);
	}

	private void init() {
		setTitle("Dreadnought");	// ウィンドウのタイトル
		initBounds();		// ウィンドウの位置の初期化
		initLookFeel();		// ウィンドウの外観の初期化
		setResizable(true);	// ウィンドウサイズを可変にする
		this.add(new Field(), BorderLayout.CENTER);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	/**
	 * 画面の中央にウィンドウを表示
	 */
	private void initBounds() {
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize(); //画面全体のサイズ
		int nx = 800;
		int ny = 600;
		int x = (d.width - nx) / 2;
		int y = (d.height - ny) / 2;
		setLocation(x, y);
		setSize(nx, ny);
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
	
}