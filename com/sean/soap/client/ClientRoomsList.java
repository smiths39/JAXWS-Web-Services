package com.sean.soap.client;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import com.sean.soap.BookingInterface;

public class ClientRoomsList extends JFrame {

	public static BookingInterface bookingInterface;

	public ClientRoomsList(BookingInterface newBookingInterface) {
		
		try {
			bookingInterface = newBookingInterface;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		initialiseUI();	
	} 
	
	// Initialise all attributes to appear in Java Swing GUI
	private void initialiseUI() {
		
		JLabel listTitle = new JLabel("Available Rooms");
		listTitle.setFont(new Font("Arial", Font.BOLD, 13));
		listTitle.setHorizontalAlignment(SwingConstants.CENTER);
			
		JMenuBar menuBar = new JMenuBar();
		setMenuStyle(menuBar);

		CapturedPanel capturedPanel = new CapturedPanel();		
		retrieveRoomList(capturedPanel);

		this.add(listTitle);
		this.setJMenuBar(menuBar);
		this.add(capturedPanel, BorderLayout.EAST);
		
		setFrameAttributes();
	}
	
	// Retrieves and displays all room's and their corresponding capacity's defined within the Room_Capacity text file	
	private void retrieveRoomList(CapturedPanel capturedPanel) {

		PrintStream printStream = System.out;
		System.setOut(new PrintStream(new CapturedText("", capturedPanel, printStream)));
				
		System.out.println("ROOM  |  CAPACITY");
		System.out.println("=================");
		
		// Retrieve all rooms and capacities
		ArrayList<String> roomList = bookingInterface.getAllRoomsAndCapacities();
		
		int count = 0;
		
		for (String room : roomList) {
		
			if (count % 2 == 0) {
				System.out.print(room);
			} else {
				System.out.println(room);
			}
			count++;
		}
	}
	
	private void setFrameAttributes() {
		
		this.setTitle("Web Services System");	
		this.setResizable(false);
		this.setSize(360, 360);
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
	
	public static void main(String [] args) { 
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			new ClientRoomsList(bookingInterface);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}