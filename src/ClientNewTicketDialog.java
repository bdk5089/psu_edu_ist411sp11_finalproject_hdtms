/*
	Filename: ClientNewTicketDialog.java
	Classname: ClientNewTicketDialog
	Comments: JDialog that allows the user to create a new ticket.
*/

/** The ClientNewTicketDialog displays a dialog so that the user can create a new Ticket.
*	@author Eric So, Bruce Kennedy
*	@version 1.0
*/

import java.net.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;

public class ClientNewTicketDialog extends JFrame {
	
	private User clientUser;
	private TicketServer ticketServerObject;
	
	private ClientNewTicketHandler clientNewTicketHandler;
	
	private JLabel ticketIDLabel;
	private JTextArea summaryDescriptionTextArea;
	private JTextArea resolutionDescriptionTextArea;
	private JButton submitButton;
	private ClientActiveTicketsFrame owner;
	
	/**
	*	ClientNewTicketDialog(ClientActiveTicketsFrame owner, User clientUser, HashMap<String, Ticket> activeTickets, TicketServer ticketServerObject)
	*	@param owner is the owner of this JDialog; it will be set to null.
	*	@param clientUser is the username associated with the Client object leading to the instantiation of this ClientDisplayTicketHandler.
	*	@param activeTickets is the HashMap containing the active tickets, passed in from the Client.
	*	@param ticketServerObject is the RMI object representing the server. It will be used for callbacks to create the Ticket.
	*/
	public ClientNewTicketDialog(ClientActiveTicketsFrame owner, User clientUser, TicketServer ticketServerObject) {
		super("New Ticket");
		
		this.owner = owner;
		this.clientUser = clientUser;
		this.ticketServerObject = ticketServerObject;
		
		// Create the TicketID label
		ticketIDLabel = new JLabel("Create New Ticket" );
		
		// Create the text areas where summary and resolution will be entered
		summaryDescriptionTextArea = new JTextArea(20, 20);
		resolutionDescriptionTextArea = new JTextArea(20, 20);
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
		
		// Make a ClientNewTicketHandler ActionListener and register the submit button
		// This class handles creating a new Ticket and sending that to the server
		clientNewTicketHandler = new ClientNewTicketHandler(owner, clientUser, this,ticketServerObject);
		submitButton.addActionListener(clientNewTicketHandler);
	}
	
	public User getClientUser() {
		return clientUser;
	}
		
	public String getSummaryDescriptionField() {
		return summaryDescriptionTextArea.getText();
	}
	
	public String getResolutionDescriptionField() {
		return resolutionDescriptionTextArea.getText();
	}
}