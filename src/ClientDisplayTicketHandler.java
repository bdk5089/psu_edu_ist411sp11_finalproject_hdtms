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
import java.rmi.RemoteException; 

public class ClientDisplayTicketHandler extends MouseAdapter {
	
	private User clientUser;
	private HashMap<String, Ticket> activeTickets;
	private TicketServer ticketServerObject;
	private ClientActiveTicketsFrame clientActiveTicketsFrame;
	
	private int numberOfClicks;
	
	/**
	*	ClientDisplayTicketHandler(String clientUser, HashMap<String, Ticket> activeTickets)
	*	@param clientUser is the User associated with the Client object leading to the instantiation of this ClientDisplayTicketHandler.
	*	@param activeTickets is the HashMap containing the active tickets, passed in from the Client.
	*	@param ticketServerObject is the RMI object representing the server. It will be used for callbacks to update the Ticket.
	*/
	public ClientDisplayTicketHandler(User clientUser, HashMap<String, Ticket> activeTickets, TicketServer ticketServerObject, ClientActiveTicketsFrame clientActiveTicketsFrame) {
		super();
		
		this.clientUser = clientUser;
		this.activeTickets = activeTickets;
		this.ticketServerObject = ticketServerObject;
		this.clientActiveTicketsFrame = clientActiveTicketsFrame;
	}
	
	public void mouseClicked(MouseEvent evt) {
		// Get the source of the clicks
		
		String source = evt.getSource().getClass().getSimpleName();
		
		if (source.equals("JList")){
			
			JList list = (JList) evt.getSource();
			
			// Get the number of clicks
			numberOfClicks = evt.getClickCount();
			
			// Check for a double-click
			if (numberOfClicks == 2) {
				int index = list.locationToIndex(evt.getPoint());
				
				// Get the ticketID by grabbing the object representing the ID nubmer, casting to Integer and getting the int value
				Ticket selected = (Ticket) list.getModel().getElementAt(index);
				Integer ticketID = selected.getID();
				System.out.println("Displaying ticket " + index + ": " + ticketID.toString());
				
				// Instantiate a JDialog displaying the ticket information and allowing the user to modify it
				ClientTicketDialog currentTicketDialog = new ClientTicketDialog(null, clientUser, ticketID.toString(), activeTickets, ticketServerObject);
				
				// Call checkOutTicket on the RMI object using the method invocation that passes the Ticket object
				try {
					ticketServerObject.checkOutTicket(clientUser.getLogon(), selected);
				} catch (RemoteException re) {
					System.out.println(re.getMessage());
				}
				
				currentTicketDialog.setVisible(true);
				
				// TODO add a takenTickets hashmap that takes the ticketID and the Ticket object; represents the tickets taken by the client
			}
		} else if (source.equals("JButton")){
			System.out.println("New Button Clicked");
			// Instantiate a new NewTicketDialog class
			ClientNewTicketDialog clientNewTicketDialog = new ClientNewTicketDialog(null, clientUser, activeTickets, ticketServerObject);
			clientNewTicketDialog.setVisible(true);
		}
	}
}