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
import java.util.Date;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.rmi.RemoteException; 
import java.sql.Timestamp;


public class ClientNewTicketHandler implements ActionListener {
	
	private ClientActiveTicketsFrame owner;	
	private User clientUser;
	private ClientNewTicketDialog clientNewTicketDialog;
	private TicketServer ticketServerObject;
	
	/**
	*	ClientNewTicketHandler(User clientUser, HashMap<String, Ticket> activeTickets, TicketServer ticketServerObject, ClientNewTicketDialog clientNewTicketDialog)
	*	@param clientUser is the User object associated with the Client object leading to the instantiation of this ClientDisplayTicketHandler.
	*	@param activeTickets is the HashMap containing the active tickets, passed in from the Client.
	*	@param ticketServerObject is the RMI object representing the server. It will be used for callbacks to create the Ticket.
	*	@param ClientNewTicketDialog is the JDialog that has the fields containing text with which to create the Ticket. 
	*/
	public ClientNewTicketHandler(ClientActiveTicketsFrame owner, User clientUser, ClientNewTicketDialog clientNewTicketDialog, TicketServer ticketServerObject) {
		this.owner = owner;
		this.clientUser = clientUser;
		this.clientNewTicketDialog = clientNewTicketDialog;
		this.ticketServerObject = ticketServerObject;
	}
	
	public void actionPerformed(ActionEvent evt) {
		// Get button clicked
		JButton buttonClicked = (JButton) evt.getSource();
		String actionCommand = new String(buttonClicked.getActionCommand());
		
		if (actionCommand.equals("Submit")) {
			System.out.println(clientUser.getLogon() + ": creating ticket...");
			
			// Get the description and resolution from the ClientNewTicketDialog
			String newDescription = clientNewTicketDialog.getSummaryDescriptionField();
			String newResolution = clientNewTicketDialog.getResolutionDescriptionField();
			
			Ticket newTicket = new Ticket(newDescription, newResolution, null, null, clientUser, new Timestamp(new Date().getTime()));

			// Call checkInTicket() on the RMI object to update the ticket on the server
			try {
				ticketServerObject.checkInTicket(newTicket);
				owner.getActiveTickets();
			} catch (RemoteException re) {
				System.out.println(re.getMessage());
			}
			
			// Refresh the activeTickets HashMap
					
			// Close the ClientNewTicketDialog
			clientNewTicketDialog.setVisible(false);
			clientNewTicketDialog.dispose();
		}
	}
}