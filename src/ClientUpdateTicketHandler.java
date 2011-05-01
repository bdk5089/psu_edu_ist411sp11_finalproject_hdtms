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
	
	public ClientUpdateTicketHandler(String clientUsername, String ticketID, HashMap<String, Ticket> activeTickets, ClientTicketDialog clientTicketDialog) {
		this.clientUsername = clientUsername;
		this.ticketID = ticketID;
		this.activeTickets = activeTickets;
		this.clientTicketDialog = clientTicketDialog;
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
		}
	}
}