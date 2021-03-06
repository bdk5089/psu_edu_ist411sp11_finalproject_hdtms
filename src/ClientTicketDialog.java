/*
	Filename: ClientTicketDialog.java
	Classname: ClientTicketDialog
	Comments: JDialog that displays the ticket information.
*/

/** The ClientTicketDialog displays ticket information of the ticket that was double-clicked.
*	@author Eric So, Bruce Kennedy
*	@version 1.0
*/

import java.net.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;

public class ClientTicketDialog extends JFrame {
	
	private User clientUser;
	private Ticket ticket;
	private TicketServer ticketServerObject;
	private ClientUpdateTicketHandler clientUpdateTicketHandler;
	private ClientActiveTicketsFrame owner;
	
	private JLabel ticketIDLabel;
	private JTextArea summaryDescriptionTextArea;
	private JTextArea resolutionDescriptionTextArea;
	private JButton submitButton;
	
	// TODO code for radio buttons for the status code and resolution codes
//	private JTextField statusCodeTextField;
//	private JTextField resolutionCodeTextField;
	
	/**
	*	ClientTicketDialog(ClientActiveTicketsFrame owner, User clientUser, String ticketID, HashMap<String, Ticket> activeTickets, TicketServer ticketServerObject)
	*	@param owner is the owner of this JDialog; it will be set to null.
	*	@param clientUser is the user associated with the Client object leading to the instantiation of this ClientDisplayTicketHandler.
	*	@param ticketID is a String representation of the ID number associated with this JDialog.
	*	@param activeTickets is the HashMap containing the active tickets, passed in from the Client.
	*	@param ticketServerObject is the RMI object representing the server. It will be used for callbacks to update the Ticket.
	*/
	public ClientTicketDialog(ClientActiveTicketsFrame owner, User clientUser, Ticket ticket, TicketServer ticketServerObject) {
		super("Update Ticket");
		
		this.owner = owner;
		this.clientUser = clientUser;
		this.ticket = ticket;
		this.ticketServerObject = ticketServerObject;
		
		
		// Create the TicketID label
		ticketIDLabel = new JLabel("Ticket ID: " + ticket.getID());
		
		// Create the text areas where summary and resolution will be entered
		summaryDescriptionTextArea = new JTextArea(20, 20);
		summaryDescriptionTextArea.setLineWrap(true);
		summaryDescriptionTextArea.setWrapStyleWord(true);
		
		resolutionDescriptionTextArea = new JTextArea(20, 20);
		resolutionDescriptionTextArea.setLineWrap(true);
		resolutionDescriptionTextArea.setWrapStyleWord(true);
		
		// Set the text in the text areas
		summaryDescriptionTextArea.setText(ticket.getDesc());
		resolutionDescriptionTextArea.setText(ticket.getResolution());
		// Make the text areas editable
		summaryDescriptionTextArea.setEditable(true);
		resolutionDescriptionTextArea.setEditable(true);
		
		// Setup the two JScrollPanes for the ticket summary and resolution descriptions
		JScrollPane summaryDescriptionScrollPane = new JScrollPane(summaryDescriptionTextArea);
		JScrollPane resolutionDescriptionScrollPane = new JScrollPane(resolutionDescriptionTextArea);
		
		JLabel summaryDescriptionLabel = new JLabel("Summary Description");
		JLabel resolutionDescriptionLabel = new JLabel("Resolution Description");
		
		summaryDescriptionScrollPane.setColumnHeaderView(summaryDescriptionLabel);
		resolutionDescriptionScrollPane.setColumnHeaderView(resolutionDescriptionLabel);
		
		
		// TODO change the statuscode and resolutioncode stuff to radio buttons
		// put into the EAST border
//		statusCodeTextField = new JTextField();
//		resolutionCodeTextField = new JTextField();
		
		
		// Create the submit button
		submitButton = new JButton("Submit");
		
		// Create the panels for the border layout
		JPanel northPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		JPanel southPanel = new JPanel();
		
		northPanel.add(ticketIDLabel);
		southPanel.add(submitButton);
		
		centerPanel.setLayout(new FlowLayout());
		centerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		centerPanel.add(summaryDescriptionScrollPane);
		centerPanel.add(resolutionDescriptionScrollPane);
		
		// Get the content pane and add components
		Container clientTicketDialogContentPane = this.getContentPane();
		
		clientTicketDialogContentPane.add(northPanel, BorderLayout.NORTH);
		clientTicketDialogContentPane.add(southPanel, BorderLayout.SOUTH);
		clientTicketDialogContentPane.add(centerPanel, BorderLayout.CENTER);
		
		// TODO may need to change the dimensions
		this.setSize(500, 450);
		
		// Make a ClientUpdateTicketHandler ActionListener and register the submit button
		// This class handles looking up the appropriate ticket, updating it and sending to the server
		clientUpdateTicketHandler = new ClientUpdateTicketHandler(owner, clientUser, ticket, this, ticketServerObject);
		submitButton.addActionListener(clientUpdateTicketHandler);
	}
	
	public User getClientUser() {
		return clientUser;
	}
	
	public int getTicketID() {
		return ticket.getID();
	}
	
	public String getSummaryDescriptionField() {
		return summaryDescriptionTextArea.getText();
	}
	
	public String getResolutionDescriptionField() {
		return resolutionDescriptionTextArea.getText();
	}
}