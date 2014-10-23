package com.sean.soap.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import com.sean.soap.BookingInterface;

public class BookingClient extends JFrame {

	static BookingInterface bookingInterface;
	
	public static void main(String[] args) throws Exception {
		
		URL url = new URL("http://localhost:8787/seansmith/soap?wsdl");
		
        QName qname = new QName("http://soap.sean.com/", "BookingServerService");
        Service service = Service.create(url, qname);
 
        bookingInterface = service.getPort(BookingInterface.class);
        bookingInterface.createAllRooms();
        SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
	
				BookingClient bookingClient = new BookingClient(bookingInterface);				
			}
        });
	} 
	
	public BookingClient(BookingInterface newBookingInterface) {
		
		bookingInterface = newBookingInterface;
		initialiseUI();
	}
	
	private void initialiseUI() {

		JMenuBar menuBar = new JMenuBar();	
		setMenuStyle(menuBar);
		
		ImageIcon logo = new ImageIcon("webLogo.png");
		JLabel logoLabel = new JLabel(logo, SwingConstants.CENTER);
		
		add(logoLabel);
		
		setFrameAttributes();
	}
	
	private void setFrameAttributes() {
		
		setTitle("Web Services System");
		setSize(450, 450);
		setLocationRelativeTo(null);			
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

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
}
