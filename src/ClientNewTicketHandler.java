/*
	Filename: ClientNewTicketHandler.java
	Classname: ClientNewTicketHandler
	Comments: Handler that creates a new Ticket.
*/

/** The ClientNewTicketHandler creates a new Ticket when Submit button is pressed on the
*	ClientNewTicketDialog object.
*	@author Eric So, Bruce Kennedy
*	@version 1.0
*/

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.rmi.RemoteException; 
import java.sql.Timestamp;
import java.sql.Date;


public class ClientNewTicketHandler implements ActionListener {
	
	private User clientUser;
	private HashMap<String, Ticket> activeTickets;
	private ClientNewTicketDialog clientNewTicketDialog;
	private TicketServer ticketServerObject;
	
	
	/**
	*	ClientNewTicketHandler(User clientUser, HashMap<String, Ticket> activeTickets, TicketServer ticketServerObject, ClientNewTicketDialog clientNewTicketDialog)
	*	@param clientUser is the User object associated with the Client object leading to the instantiation of this ClientDisplayTicketHandler.
	*	@param activeTickets is the HashMap containing the active tickets, passed in from the Client.
	*	@param ticketServerObject is the RMI object representing the server. It will be used for callbacks to create the Ticket.
	*	@param ClientNewTicketDialog is the JDialog that has the fields containing text with which to create the Ticket. 
	*/
	public ClientNewTicketHandler(User clientUser, HashMap<String, Ticket> activeTickets, TicketServer ticketServerObject, ClientNewTicketDialog clientNewTicketDialog) {
		this.clientUser = clientUser;
		this.activeTickets = activeTickets;
		this.clientNewTicketDialog = clientNewTicketDialog;
		this.ticketServerObject = ticketServerObject;
	}
	
	public void actionPerformed(ActionEvent evt) {
		// Get button clicked
		JButton buttonClicked = (JButton) evt.getSource();
		String actionCommand = new String(buttonClicked.getActionCommand());
		
		if (actionCommand.equals("Submit")) {
			System.out.println(clientUser.getName() + ": creating ticket...");
			
			// Get the description and resolution from the ClientNewTicketDialog
			String newDescription = clientNewTicketDialog.getSummaryDescriptionField();
			String newResolution = clientNewTicketDialog.getResolutionDescriptionField();
			
			// Create a ticket
			Ticket newTicket = new Ticket(newDescription, newResolution, 1, 1, clientUser, new Timestamp(new Date().getTime()));
			
			// Call checkInTicket() on the RMI object to update the ticket on the server
			try {
				ticketServerObject.checkInTicket(newTicket);
			} catch (RemoteException re) {
				System.out.println(re.getMessage());
			}
			
			// Close the ClientNewTicketDialog
			clientNewTicketDialog.setVisible(false);
			clientNewTicketDialog.dispose();
		}
	}
}