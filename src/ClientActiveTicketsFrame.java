/*
	Filename: ClientActiveTicketsFrame.java
	Classname: ClientActiveTicketsFrame
	Comments: JFrame that displays the active tickets list.
*/

/** The ClientActiveTicketsFrame class displays the active tickets list.
*	@author Eric So, Bruce Kennedy
*	@version 1.0
*/

import java.net.*;
import java.util.*;

import java.awt.*;
import javax.swing.*;

public class ClientActiveTicketsFrame extends JFrame {
	
	private String clientUsername;
	private HashMap<String, Ticket> activeTickets;
	private JList activeTicketsToDisplay;
	
	public InstantMessageFrame(String clientUsername, HashMap<String, Ticket> activeTickets) {
		super("Active Tickets");
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		this.clientUsername = clientUsername;
		this.activeTickets = activeTickets;
		
		// Create Array of int objects with the ticket IDs of the active tickets
		int[] ticketIdArray = activeTickets.keySet().toArray();
		
		this.activeTicketsToDisplay = new JList(ticketIdArray);
		
		// Setup the InstantMessageFrame
		Container clientActiveTicketsFrameContentPane = this.getContentPane();
		clientActiveTicketsFrameContentPane.setLayout(new BorderLayout());
		
		JScrollPane activeTicketScrollPane = new JScrollPane(activeTicketsToDisplay);
		clientActiveTicketsFrameContentPane.add(activeTicketScrollPane, BorderLayout.CENTER);
		
		// Create a lable to show who's logged in: clientUsername
		JLabel clientUsernameLabel = new JLabel("Logged in as: " + clientUsername);
		clientActiveTicketsFrameContentPane.add(clientUsernameLabel, BorderLayout.NORTH);
		
		// Create handler for double-clicks on the active tickets list
		ClientDisplayTicketHandler clientDisplayTicketHandler = new ClientDisplayTicketHandler(clientUsername, activeTickets);
		activeTicketsToDisplay.addMouseListener(clientDisplayTicketHandler);
	}
	
}