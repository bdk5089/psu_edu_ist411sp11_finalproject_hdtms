/*
	Filename: ClientDisplayTicketHandler.java
	Classname: ClientDisplayTicketHandler
	Comments: Event handler that takes care of double-clicks on the active tickets list.
*/

/** The ClientDisplayTicketHandler handles double-clicks on the active tickets list.
*	@author Eric So, Bruce Kennedy
*	@version 1.0
*/

import java.net.*;
import java.util.*;

import java.awt.event.*;
import javax.swing.*;

public class ClientDisplayTicketHandler extends MouseAdapter {
	
	private String clientUsername;
	private HashMap<String, Ticket> activeTickets;
	
	private int numberOfClicks;
	
	public ClientDisplayTicketHandler(String clientUsername, HashMap<String, Ticket> activeTickets) {
		super();
		
		this.clientUsername = clientUsername;
		this.activeTickets = activeTickets;
	}
	
	public void mouseClicked(MouseEvent evt) {
		// Get the source of the clicks
		JList list = (JList) evt.getSource();
		
		// Get the number of clicks
		numberOfClicks = evt.getClickCount();
		
		// Check for a double-click
		if (numberOfClicks == 2) {
			int index = list.locationToIndex(evt.getPoint());
			
			// Get the ticketID by grabbing the object representing the ID nubmer, casting to Integer and getting the int value
			Integer ticketID = (Integer) list.getSelectedValue();
			System.out.println("Displaying ticket " + index + ": " + ticketID.toString());
			
			// Instantiate a JDialog displaying the ticket information and allowing the user to modify it
			ClientTicketDialog currentTicketDialog = new ClientTicketDialog(null, clientUsername, ticketID.toString(), activeTickets);
			
			// TODO add a takenTickets hashmap that takes the ticketID and the Ticket object; represents the tickets taken by the client
			
			tempDialog.setVisible(true);
		}
	}
}