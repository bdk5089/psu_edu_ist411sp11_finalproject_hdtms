/*
	Filename: ClientUpdateTicketHandler.java
	Classname: ClientUpdateTicketHandler
	Comments: Handler that updates the ticket information.
*/

/** The ClientUpdateTicketHandler updates ticket information when Submit button is pressed on the
*	ClientTicketDialog object.
*	@author Eric So, Bruce Kennedy
*	@version 1.0
*/

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.rmi.RemoteException; 


public class ClientUpdateTicketHandler implements ActionListener {
	
	private ClientActiveTicketsFrame owner;
	private User clientUser;
	private Ticket ticket;
	private ClientTicketDialog clientTicketDialog;
	private TicketServer ticketServerObject;
	
	/**
	*	ClientUpdateTicketHandler(User clientUser, String ticketID, HashMap<String, Ticket> activeTickets, TicketServer ticketServerObject, ClientTicketDialog clientTicketDialog)
	*	@param clientUser is the User associated with the Client object leading to the instantiation of this ClientDisplayTicketHandler.
	*	@param ticketID is a String representation of the ID number associated with this JDialog.
	*	@param activeTickets is the HashMap containing the active tickets, passed in from the Client.
	*	@param ticketServerObject is the RMI object representing the server. It will be used for callbacks to update the Ticket.
	*	@param clientTicketDialog is the JDialog that has the fields containing text with which to update the Ticket. 
	*/
	public ClientUpdateTicketHandler(ClientActiveTicketsFrame owner, User clientUser, Ticket ticket, ClientTicketDialog clientTicketDialog, TicketServer ticketServerObject) {
		this.owner = owner;
		this.clientUser = clientUser;
		this.ticket = ticket;
		this.clientTicketDialog = clientTicketDialog;
		this.ticketServerObject = ticketServerObject;
	}
	
	public void actionPerformed(ActionEvent evt) {
		// Get button clicked
		JButton buttonClicked = (JButton) evt.getSource();
		String actionCommand = new String(buttonClicked.getActionCommand());
		
		if (actionCommand.equals("Submit")) {
			System.out.println(clientUser.getLogon() + ": updating ticket...");
			
			// Recover ticket and update it
			Ticket ticketToUpdate = ticket;
			ticketToUpdate.setDesc(clientTicketDialog.getSummaryDescriptionField());
			ticketToUpdate.setResolution(clientTicketDialog.getResolutionDescriptionField());
			
			// Call checkInTicket() on the RMI object to update the ticket on the server
			try {
				ticketServerObject.checkInTicket(ticketToUpdate);
				// Refresh the activeTickets HashMap
				owner.getActiveTickets();
			} catch (RemoteException re) {
				System.out.println(re.getMessage());
			}
					
			
			// Close the ClientTicketDialog
			clientTicketDialog.setVisible(false);
			clientTicketDialog.dispose();
		}
	}
}