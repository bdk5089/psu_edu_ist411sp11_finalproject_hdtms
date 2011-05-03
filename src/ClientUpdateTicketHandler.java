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


public class ClientUpdateTicketHandler implements ActionListener {
	
	private String clientUsername;
	private String ticketID;
	private HashMap<String, Ticket> activeTickets;
	private ClientTicketDialog clientTicketDialog;
	private TicketServer ticketServerObject;
	
	/**
	*	ClientUpdateTicketHandler(String clientUsername, String ticketID, HashMap<String, Ticket> activeTickets, TicketServer ticketServerObject, ClientTicketDialog clientTicketDialog)
	*	@param clientUsername is the username associated with the Client object leading to the instantiation of this ClientDisplayTicketHandler.
	*	@param ticketID is a String representation of the ID number associated with this JDialog.
	*	@param activeTickets is the HashMap containing the active tickets, passed in from the Client.
	*	@param ticketServerObject is the RMI object representing the server. It will be used for callbacks to update the Ticket.
	*	@param clientTicketDialog is the JDialog that has the fields containing text with which to update the Ticket. 
	*/
	public ClientUpdateTicketHandler(String clientUsername, String ticketID, HashMap<String, Ticket> activeTickets, TicketServer ticketServerObject, ClientTicketDialog clientTicketDialog) {
		this.clientUsername = clientUsername;
		this.ticketID = ticketID;
		this.activeTickets = activeTickets;
		this.clientTicketDialog = clientTicketDialog;
		this.ticketServerObject = ticketServerObject;
	}
	
	public void actionPerformed(ActionEvent evt) {
		// Get button clicked
		JButton buttonClicked = (JButton) evt.getSource();
		String actionCommand = new String(buttonClicked.getActionCommand());
		
		if (actionCommand.equals("Submit")) {
			System.out.println(clientUsername + ": updating ticket...");
			
			// Recover ticket and update it
			Ticket ticketToUpdate = (Ticket) activeTickets.get(ticketID);
			ticketToUpdate.setDesc(clientTicketDialog.getSummaryDescriptionField());
			ticketToUpdate.setResolution(clientTicketDialog.getResolutionDescriptionField());
			
			// Call checkInTicket on the RMI object to update the ticket on the server
			ticketServerObject.checkInTicket(ticketToUpdate);
		}
	}
}