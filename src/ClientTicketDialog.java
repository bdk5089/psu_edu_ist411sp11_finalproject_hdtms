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
	
	private JTextField textField;
	private JTextArea textArea;
	
	private SendMessageHandler sendMessageHandler;
	
	public ClientTicketDialog(Frame owner, String clientUsername, String ticketID, HashMap<String, Ticket> activeTickets) {
		super(owner, ticketID, false);
		
		this.clientUsername = clientUsername;
		this.ticketID = ticketID;
		this.activeTickets = activeTickets;
		
		// TODO modify this code to accomodate displaying the ticket information
		// May need to modify code to send along the entire ticket, rather than just the ticketID
		// Or create a new class to handle transactions of tickets, to and from the server
		
		
		textField = new JTextField(20);
		textArea = new JTextArea();
		textArea.setEditable(false);
		
		// Make panel to hold buttons
		JPanel bottomPanel = new JPanel();
		bottomPanel.add(textField);
		
		// Get the content pane and add components
		Container instantMessageDialogContentPane = this.getContentPane();
		instantMessageDialogContentPane.add(bottomPanel, BorderLayout.SOUTH);
		
		JScrollPane messageScrollPane = new JScrollPane(textArea);
		instantMessageDialogContentPane.add(messageScrollPane, BorderLayout.CENTER);
		
		JLabel recipientMessageLabel = new JLabel("Sending message to " + recipient);
		instantMessageDialogContentPane.add(recipientMessageLabel, BorderLayout.NORTH);
		
		this.setSize(400, 200);
		
		// Make a SendMessageHandler ActionListener and register the buttons and the JTextArea
		sendMessageHandler = new SendMessageHandler(textField, textArea, recipient, this);
		sendButton.addActionListener(sendMessageHandler);
	}
	
	public void updateTextArea(String textToAppend) {
		
		// Add the sent text to the JTextArea
		textArea.append(textToAppend + "\n");
	}
	
	public void clearTextField() {
		textField.setText("");
	}
	
	public String getClientUsername() {
		return clientUsername;
	}
}