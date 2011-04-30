//TODO code this class
// This class handles updating a ticket on the Client by taking the old ticket reference and replacing it
// with a new ticket reference in the Client's activeTickets ArrayList

// This handler will be called from the ClientTicketDialog
// The ClientTicketDialog will require ArrayList of active tickets

/*
	Listener class that handles sending messages from the client, via the InstantMessageDialog class
	to the server.
*/


clientUpdateTicketHandler = new ClientUpdateTicketHandler(clientUsername, ticketID, activeTickets);

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ClientUpdateTicketHandler implements ActionListener {
	
	private String clientUsername;
	private String ticketID;
	private HashMap<String, Ticket> activeTickets;
	
	
	public ClientUpdateTicketHandler(clientUsername, ticketID, activeTickets) {

		this.clientUsername = clientUsername;
		this.ticketID = ticketID;
		this.activeTickets = activeTickets;
	}
	
	public void actionPerformed(ActionEvent evt) {
		// Get button clicked
		JButton buttonClicked = (JButton) evt.getSource();
		String actionCommand = new String(buttonClicked.getActionCommand());
		
		
		textToSend = textField.getText();
		
		if (actionCommand.equals("Submit")) {
			System.out.println(clientUsername + ": updating ticket...");
			
			// Recover ticket and update it
			Ticket ticketToUpdate = (Ticket) activeTickets.get(ticketID);
			
			ticketToUpdate
			
		}
	}
}