package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ops.GameController;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class LoginWindow extends JFrame {
	private JPanel contentPane;
	private JPasswordField passwordField;
	private JTextField userName;
	private GameController gController = GameController.getInstance();	
	

	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
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

	/**
	 * Create the frame.
	 */
	public LoginWindow() {
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
		
		JLabel popupPosition = new JLabel("");
		popupPosition.setBounds(572, 256, 46, 14);
		contentPane.add(popupPosition);
		
		passwordField = new JPasswordField();
		
		passwordField.setBounds(283, 181, 155, 20);
		contentPane.add(passwordField);
		
		userName = new JTextField();
		userName.setBounds(283, 150, 155, 20);
		contentPane.add(userName);
		userName.setColumns(10);
		
		JLabel lblLogn = new JLabel("LOGIN");
		lblLogn.setFont(new Font("Tahoma", Font.PLAIN, 19));
		lblLogn.setBounds(329, 93, 55, 46);
		contentPane.add(lblLogn);
		
		JButton loginAsAdminButton = new JButton("Login as Admin");
		loginAsAdminButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean success = gController.loginAsAdmin(userName, passwordField);
				if(success) {
						if (success) {
							dispose();
							LoadNewWindow frame= new LoadNewWindow();
							frame.setVisible(true);
						}
				}
				else {
					JOptionPane.showMessageDialog(popupPosition, "Wrong username or password. Please try again!");
				}
			
			}
		});
		loginAsAdminButton.setBounds(283, 224, 155, 23);
		contentPane.add(loginAsAdminButton);
		
		JButton loginAsOverseerButton = new JButton("Login as Overseer");
		loginAsOverseerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			 	JTextField xField = new JTextField(5);
			    
			 	JPanel myPanel = new JPanel();
			    myPanel.add(new JLabel("World ID:"));
			    myPanel.add(xField);

			    int result = JOptionPane.showConfirmDialog(popupPosition, myPanel,
			        "ENTER WORLD ID:", JOptionPane.OK_CANCEL_OPTION);
			    
			    boolean success = gController.loginAsOS(userName, passwordField);
			    int input;
			    
			    if(result == 0) {
			    	if (success) {
			    		try {
					    	input = Integer.parseInt(xField.getText());
					    	success = gController.loadSave(input, "overseer");
							if (success) {
								dispose();
								MainWindow frame = new MainWindow("overseer");
								frame.setVisible(true);
							}
					    }
					    catch(Exception exc) {
					    	JOptionPane.showMessageDialog(popupPosition, "Please enter a valid world id.");
					    }
					}
			    	else {
			    		JOptionPane.showMessageDialog(popupPosition, "Wrong username or password.");
			    	}
				    
			    }
			    

			}
		});
		loginAsOverseerButton.setBounds(283, 256, 155, 23);
		contentPane.add(loginAsOverseerButton);
		
		JButton registerButton = new JButton("Register");
		registerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				RegisterWindow frame = new RegisterWindow();
				frame.setVisible(true);
			}
		});
		registerButton.setBounds(283, 336, 155, 23);
		contentPane.add(registerButton);
		
		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		exitButton.setBounds(283, 370, 155, 23);
		contentPane.add(exitButton);
	}
}
