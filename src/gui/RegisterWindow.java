package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ops.GameController;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RegisterWindow extends JFrame {

	private JPanel contentPane;
	private JTextField userName;
	private JPasswordField passwordField;
	private GameController gController = GameController.getInstance();



	/**
	 * Create the frame.
	 */
	public RegisterWindow() {
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
		
		userName = new JTextField();
		userName.setColumns(10);
		userName.setBounds(293, 170, 155, 20);
		contentPane.add(userName);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(293, 201, 155, 20);
		contentPane.add(passwordField);
		
		JLabel lblRegster = new JLabel("REGISTER");
		lblRegster.setFont(new Font("Tahoma", Font.PLAIN, 19));
		lblRegster.setBounds(323, 113, 88, 46);
		contentPane.add(lblRegster);
		
		JButton btnLoginAsOverseer = new JButton("Register as Overseer");
		btnLoginAsOverseer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gController.register(userName, passwordField);
			}
		});
		btnLoginAsOverseer.setBounds(293, 232, 155, 23);
		contentPane.add(btnLoginAsOverseer);
		
		JButton btnReturnToLogin = new JButton("Return to Login");
		btnReturnToLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				LoginWindow frame = new LoginWindow();
				frame.setVisible(true);
			}
		});
		btnReturnToLogin.setBounds(293, 335, 155, 23);
		contentPane.add(btnReturnToLogin);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnExit.setBounds(293, 369, 155, 23);
		contentPane.add(btnExit);
	}

}
