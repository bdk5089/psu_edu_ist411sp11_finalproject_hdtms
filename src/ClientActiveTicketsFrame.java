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
	private TicketServer ticketServerObject;
	
	public static void main(String[] args) {
		// Create the ClientActiveTicketsFrame
		ClientActiveTicketsFrame test  = new ClientActiveTicketsFrame("anonymous",new HashMap<String,Ticket>(),null);
		test.setSize(400, 300);
		test.setVisible(true);
	}	
	
	/**
	*	ClientActiveTicketsFrame(String clientUsername, HashMap<String, Ticket> activeTickets)
	*	@param clientUsername is the username associated with the Client object instantiating this ClientActiveTicketsFrame.
	*	@param activeTickets is the HashMap containing the active tickets, passed in from the Client.
	*	@param activeTickets is the HashMap containing the active tickets, passed in from the Client.
	*/
	public ClientActiveTicketsFrame(String clientUsername, HashMap<String, Ticket> activeTickets, TicketServer ticketServerObject) {
		super("Active Tickets");
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		this.clientUsername = clientUsername;
		this.activeTickets = activeTickets;
		
		this.ticketServerObject = ticketServerObject;
		
		// Get the Ticket values, instantiate a JList to display them		
		this.activeTicketsToDisplay = new JList(new Vector<Ticket>(activeTickets.values()));
		
		// Setup the JFrame to show the JList of active tickets
		Container clientActiveTicketsFrameContentPane = this.getContentPane();
		clientActiveTicketsFrameContentPane.setLayout(new BorderLayout());
		
		JScrollPane activeTicketScrollPane = new JScrollPane(activeTicketsToDisplay);
		clientActiveTicketsFrameContentPane.add(activeTicketScrollPane, BorderLayout.CENTER);
		
		// Create a label to show who's logged in: clientUsername
		JLabel clientUsernameLabel = new JLabel("Logged in as: " + clientUsername);
		clientActiveTicketsFrameContentPane.add(clientUsernameLabel, BorderLayout.NORTH);
		
		// Create a button to add new tickets
		JPanel buttonPanel = new JPanel(new BorderLayout());
		JButton newButton = new JButton("New Ticket");
		buttonPanel.add(newButton, BorderLayout.CENTER);
		buttonPanel.setMaximumSize(new Dimension(10000,60));
		clientActiveTicketsFrameContentPane.add(buttonPanel, BorderLayout.SOUTH);
		
		
		// Create handler for double-clicks on the active tickets list
		ClientDisplayTicketHandler clientDisplayTicketHandler = new ClientDisplayTicketHandler(clientUsername, activeTickets, ticketServerObject);
		activeTicketsToDisplay.addMouseListener(clientDisplayTicketHandler);
		newButton.addMouseListener(clientDisplayTicketHandler);
	}
	
}