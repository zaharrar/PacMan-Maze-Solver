package be.evasion.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import be.evasion.gui.sprite.SpriteManager;
import be.evasion.gui.util.GUIUtils;
import be.evasion.maze.Direction;
import be.evasion.util.Index;

public class MazeFrame extends JFrame {
	
	private static final long serialVersionUID = -7570849659919991236L;
	private MazePanel mazePanel;
	private JPanel content;
	private SpriteManager spriteManager;
	private LoadingThread thread;
	private Font font;
	private JLabel label;
	public MazeFrame(){
		this.spriteManager = new SpriteManager();
		this.mazePanel = new MazePanel(spriteManager);
		this.thread = new LoadingThread();
		this.font = new Font("Comic Sans MS", Font.BOLD, 30);
		this.setTitle("Pakkuman");
		this.label = new JLabel("Loading.");
		this.label.setFont(font); 
		this.content = new JPanel(new BorderLayout());
		this.content.add(label, BorderLayout.NORTH);
		this.content.add(mazePanel, BorderLayout.SOUTH);
		this.setContentPane(content);
		this.putMiddleScreen();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
	}
	private void putMiddleScreen(){
		int x = (int)(GUIUtils.getScreenSize().getWidth()/4);
		int y = (int)(GUIUtils.getScreenSize().getHeight()/4);
		//setLocation(x, y);
		setLocation(0, 0);
	}
	public void movePakkuman(Direction direction, Index newPakkumanLocation){
		while(!GUIUtils.isAlign(spriteManager.getPakkumanSprite(), newPakkumanLocation)){
			spriteManager.movePakkumanSprite(direction);
			repaint();
			try {
				Thread.sleep(6);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public void startLoading(){
		thread.setLoading(true);
		label.setForeground(Color.BLUE);
		label.setText("Loading ..");
		repaint();
	}
	public void stopLoading(){
		thread.setLoading(true);
		repaint();
	}
	public void pathFound(){
		label.setForeground(Color.GREEN);
		label.setText("Path Found !");
		repaint();
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public void pathNotFound(){
		label.setForeground(Color.RED);
		label.setText("ERROR 404 PATH NOT FOUND !");
		repaint();
	}
	public class LoadingThread extends Thread{
		private boolean loading;
		@Override
		public void run() {
			while(loading){
				MazeFrame.this.repaint();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		public boolean isLoading() {
			return loading;
		}
		public void setLoading(boolean loading) {
			this.loading = loading;
		}		
	}
}
