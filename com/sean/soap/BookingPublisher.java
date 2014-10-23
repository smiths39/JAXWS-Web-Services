package com.sean.soap;

import javax.xml.ws.Endpoint;
import com.sean.soap.BookingServer;
 
public class BookingPublisher {
 
	public static void main(String [] args) {
		
		// Create endpoint web service connection
		Endpoint endPoint = Endpoint.publish("http://localhost:8787/seansmith/soap", new BookingServer());
		
		// A verification of that connection was established is printed
		System.out.println("Connection established: " + endPoint.isPublished());
    }
}