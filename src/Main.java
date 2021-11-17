import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import java.awt.event.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Main {
	
	public static Thread thread;
	
	static class ClientOnline extends WindowAdapter{
		public void windowOpened(WindowEvent e) {
			try {
				Socket soc = new Socket("192.168.100.4",9999);
				PackageSent info = new PackageSent();
				info.setMessage(" online");
				ObjectOutputStream package_data = new ObjectOutputStream(soc.getOutputStream());
				package_data.writeObject(info);
				soc.close();
				
				
			}catch(Exception ew) {
				
			}
		}
	}
	
	
	public  static class LayoutView extends JPanel implements Runnable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public JLabel label1;
		public JButton sendButton;
		private  JTextField input1;
		private JLabel nameClient;
		private JComboBox ipAddress;
		private JTextArea chatField;
		private JLabel c_name;
		private JScrollPane scroll;
		String name_user;

		public LayoutView() {
			
			name_user=JOptionPane.showInputDialog("Nombre: ");
			
			c_name=new JLabel("Usuario: ");
			add(c_name);
			
			nameClient = new JLabel();
			nameClient.setText(name_user);
			add(nameClient);	
			
			label1 = new JLabel("Chat");
			add(label1);
			
			ipAddress = new JComboBox();
			add(ipAddress);
			
			chatField = new JTextArea(12,20);
			add(chatField);
			scroll = new JScrollPane(chatField);
			scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			add(scroll);
			input1 = new JTextField(20);
			add(input1);
			sendButton = new JButton("Enviar");
			sendMessage event = new sendMessage();
			sendButton.addActionListener(event);
			add(sendButton);
			
			thread = new Thread(this);
			thread.start();
		}
		
		private class sendMessage implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				chatField.append("Tú: " + input1.getText() + "\n");

				try {
					Socket socket = new Socket("192.168.100.4",9999);
					
					PackageSent information = new PackageSent();
					information.setIp(ipAddress.getSelectedItem().toString());
					information.setNameClient(nameClient.getText());
					System.out.println("Message to send is " + input1.getText());
					information.setMessage(input1.getText());
					
					ObjectOutputStream pack = new ObjectOutputStream(socket.getOutputStream());
					pack.writeObject(information);
					input1.setText("");
					socket.close(); 
					
					
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
			
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			chatField.append(nameClient.getText() + ": " + input1.getText() + "\n");
			try {
				ServerSocket server_client = new ServerSocket(9090);
				Socket client;
				
				PackageSent packReceived;
				
				while(true) {
					client = server_client.accept();
					ObjectInputStream receive = new ObjectInputStream(client.getInputStream());
					
					packReceived= (PackageSent) receive.readObject();
					
					if(!packReceived.getMessage().equals(" online")) {
						chatField.append(packReceived.getNameClient() + ": " + packReceived.getMessage() + "\n");
					}else {
						ArrayList<String> ipsMenu = new ArrayList<String>();
						ipsMenu=packReceived.getIpList();
						ipAddress.removeAllItems();
						for(String z: ipsMenu) {
							
							ipAddress.addItem(z);
						}
					}
				}
				
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
			
		}
	}
	
	
	public static class UserView extends JFrame{

		public UserView() {
			setBounds(600,300,280,350);
			LayoutView layoutView = new LayoutView();
			add(layoutView);
			setVisible(true);
			addWindowListener( new ClientOnline());
			addWindowListener(new WindowAdapter() {

		        @SuppressWarnings("deprecation")
				@Override
		        public void windowClosing(WindowEvent e) {
		            super.windowClosing(e); 
		            thread.stop();
		            
		        }


		    });
		}
		
	}

	public static void main(String []args) {
		UserView myView = new UserView();
		myView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
