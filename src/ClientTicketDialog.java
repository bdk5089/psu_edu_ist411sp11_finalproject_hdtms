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

import java.awt.*;
import javax.swing.*;

public class ClientTicketDialog extends JDialog {
	
	private String clientUsername;
	private String ticketID;
	private HashMap<String, Ticket> activeTickets;
	
	private JLabel ticketIDLabel;
	
	private JTextArea summaryDescriptionTextArea;
	private JTextArea resolutionDescriptionTextArea;
	
	private JTextField statusCodeTextField;
	private JTextField resolutionCodeTextField;
	
	private JButton submitButton;
	
	private ClientUpdateTicketHandler clientUpdateTicketHandler;
	
	
	public ClientTicketDialog(Frame owner, String clientUsername, String ticketID, HashMap<String, Ticket> activeTickets) {
		super(owner, ticketID, false);
		
		this.clientUsername = clientUsername;
		this.ticketID = ticketID;
		this.activeTickets = activeTickets;
		
		// Create the TicketID label
		ticketIDLabel = new JLabel("Ticket ID: " + ticketID);
		
		// Create the text areas where summary and resolution will be entered
		summaryDescriptionTextArea = new JTextArea(60, 60);
		resolutionDescriptionTextArea = new JTextArea(60, 60);
		summaryDescriptionTextArea.setEditable(true);
		resolutionDescriptionTextArea.setEditable(true);
		
		// Setup the two JScrollPanes for the ticket summary and resolution descriptions
		JScrollPane summaryDescriptionScrollPane = new JScrollPane(summaryDescriptionTextArea);
		JScrollPane resolutionDescriptionTextArea = new JScrollPane(resolutionDescriptionTextArea);
		
		JLabel summaryDescriptionLabel = new JLabel("Summary Description");
		JLabel resolutionDescriptionLabel = new JLabel("Resolution Description");
		
		summaryDescriptionScrollPane.setColumnHeaderView(summaryDescriptionLabel);
		resolutionDescriptionTextArea.setColumnHeaderView(resolutionDescriptionLabel);
		
		
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
//		centerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		centerPanel.add(summaryDescriptionScrollPane);
		centerPanel.add(resolutionDescriptionTextArea);
		
		// Get the content pane and add components
		Container clientTicketDialogContentPane = this.getContentPane();
		
		clientTicketDialogContentPane.add(northPanel, BorderLayout.NORTH);
		clientTicketDialogContentPane.add(southPanel, BorderLayout.SOUTH);
		clientTicketDialogContentPane.add(centerPanel, BorderLayout.CENTER);
		
		// TODO may need to change the dimensions
		this.setSize(400, 200);
		
		// Make a ClientUpdateTicketHandler ActionListener and register the submit button
		// This class handles looking up the appropriate ticket, updating it and sending to the server
		clientUpdateTicketHandler = new ClientUpdateTicketHandler(clientUsername, ticketID, activeTickets);
		submitButton.addActionListener(clientUpdateTicketHandler);
	}
	
	public String getClientUsername() {
		return clientUsername;
	}
	
	public String getTicketID() {
		return ticketID;
	}
}