package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ops.GameController;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class LoadNewWindow extends JFrame {

	private JPanel contentPane;
	private GameController gController = GameController.getInstance();



	/**
	 * Create the frame.
	 */
	public LoadNewWindow() {
		setTitle("Plague Control Organization");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 771, 463);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel headLine = new JLabel("PLAGUE CONTROL ORGANIZATION");
		headLine.setFont(new Font("Tahoma", Font.PLAIN, 26));
		headLine.setBounds(179, 36, 414, 46);
		contentPane.add(headLine);
		
		JButton newWorldButton = new JButton("New World");
		newWorldButton.setFont(new Font("Tahoma", Font.PLAIN, 24));
		newWorldButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					gController.start();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				dispose();
				MainWindow frame = new MainWindow("admin");
				frame.setVisible(true);
			}
		});
		newWorldButton.setBounds(48, 156, 264, 145);
		contentPane.add(newWorldButton);
		
		JButton loadLastWorld = new JButton("Load Last World");
		loadLastWorld.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					gController.loadSave(0, "admin");
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				dispose();
				MainWindow frame = new MainWindow("admin");
				frame.setVisible(true);
			}
		});
		loadLastWorld.setFont(new Font("Tahoma", Font.PLAIN, 24));
		loadLastWorld.setBounds(400, 156,  264, 145);
		contentPane.add(loadLastWorld);
	}
}
