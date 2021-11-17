import javax.swing.JFrame;


import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	 static class LayoutServer extends JFrame implements Runnable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		JTextArea area;
		JPanel view;
		Thread thread;
		
		public LayoutServer() {
			setBounds(1200, 300, 300, 400);
			view = new JPanel();
			view.setLayout(new BorderLayout());
			area = new JTextArea();
			view.add(area, BorderLayout.CENTER);
			add(view);
			setVisible(true);
			thread = new Thread(this);
			thread.start();
		}
		@Override
		public void run() {
			try {
				String nameClient;
				String ipAddress;
				String message;
				ArrayList <String> ipList = new ArrayList<String>();
				
				PackageSent pack;
				
				
				ServerSocket server = new ServerSocket(9999);
				
				while(true) {
					Socket socketReceived = server.accept();
					
					
					ObjectInputStream information = new ObjectInputStream(socketReceived.getInputStream());
					pack = (PackageSent) information.readObject();
					
					nameClient = pack.getNameClient();
					ipAddress = pack.getIp();
					message= pack.getMessage();
					
					
					if(!message.equals(" online")) {
					
						area.append(nameClient + ": " + message + " para " + ipAddress + "\n");
						
						Socket sentTo = new Socket(ipAddress, 9090);
						ObjectOutputStream resentPack = new ObjectOutputStream(sentTo.getOutputStream());
						
						resentPack.writeObject(pack);
						
						resentPack.close();
						sentTo.close();
						socketReceived.close();					
					}else {
						InetAddress localization = socketReceived.getInetAddress();
						String ipDir = localization.getHostAddress();
						ipList.add(ipDir);
						pack.setIpList(ipList);  
						for(String z: ipList) {
							Socket sentTo = new Socket(z, 9090);
							ObjectOutputStream resentPack = new ObjectOutputStream(sentTo.getOutputStream());
							
							resentPack.writeObject(pack);
							
							resentPack.close();
							sentTo.close();
							socketReceived.close();		
						}
					}
				}	
				}catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	
	public static void main(String[]args) {
		LayoutServer layout = new LayoutServer();
		layout.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}

}
