package lantrans;

import java.io.*;

import java.net.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.util.*;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

/**
 * a transporter that can send or receive file.
 * @author Gaunthan
 *
 */
public class Transporter 
{
	private static final int FILE_PORT = 8192;
	private static final int MESSAGE_PORT = 8198;
	private static final int BUFFER_SIZE = 1024;
	private static String serverIP;
	
	/**
	 * Send a message to host.
	 * @param message a string
	 * @param the time that send the message
	 * @param host where the message send to
	 * @param localHostName the name of the local host
	 * @param localHostIP the IP of the local host
	 */
	public static void sendMessage(String message, String sendTime, String host, 
			String localHostName, String localHostIP)
	{
		new Thread(new Runnable() 
		{
			public void run() 
			{
				try(Socket s = new Socket(host, MESSAGE_PORT))
				{
					try(OutputStream os = s.getOutputStream();
							PrintWriter out = new PrintWriter(os))
					{
						out.println(localHostName);
						out.println(localHostIP);
						out.println(sendTime);
						out.println(message);
					}			
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	
	/**
	 * Send a file to a host.
	 * @param file a file that would be transported to a host
	 * @param host a host's address
	 * @param progressBar a JProgressBar of the main frame, for showing the progress of transportation.
	 */
	public static void sendFile(File file, String host, JProgressBar progressBar)
	{
		new Thread(new Runnable() 
		{
			public void run() 
			{
				//set progress bar
				
				progressBar.setMaximum((int) (file.length() / 1024));
				progressBar.setStringPainted(true);
				progressBar.setValue(0);
				
				try
				{
					//send file name to socket
					
					try(Socket s = new Socket(host, FILE_PORT))
					{
						
						try(OutputStream os = s.getOutputStream();
								PrintWriter out = new PrintWriter(os)	)
						{
							out.println(getWlan0IP());
							out.println(file.getName());
							out.print(file.length());
						}			
					}
					
					//send file data to socket	
					
					try(Socket s = new Socket(host, FILE_PORT))
					{		
						int totalLength = 0;
						
						try(OutputStream os = s.getOutputStream();
								InputStream is = new BufferedInputStream(new FileInputStream(file), BUFFER_SIZE))
						{				
							byte[] b = new byte[BUFFER_SIZE];
							
							int length;
							
							while((length = is.read(b)) != -1)
							{
								totalLength += 1;
								progressBar.setValue(totalLength);
								
								if(length == BUFFER_SIZE)
									os.write(b);
								else 
								{
									byte[] tmp = new byte[length];
									
									for(int i = 0; i < length; ++i)
									{
										tmp[i] = b[i];
									}
									
									os.write(b);	
								}
							}					
						}			
					}
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
				
				//show information
				JOptionPane.showMessageDialog(null, new Date() + "\r\n" + "To : " + host + "\r\n"
						+ "Send : " + file.getPath() + "\r\n" + "bytes : " + FormatFileSize(file.length()));
			}
		}).start();	
	}
	

	/**
	 * Get a message string from socket and add information to message area.
	 * @param messageArea a JTextArea for showing received information
	 */
	public static void receiveMessage(JTextArea messageArea)
	{	
		new Thread(new Runnable() 
			{
				String hostName;
				String hostIP;
				String message;
				String time;
				
				public void run() 
				{
					try(ServerSocket server = new ServerSocket(MESSAGE_PORT))
					{
						while(true)
						{
							try(Socket incoming = server.accept())
							{			
								try(InputStream is = incoming.getInputStream();
										Scanner in = new Scanner(is))
								{
									hostName = in.nextLine();
									hostIP = in.nextLine();
									time = in.nextLine();
									message = in.nextLine();
																	
									//show message in message area
									messageArea.append(hostName + "  :  " + hostIP + "    " + time + 
											"\r\n    " + message + "\r\n");
								}
							}
						}
					}
					catch(IOException e)
					{
						e.printStackTrace();
					}	
				}
			}).start();
	}

	
	/**
	 * Enter listen mode that will wait for a connection, then get the file from the connection.
	 */
	public static void receiveFile(File dir, JProgressBar progressBar)
	{
		new Thread(new Runnable() 
			{
				public void run() 
				{
					File file = null;
					long fileLength;
					
					try(ServerSocket server = new ServerSocket(FILE_PORT))
					{
						progressBar.setValue(0);
						
						//get file name
						try(Socket incoming = server.accept())
						{			
							try(InputStream is = incoming.getInputStream();
									Scanner in = new Scanner(is))
							{
								//read server IP
								serverIP = in.nextLine();
								
								//read file name
								String filename = in.nextLine();
								
								file = new File(dir.getPath() + File.separator + filename);		
								
								//read file length
								fileLength = in.nextLong();		
								
								//create file to save the data
								if(file.exists())
									file.delete();
								file.createNewFile();
							}
						}

						//get file data
						try(Socket incoming = server.accept())
						{			
							try(InputStream is = incoming.getInputStream())
							{	
								//set progress bar
								
								progressBar.setMaximum((int) (fileLength / 1024));
								progressBar.setStringPainted(true);
								
								//read bytes from socket
								
								byte[] buffer = new byte[BUFFER_SIZE];
								int length;
								
								int totalLength = 0;					
								
								while((length = is.read(buffer)) != -1)
								{
									totalLength += 1;
									progressBar.setValue(totalLength);
									
									if(length == BUFFER_SIZE)
										Files.write(file.toPath(), buffer, StandardOpenOption.APPEND);
									else
									{
										byte[] b = new byte[length];
										
										for(int i = 0; i < length; ++i)
										{
											b[i] = buffer[i];
										}
										Files.write(file.toPath(), b, StandardOpenOption.APPEND);
									}
								}
							}		
						}
						
						//show information
						JOptionPane.showMessageDialog(null, new Date() + "\r\n" + "From : " + serverIP + "\r\n"
								+ "receive : " + file.getName() + "\r\n" + "bytes : " + FormatFileSize(file.length()));	
					}
					catch(IOException e)
					{
						e.printStackTrace();
					}	
				}
			}).start();
	}
	
				
	/**
	 * format file size
	 * @param fileSize the size of a file
	 * @return new file size
	 */
	public static String FormatFileSize(long fileSize)
	{  
        DecimalFormat df = new DecimalFormat("#.00");  
        String fileSizeString = "";  
        
        if (fileSize < 1024) 
            fileSizeString = df.format((double) fileSize) + "B";  
        else if (fileSize < 1048576) 
        	fileSizeString = df.format((double) fileSize / 1024) + "K";   
        else if (fileSize < 1073741824)  
        	fileSizeString = df.format((double) fileSize / 1048576) + "M";   
        else  
        	fileSizeString = df.format((double) fileSize / 1073741824) + "G";    
        
        return fileSizeString;  
    }  
	
	
	/**
	 * get IP of Ethernet interface "Wlan0" 
	 * @return IP of Ethernet interface Wlan0
	 */
	public static String getWlan0IP()
	{	
		try
		{
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces(); 
			
			InetAddress ip = null; 
			while (interfaces.hasMoreElements()) 
	        {  
	            NetworkInterface ni = interfaces.nextElement();  
	            
	            if("wlan0".equals(ni.getName()))
	            {
	            	Enumeration<InetAddress> addresses= ni.getInetAddresses();
		            while (addresses.hasMoreElements())
		            {
		            	  ip = (InetAddress) addresses.nextElement();  
		                  if (ip != null && ip instanceof Inet4Address)  
		                  {  
		                      return ip.getHostAddress();
		                  }  	  	            
		            }	
	            }	            	            
	        }
		}catch(SocketException e){}
		
		return null;	
	}
}
