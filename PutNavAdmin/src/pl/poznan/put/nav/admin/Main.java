package pl.poznan.put.nav.admin;

import java.awt.EventQueue;
import javax.swing.JFrame;

import pl.poznan.put.nav.admin.gui.MainFrame;

public class Main {

	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new MainFrame();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}
}
