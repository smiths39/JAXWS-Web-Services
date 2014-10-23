package com.sean.soap.client;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
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

public class ClientBookingTimeTable extends JFrame {

    private JLabel roomNameLabel;
    private JComboBox roomNameBox;
	private JButton submitButton;
    private JPanel topPanel, bottomPanel;
	
	private CapturedPanel capturedPanel;
	
	static BookingInterface bookingInterface = null;
																								
	private String [] daysOfWeek = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
	
	public ClientBookingTimeTable(BookingInterface newBookingInterface) {

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
		
		roomNameLabel = new JLabel("Room Name: ");
		submitButton = new JButton("Submit");
        roomNameBox = new JComboBox();
		
		submitButton.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent event) {

				capturedPanel = new CapturedPanel();
				
				// Get selected room name
				String submitRoomName = roomNameBox.getSelectedItem().toString();
				
				// Retrieve timetable for specified room
				retrieveRoomTimeTable(capturedPanel, submitRoomName);		

				add(capturedPanel, BorderLayout.SOUTH);
				
				// Disable submission button
				submitButton.setEnabled(false);
			}
		});
		
        this.add(createCenterPanel(), "Center");
        this.add(new JPanel(), "West");
        this.add(new JPanel(), "East");
        this.setJMenuBar(menuBar);
		
		setFrameAttributes();
	}
	
	private void setFrameAttributes() {
		
		this.setTitle("Web Services System");
        this.setResizable(false);
		this.setSize(360, 360);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
	}
	
	// Populate combo box with retrieved room names
	private void addExistingRoomNames(JComboBox roomNames) {
		
		ArrayList<String> roomNameList = bookingInterface.getAllRooms();
		
		for (String name : roomNameList) {
			roomNameBox.addItem(name);		
		}
	}
	
	// Retrieve and display timetable for a specified room
	private void retrieveRoomTimeTable(CapturedPanel capturedPanel, String retrievedRoomName) {
		
		PrintStream printStream = System.out;
		System.setOut(new PrintStream(new CapturedText("", capturedPanel, printStream)));
		
		displayTimeSlot();

		int room = Integer.parseInt(retrievedRoomName.replaceAll("[^0-9]", ""));
	
		displayTimeTable(bookingInterface.bookingTimeTable(retrievedRoomName), room);
	}
	
	// Display time format in print layout
	private void displayTimeSlot() {
		
		System.out.format("%2s %-14s" ,"TIME", " ");
		
		for (int amIndex = 9; amIndex <= 12; amIndex++) {
			System.out.format("%4s", amIndex);
		}
		
		for (int pmIndex = 1; pmIndex <= 8; pmIndex++) {
			System.out.format("%5s", pmIndex);
		}
		
		System.out.println();
	}
	
	// Display timetable for a specified room
	private void displayTimeTable(int roomTimeTable[][][], int room) {
	
		for (int dayIndex = 0; dayIndex < 7; dayIndex++) {
	
			if (daysOfWeek[dayIndex].equals("Wednesday")) {
				System.out.format("%-12s", daysOfWeek[dayIndex]);
			} else if (daysOfWeek[dayIndex].equals("Friday")) { 
				System.out.format("%-20s", daysOfWeek[dayIndex]);
			} else {
				System.out.format("%-16s", daysOfWeek[dayIndex]);
			}
			
			for (int timeIndex = 0; timeIndex < 12; timeIndex++) {
				
				String roomNotation = Integer.toString(roomTimeTable[room][dayIndex][timeIndex]);
				
				if (roomNotation.equals("1")) {
					System.out.printf("%6s", "X ");
				} else {
					System.out.printf("%6s", "- ");
				}
			}
			System.out.println();
	    }
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
	
	private JPanel createCenterPanel() {
	
		addExistingRoomNames(roomNameBox);			
		
		topPanel = new JPanel();

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(15,15,15,0);
        topPanel.add(roomNameLabel,gbc);

        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(15,35,15,0);
        topPanel.add(roomNameBox,gbc);

		gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(15,15,15,0);
        topPanel.add(submitButton,gbc);
		
        topPanel.setBorder(BorderFactory.createTitledBorder("Room"));

        topPanel.setPreferredSize(topPanel.getPreferredSize());
        topPanel.validate();

        return topPanel;
    }
    
    public static void main(String[]args) {
    	
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            new ClientBookingTimeTable(bookingInterface);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}