package gui;

import javax.swing.Box;
import javax.swing.ImageIcon;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.sql.Connection;
import java.sql.SQLException;

import ops.City;
import ops.DBLayer;
import ops.GameController;
import ops.HQ;
import ops.World;

import javax.swing.JProgressBar;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

public class MainWindow extends JFrame {
	ImageIcon imageWorld = new ImageIcon("world_temp.png");
	private JPanel contentPane;
	private JTable table;
	private JScrollPane openPane = null;
	private GameController gController = GameController.getInstance();
	private Connection connection;
	
	public JScrollPane constructTable(String[] header, DefaultTableModel model) {
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(128, 99, 452, 182);
		contentPane.add(scrollPane);
		
		JTable table = new JTable(model);
		
		table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

		scrollPane.setViewportView(table);
		return scrollPane;
	}

	/**
	 * Create the frame.
	 */
	public MainWindow(String user) {
		connection = DBLayer.getInstance().connect();
		setTitle("Plague Control Organization");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 771, 463);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel cureRateLabel = new JLabel("+2.0");

		cureRateLabel.setBounds(9, 25, 46, 14);
		contentPane.add(cureRateLabel);
		
		JLabel infectionRateLabel = new JLabel("+4.0");
		infectionRateLabel.setBounds(10, 59, 45, 14);
		contentPane.add(infectionRateLabel);
		
		JLabel lblDay = new JLabel("Day:");
		lblDay.setBounds(9, 173, 24, 14);
		contentPane.add(lblDay);
		
		JLabel lblDayValue = new JLabel("1");
		lblDayValue.setBounds(41, 173, 67, 14);
		contentPane.add(lblDayValue);
		
		JLabel populationLabel = new JLabel("POPULATION");
		populationLabel.setText(String.valueOf(gController.world.worldPopulation));
		populationLabel.setBounds(41, 123, 67, 14);
		contentPane.add(populationLabel);
		
		JLabel infectedLabel = new JLabel("INFECTED");
		infectedLabel.setText(String.valueOf(gController.world.totalInfectedPeople));
		infectedLabel.setBounds(41, 148, 67, 14);
		contentPane.add(infectedLabel);
		
		JLabel moneyLabel = new JLabel("MONEY");
		moneyLabel.setText(String.valueOf(gController.world.organization.currentMoney));// sahip olunan para bu metodla güncellenebilir
		moneyLabel.setBounds(41, 99, 67, 14);
		contentPane.add(moneyLabel);
		
		JLabel popupPosition = new JLabel("");
		popupPosition.setBounds(311, 351, 46, 14);
		contentPane.add(popupPosition);
		
		JProgressBar cureProgress = new JProgressBar();
		cureProgress.setValue(30);
		cureProgress.setBounds(65, 25, 515, 14);
		contentPane.add(cureProgress);
		
		JProgressBar infectionProgress = new JProgressBar();
		infectionProgress.setBounds(65, 59, 515, 14);
		infectionProgress.setValue(50);
		contentPane.add(infectionProgress);
		
		
		JLabel cureProgressLabel = new JLabel("CURE PROGRESS");
		cureProgressLabel.setBounds(252, 11, 176, 14);
		contentPane.add(cureProgressLabel);
		
		JLabel infectionProgressLabel = new JLabel("INFECTION PROGRESS");
		infectionProgressLabel.setBounds(239, 42, 168, 14);
		contentPane.add(infectionProgressLabel);
		
		JButton routesButton = new JButton("Routes");
		routesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(openPane == null) {// soldaki butonlara tekrar basýnca kapatmak için if bloðu
					DefaultTableModel model = new DefaultTableModel();//model e header atayýp her bir row a liste ekliyoruz,tabloya eklemeye hazýr oluyor
					
					String[] header = new String[] {"IS ON ROUTE","REMAINING","START TIME","RATE CHANGE","DESTINATION","VEHICLE","HQ NAME"};
					model.setColumnIdentifiers(header);
					
					try {
						var statement = connection.createStatement();
						var resultSet = statement.executeQuery("select * from Routes");
						while (resultSet.next()) {
							var array = new String[7];
							array[0] = resultSet.getString("IsOnRoute");
							array[1] = String.valueOf(resultSet.getInt("RemainingDay"));
							array[2] = String.valueOf(resultSet.getInt("InfectionRateChange"));
							array[3] = String.valueOf(resultSet.getInt("MonetaryGain"));
							array[4] = resultSet.getString("Destination");
							array[5] = resultSet.getString("Vehicle");
							array[6] = resultSet.getString("HQ_Name");
							model.addRow(array);
						}
						resultSet.close();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					openPane = constructTable(header,model);
				}
				else {
					openPane.setVisible(false);
					openPane = null;
				}
			}
		});
		routesButton.setBounds(9, 304, 89, 23);
		contentPane.add(routesButton);
		
		JButton vehiclesButton = new JButton("Vehicles");
		vehiclesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(openPane == null) {
					DefaultTableModel model = new DefaultTableModel();
					
					String[] header = new String[] {"NAME","SPEED","PRICE","SUPPLYCAP","IS BOUGHT","HQ ID"};
					model.setColumnIdentifiers(header);
					
					try {
						var statement = connection.createStatement();
						var resultSet = statement.executeQuery("select * from Vehicles");
						while (resultSet.next()) {
							var array = new String[6];
							array[0] = resultSet.getString("Name");
							array[1] = String.valueOf(resultSet.getInt("Speed"));
							array[2] = String.valueOf(resultSet.getInt("Price"));
							array[3] = String.valueOf(resultSet.getInt("SupplyCap"));
							array[4] = resultSet.getString("IsBought");
							array[5] = String.valueOf(resultSet.getInt("HQ_Id"));
							model.addRow(array);
						}
						resultSet.close();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					for(int i = 0; i < 10;i++) {
						model.addRow(new String[10]);
					}
					openPane = constructTable(header,model);
				}
				else {
					openPane.setVisible(false);
					openPane = null;
				}
			}
		});
		vehiclesButton.setBounds(9, 270, 89, 23);
		contentPane.add(vehiclesButton);
		
		JButton hqsButton = new JButton("HQ's");
		hqsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(openPane == null) {
					DefaultTableModel model = new DefaultTableModel();
					
					String[] header = new String[] {"NAME","STATE ID","IS NEAR COAST","ID","IS BOUGHT"};
					model.setColumnIdentifiers(header);
					
					
					try {
						var statement = connection.createStatement();
						var resultSet = statement.executeQuery("select * from HQ");
						while (resultSet.next()) {
							var array = new String[5];
							array[0] = resultSet.getString("Name");
							array[1] = String.valueOf(resultSet.getInt("StateID"));
							array[2] = resultSet.getString("IsNearCoast");
							array[3] = String.valueOf(resultSet.getInt("Id"));
							array[4] = resultSet.getString("IsBought");
							model.addRow(array);
						}
						resultSet.close();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					

					openPane = constructTable(header,model);
				}
				else {
					openPane.setVisible(false);
					openPane = null;
				}
			}
		});
		hqsButton.setBounds(9, 236, 89, 23);
		contentPane.add(hqsButton);
		
		
		JButton citiesButton = new JButton("Cities");
		
		
		citiesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(openPane == null) {
					DefaultTableModel model = new DefaultTableModel();
					String[] header = new String[] {"NAME","STATE ID","IS NEAR COAST","POPULATION","INFECTED PEOPLE","PERCENT"};
					model.setColumnIdentifiers(header);
					
					for (City city : gController.world.cities) {
						var array = new String[6];
						array[0] = city.name;
						array[1] = String.valueOf(city.stateID);
						if (city.isNearCoast) 
							array[2] = "Yes";
						else 
							array[2] = "No";
						array[3] = String.valueOf(city.population);
						array[4] = String.valueOf(city.infectedPeople);
						array[5] = String.valueOf(city.infectedPeoplePercent);
						model.addRow(array);
					}
					
					openPane = constructTable(header,model);
				}
				else {
					openPane.setVisible(false);
					openPane = null;
				}
				
			}
		});
		citiesButton.setBounds(9, 202, 89, 23);
		contentPane.add(citiesButton);

		if(user.equalsIgnoreCase("admin")) {
			
			JButton buyHqButton = new JButton("BUY HQ");
			buyHqButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					 	JTextField hqField = new JTextField(5);
					    
					 	JPanel myPanel = new JPanel();
					    myPanel.add(new JLabel("HQ ID:"));
					    myPanel.add(hqField);

					    int result = JOptionPane.showConfirmDialog(popupPosition, myPanel,
					        "BUY HEADQUARTER", JOptionPane.OK_CANCEL_OPTION);
					    if (result == 0) {
					    	String HQ_Id = hqField.getText();
						    
						    try {
						    	gController.world.organization.buyHQ(Integer.parseInt(HQ_Id));
								
						    }
						    catch(Exception exc) {
						    	JOptionPane.showMessageDialog(popupPosition, "Please enter a valid hq id.");
						    }
						}
				}
			});
			buyHqButton.setBounds(605, 129, 140, 23);
			contentPane.add(buyHqButton);
			
			JButton buyVehicleButton = new JButton("BUY VEHICLE");
			buyVehicleButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JTextField hqField = new JTextField(5);
				    JTextField vehNameFile = new JTextField(5);
				    
				 	JPanel myPanel = new JPanel();
				    myPanel.add(new JLabel("Hq Id:"));
				    myPanel.add(hqField);
				    myPanel.add(Box.createVerticalStrut(15));
				    myPanel.add(new JLabel("Vehicle Name:"));			    
				    myPanel.add(vehNameFile);
				    myPanel.add(Box.createVerticalStrut(15));

				    int result = JOptionPane.showConfirmDialog(popupPosition, myPanel,
				        "BUY VEHICLE", JOptionPane.OK_CANCEL_OPTION);
				    if (result == 0) {
				    	try {
				    		String hqId = hqField.getText();//fonksiyonlar için bu deðiþkenleri kullan
						    String vehName = vehNameFile.getText();
						    
						    boolean success = gController.world.organization.buyVehicles(Integer.parseInt(hqId), vehName);
						    if (!success)
						    	JOptionPane.showMessageDialog(popupPosition, "Vehicle could not be bought.");
						    else {
						    	JOptionPane.showMessageDialog(popupPosition, "Vehicle bought.");
							}
					    }
					    catch(Exception exc) {
					    	JOptionPane.showMessageDialog(popupPosition, "Please enter an integer.");
					    }
					}
				}
			
			});
			buyVehicleButton.setBounds(605, 163, 140, 23);
			contentPane.add(buyVehicleButton);
			
			JButton advanceCureButton = new JButton("ADVANCE CURE");
			advanceCureButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					JTextField donField = new JTextField(5);
				    
				 	JPanel myPanel = new JPanel();
				    myPanel.add(new JLabel("Money:"));
				    myPanel.add(donField);
				    
					int result = JOptionPane.showConfirmDialog(popupPosition, myPanel,
					        "DONATE MONEY", JOptionPane.OK_CANCEL_OPTION);
					
					if (result == 0) {
						String donatedMoney  = donField.getText();
						try {
					    	gController.world.organization.advanceCure(Integer.parseInt(donatedMoney));
							
					    }
					    catch(Exception exc) {
					    	JOptionPane.showMessageDialog(popupPosition, "Please enter an integer.");
					    }
					}
				}
			});
			advanceCureButton.setBounds(605, 95, 140, 23);
			contentPane.add(advanceCureButton);
			
			JButton saveButton = new JButton("SAVE");
			saveButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						gController.save();
						moneyLabel.setText(String.valueOf(gController.world.organization.currentMoney));
						cureRateLabel.setText("+" + String.valueOf(Math.floor(gController.world.organization.cure.cureRate * 10) / 10));
						infectedLabel.setText(String.valueOf(gController.world.totalInfectedPeople));
						lblDayValue.setText(String.valueOf(gController.world.day));
					} catch (SQLException | InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					JOptionPane.showMessageDialog(popupPosition, "Save successfull.");
				}
			});
			saveButton.setBounds(616, 322, 115, 23);
			contentPane.add(saveButton);
			
			JButton createRouteButton = new JButton("CREATE ROUTE");
			createRouteButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JTextField hqField = new JTextField(5);
				    JTextField cityField = new JTextField(5);
				 	JTextField vehField = new JTextField(5);
				    
				 	JPanel myPanel = new JPanel();
				    myPanel.add(new JLabel("HQ Id:"));
				    myPanel.add(hqField);
				    myPanel.add(Box.createVerticalStrut(15));
				    myPanel.add(new JLabel("Destination City:"));			    
				    myPanel.add(cityField);
				    myPanel.add(Box.createVerticalStrut(15));
				    myPanel.add(new JLabel("Vehicle Name: "));
				    myPanel.add(vehField);

				    int result = JOptionPane.showConfirmDialog(popupPosition, myPanel,
				        "BUY HEADQUARTER", JOptionPane.OK_CANCEL_OPTION);
				    
				    if (result == 0) {
						try {
							String hqIDStr  = hqField.getText();
							int hqID = Integer.parseInt(hqIDStr);
						    String destinationCity = cityField.getText();
						    String vehicleStr = vehField.getText();
						    boolean success = gController.routeHandler(hqID, destinationCity, vehicleStr);
						    if (!success)
						    	JOptionPane.showMessageDialog(popupPosition, "Route could not be created.");
						    else {
						    	JOptionPane.showMessageDialog(popupPosition, "Route created.");
							}
						    
					    }
					    catch(Exception exc) {
					    	JOptionPane.showMessageDialog(popupPosition, "Please enter an integer.");
					    }
						
				    	
					}
				}
			});
			createRouteButton.setBounds(605, 197, 140, 23);
			
			contentPane.add(createRouteButton);

			
			JButton nextDayButton = new JButton("NEXT DAY");
			nextDayButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					JOptionPane dayPane = new JOptionPane();
					dayPane.showMessageDialog(popupPosition, "Day " + gController.world.day);
					
					if(cureProgress.getValue() + gController.world.organization.cure.cureRate >= 100) {
						JOptionPane.showMessageDialog(popupPosition, "SIMULATION IS OVER. THE CURE HAS BEEN DEVELOPED");

						dispose();
						LoginWindow frame = new LoginWindow();
						frame.setVisible(true);
					}
					else if(infectionProgress.getValue() + gController.world.infectionRate >= 100) {
						JOptionPane.showMessageDialog(popupPosition, "SIMULATION IS OVER. THE INFECTION HAS SPREAD");

						dispose();
						LoginWindow frame = new LoginWindow();
						frame.setVisible(true);
					}
					else {
						infectionProgress.setValue(infectionProgress.getValue() + gController.world.infectionRate);
						cureProgress.setValue(cureProgress.getValue() + (int)gController.world.organization.cure.cureRate);
					}
					
					gController.world.increaseDay();
				}
			});
			nextDayButton.setBounds(616, 25, 115, 48);
			contentPane.add(nextDayButton);
		}
		
		
		JButton mainMenuButton = new JButton("MAIN MENU");
		mainMenuButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				LoginWindow frame = new LoginWindow();
				frame.setVisible(true);
			}
		});
		
		mainMenuButton.setBounds(616, 356, 115, 23);
		contentPane.add(mainMenuButton);
		
		JButton exitButton = new JButton("EXIT");
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		exitButton.setBounds(616, 390, 115, 23);
		contentPane.add(exitButton);

		
		JLabel lblCur = new JLabel("Cur:");
		lblCur.setBounds(9, 99, 24, 14);
		contentPane.add(lblCur);
		
		JLabel lblPop = new JLabel("Pop:");
		lblPop.setBounds(9, 123, 34, 14);
		contentPane.add(lblPop);
		
		JLabel lblInf = new JLabel("Inf:");
		lblInf.setBounds(9, 148, 24, 14);
		contentPane.add(lblInf);
		


	}
}
