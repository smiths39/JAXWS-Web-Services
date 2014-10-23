package com.sean.soap.client;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.sean.soap.BookingInterface;

public class ClientBookRoom extends JFrame {

	private JLabel roomNameLabel, dayOfWeekLabel, timeOfDayLabel, userLabel, resultLabel;
    private JComboBox roomNameBox, dayOfWeekBox, timeOfDayBox, userBox;
	private JButton validateButton;
    private JPanel centerPanel;
	
	static BookingInterface bookingInterface = null;
	
	private String [] daysOfWeek = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
	private String [] timeOfDay = { "9am", "10am", "11am", "12am", "1pm", "2pm", "3pm", "4pm", "5pm", "6pm", "7pm", "8pm" };
	private String resultLabelText = "Result: ";
	
	public ClientBookRoom(BookingInterface newBookingInterface) {

		try{
			bookingInterface = newBookingInterface;
		} catch (Exception e) {
			e.printStackTrace();
		}	

		initialiseUI();
	}

	// Initialise all attributes to appear in Java Swing GUI
	private void initialiseUI() {
	
		JMenuBar menuBar = new JMenuBar();
		setMenuStyle(menuBar);
		
		initialiseFrameVariables();
		
		validateButton.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent event) {

				String roomStatus = null;
				
				// Retrieve selections from combo boxes
				String submitRoomName = roomNameBox.getSelectedItem().toString();
				String submitDayOfWeek = dayOfWeekBox.getSelectedItem().toString();
				String submitTimeOfDay = timeOfDayBox.getSelectedItem().toString();
				
				int userID = (Integer) userBox.getSelectedItem();
				
				int dayValue = bookingInterface.getDayValue(submitDayOfWeek);
				int timeValue = bookingInterface.getTimeValue(submitTimeOfDay);
				
				// Book a room for a specified user
				roomStatus = bookingInterface.bookAvailableRoom(submitRoomName, dayValue, timeValue, userID);
				
				// If room is already booked, add user to booking queue
				if (roomStatus.equals("Room is already booked")) {
					String output = bookingInterface.addUserBookingQueue(submitRoomName, dayValue, timeValue, userID);
					resultLabel.setText(resultLabelText + output);					
				} else {
					resultLabel.setText(resultLabelText + roomStatus);
				}
			}
		});
		
        this.add(createCenterPanel(), "Center");
        this.add(new JPanel(), "West");
        this.add(new JPanel(), "East");
        this.setJMenuBar(menuBar);
		
		setFrameAttributes();		
	}
	
	private void initialiseFrameVariables() {
		
		roomNameLabel = new JLabel("Room Name: ");
        dayOfWeekLabel = new JLabel("Day: ");
        timeOfDayLabel = new JLabel("Time: ");
        userLabel = new JLabel("Username: ");
        resultLabel = new JLabel(resultLabelText);

        roomNameBox = new JComboBox();
        dayOfWeekBox = new JComboBox();
        timeOfDayBox = new JComboBox();
        userBox = new JComboBox();
        
		validateButton = new JButton("Submit Values");
	}
	
	private void setFrameAttributes() {
		
		this.setTitle("Web Services System");
        this.setResizable(false);
		this.setSize(420, 420);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	// Set menu headers	
	public void setMenuStyle(JMenuBar menuBar) {

		ImageIcon homeIcon = new ImageIcon("home.png");
		ImageIcon exitIcon = new ImageIcon("exit.png");
		
		JMenu file = new JMenu("File");
		JMenu services = new JMenu("Services");
		
		setFileMenuItem(file, homeIcon, exitIcon);
		setServicesMenuItem(services);

		menuBar.add(file);
		menuBar.add(services);

		setJMenuBar(menuBar);
	}
	
	// Initialise functionality of menu headers
	private void setFileMenuItem(JMenu file, ImageIcon homeIcon, ImageIcon exitIcon) {
		
		JMenuItem homeItem = new JMenuItem("Home", homeIcon);
		JMenuItem exitItem = new JMenuItem("Exit", exitIcon);
		
		homeItem.setToolTipText("Return to main menu");
		exitItem.setToolTipText("Exit application");
		
		homeItem.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent event) {
				
				setVisible(false);
				new BookingClient(bookingInterface);
			}
		});
		
		exitItem.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent event) {
				
				System.exit(0);
			}
		});
		
		file.add(homeItem);
		file.add(exitItem);
	}
	
	// Initialises and implements functionality of menu header items
	private void setServicesMenuItem(JMenu services) {
	
		JMenuItem menuListRooms = new JMenuItem("List All Rooms");
		JMenuItem menuAddUser = new JMenuItem("Add User");
		JMenuItem menuCheckAvailability = new JMenuItem("Check Room Availability");
		JMenuItem menuBookRoom = new JMenuItem("Book a Room");
		JMenuItem menuCancelBooking = new JMenuItem("Cancel Booking");
		JMenuItem menuNotifications = new JMenuItem("Notifications");
		JMenuItem menuRoomTimeTable = new JMenuItem("Display Room TimeTable");
		
		menuListRooms.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent event) {
				
				setVisible(false);
				new ClientRoomsList(bookingInterface);				
			}
		});

		menuAddUser.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				
				setVisible(false);
				new ClientAddUser(bookingInterface);				
			}
		});
		
		menuCheckAvailability.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent event) {

				setVisible(false);
				new ClientAvailableRoomPrompt(bookingInterface);
			}
		});
		
		menuBookRoom.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent event) {

				setVisible(false);
				new ClientBookRoom(bookingInterface);
			}
		});

		menuCancelBooking.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {

				setVisible(false);
				new ClientCancelBooking(bookingInterface);
			}
		});
		
		menuNotifications.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {

				setVisible(false);
				new ClientNotifications(bookingInterface);
			}
		});

		menuRoomTimeTable.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent event) {

				setVisible(false);
				new ClientBookingTimeTable(bookingInterface);
			}
		});
		
		services.add(menuListRooms);
		services.add(menuAddUser);
		services.add(menuCheckAvailability);
		services.add(menuBookRoom);
		services.add(menuCancelBooking);
		services.add(menuNotifications);
		services.add(menuRoomTimeTable);				
	}
	
	// Populate combo box with array contents
	private void addComboItems(JComboBox combo, String[] strArray) {
		
		for (int index = 0; index < strArray.length; index++) {
			
			combo.addItem(strArray[index]);
		}
	}
	
	// Populate combo box with existing rooms
	private void addExistingRoomNames(JComboBox roomNames) {
		
		ArrayList<String> roomNameList = bookingInterface.getAllRooms();
		
		for (String name : roomNameList) {
			roomNameBox.addItem(name);		
		}
	}
	
	// Populate combo box with existing users
	private void addExistingUsers(JComboBox usernames) {
		
		ArrayList<Integer> usernameList = bookingInterface.getAllUsers();
		
		if (usernameList.isEmpty()) {
			validateButton.setEnabled(false);
		} else {
			
			validateButton.setEnabled(true);
			for (Integer userNumber : usernameList) {
				userBox.addItem(userNumber);		
			}
		}
	}

    private JPanel createCenterPanel() {
	
		addExistingRoomNames(roomNameBox);			
		addComboItems(dayOfWeekBox, daysOfWeek);
		addComboItems(timeOfDayBox, timeOfDay);			
        addExistingUsers(userBox);
		
		centerPanel = new JPanel();

        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();

        centerPanel.setLayout(gbl);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(15,15,15,0);
        centerPanel.add(roomNameLabel,gbc);

        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(15,35,15,0);
        centerPanel.add(roomNameBox,gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(15,15,15,0);
        centerPanel.add(dayOfWeekLabel,gbc);

        gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(15,35,15,0);
        centerPanel.add(dayOfWeekBox,gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(15,15,15,0);
        centerPanel.add(timeOfDayLabel,gbc);

        gbc.gridx = 3;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(15,35,15,0);
        centerPanel.add(timeOfDayBox,gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(15,15,15,0);
        centerPanel.add(userLabel,gbc);

        gbc.gridx = 3;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(15,35,15,0);
        centerPanel.add(userBox,gbc);
        
		gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(15,15,15,0);
        centerPanel.add(validateButton,gbc);
		
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(50,0,0,0);
		gbc.gridwidth = 6;
        centerPanel.add(resultLabel,gbc);

        centerPanel.setBorder(BorderFactory.createTitledBorder("Book Room"));
       
        centerPanel.setPreferredSize(centerPanel.getPreferredSize());
        centerPanel.validate();

        return centerPanel;
    }
    
    public static void main(String[]args) {
    	
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            new ClientBookRoom(bookingInterface);
		} catch (Exception e) {
            e.printStackTrace();
        }
    }
}

