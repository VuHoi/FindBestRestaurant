package lantrans;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import javax.swing.*;

/**
 *a frame that contains a file chooser and menu bar
 * @author Gaunthan
 *
 */
public class ChooserFrame extends JFrame 
{	
	private static final int DEFAULT_WIDTH = 600;
	private static final int DEFAULT_HEIGHT = 400;
	
	private JButton setHostButton;
	private JButton sendFileButton;
	private JButton receiveFileButton;
	private JButton aboutButton;
	private JButton exitButton;
	
	private JTextArea textArea;
	private JFileChooser chooser;
	private JProgressBar progressBar;
	private JTextArea messageArea;
	private JTextField inputField;
	private JButton sendMessageButton;
	
	private String localHostIP;
	private String localHostName;
	private String host;
	
	/**
	 * construct a frame that has file chooser and buttons
	 */
	public ChooserFrame()
	{	
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);	
		
		JPanel northPanel = new JPanel();
		add(northPanel, BorderLayout.NORTH);

		//add buttons
		
		sendFileButton = new JButton("Send file");
		northPanel.add(sendFileButton);
		
		receiveFileButton = new JButton("Receive file");
		northPanel.add(receiveFileButton);
		
		setHostButton = new JButton("Set host");
		northPanel.add(setHostButton);
		
		aboutButton = new JButton("About");
		northPanel.add(aboutButton);
		
		exitButton = new JButton("Exit");
		northPanel.add(exitButton);
		
		//add listener for each button
		
		sendFileButton.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent event)
				{
					sendFileButton.setEnabled(false);
					receiveFileButton.setEnabled(false);
					
                	if(host == null)
                		host = JOptionPane.showInputDialog(null, "Please enter A host'S address");
     				
                	//be sure that user had entered
                	if(host != null && !host.isEmpty())
                	{
                		chooser.setCurrentDirectory(new File("."));
                		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                		
     					int result = chooser.showOpenDialog(ChooserFrame.this);
     					if(result == JFileChooser.APPROVE_OPTION)
     					{
     						File file = chooser.getSelectedFile();
     						
     						Transporter.sendFile(file, host, progressBar);						
     					}		
                	}
                	
                	sendFileButton.setEnabled(true);
                	receiveFileButton.setEnabled(true);
				}				
			});
	
		receiveFileButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent event)
				{
					sendFileButton.setEnabled(false);
					receiveFileButton.setEnabled(false);
					
             		chooser.setCurrentDirectory(new File("."));
            		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            		
             		int result = chooser.showSaveDialog(ChooserFrame.this);
             		if(result == JFileChooser.APPROVE_OPTION)
 					{
 						File dir = chooser.getSelectedFile();

	             		Transporter.receiveFile(dir, progressBar);		             		
 					}
             		
             		sendFileButton.setEnabled(true);
             		receiveFileButton.setEnabled(true);
				}
			});
			
		setHostButton.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent event) 
				{
					host = JOptionPane.showInputDialog(null, "Please enter a host's address");
				}
			});
		
		aboutButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent event)
				{
					JOptionPane.showMessageDialog(null,
							"LANtrans is a LAN-based file transportation and chatroom program, created by Gaunthan." 
								+ "E-mail:  gaunthanHuang@foxmail.com");
				}
			});
		
		exitButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent event)
				{
					System.exit(0);
				}
			});	

		//set up progress bar
		progressBar = new JProgressBar();
		progressBar.setOrientation(SwingConstants.VERTICAL);
		add(progressBar, BorderLayout.EAST);
		
		//set up a panel for chatroom
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridBagLayout());
		
		centerPanel.add(new JLabel("Chatroom"), new GBC(0, 0, 2, 1));
		
		//add a text area with a scroll
		messageArea = new JTextArea(8, 50);
		messageArea.setEditable(false);
		centerPanel.add(new JScrollPane(messageArea), new GBC(0, 1, 2, 1));		
		
		//add a text field for input message
		inputField = new JTextField(40);
		centerPanel.add(inputField, new GBC(0, 2, 1, 1));
		
		//add a button to send message
		sendMessageButton = new JButton("Send");
		centerPanel.add(sendMessageButton, new GBC(1, 2, 1, 1));
		sendMessageButton.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent e) 
				{
					if(host == null)
                		host = JOptionPane.showInputDialog(null, "Please enter A host'S address");
					
					if(host != null && !host.isEmpty())
					{
						String message = inputField.getText();
						if(!"".equals(message))
						{
							String sendTime = new Date().toString();
							Transporter.sendMessage(message, sendTime, host, localHostName, localHostIP);
							messageArea.append(localHostName + "  :  " + localHostIP + "    " + sendTime + 
									"\r\n    " + message + "\r\n");
						}							
						
						//clear text field
						inputField.setText(null);
					}
				}
			});
		
		Transporter.receiveMessage(messageArea);
		
		//add center panel to frame
		add(centerPanel, BorderLayout.SOUTH);
		
		//add a text area with a scroll for showing information
		textArea = new JTextArea(6,8);
		textArea.setEditable(false);
		add(textArea, BorderLayout.CENTER);
		
		//set information of local host.
		setLocalHostInfo();
		
		//show information on text area
		showInformation(textArea);
		
		// set up file chooser
		chooser = new JFileChooser();
	}
	
	/**
	 * set local host information.
	 */
	public void setLocalHostInfo()
	{
		localHostIP = Transporter.getWlan0IP();
		try 
		{
			localHostName = InetAddress.getLocalHost().getHostName();
		} 
		catch (UnknownHostException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * show some information to user
	 * @param textArea
	 */
	public void showInformation(JTextArea textArea)
	{		
		textArea.append("*Your host:\r\n");
		textArea.append("    " + localHostName);
		textArea.append("    " + localHostIP + "\r\n");
		
		textArea.append("*Notes:\r\n");
		textArea.append("        click button \"Send file\" to choose a file that will send to someone.\n");
		textArea.append("        click button \"Receive file\" to choose a directory where save the file that received from others.\n");
		textArea.append("        click button \"Set host\" to set address where the file or the message sent to.\n");
		textArea.append("        please make sure that you and your friend are in the same LAN.\n");
	}
	
	/**
	 * This class simplifies the use of the GridBagConstraints class.
	 * @author Gaunthan
	 *
	 */
	class GBC extends GridBagConstraints
	{
		/**
		 * Constructs a GBC with given gridx, gridy, gridwidth, gridheight 
		 * and all other grid bag constraint values set to the default.
		 * @param gridx the gridx position
		 * @param gridy the gridy position
		 * @param gridwidth the cell span in x-direction
		 * @param gridheight the cell span in y-direction
		 */
		public GBC(int gridx, int gridy, int gridwidth, int gridheight)
		{
			this.gridx = gridx;
			this.gridy = gridy;
			this.gridwidth = gridwidth;
			this.gridheight = gridheight;
		}
	}
}

