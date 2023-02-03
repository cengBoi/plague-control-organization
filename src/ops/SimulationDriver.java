package ops;

import java.awt.EventQueue;

import gui.LoginWindow;

public class SimulationDriver {
	public static void main() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginWindow frame = new LoginWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
