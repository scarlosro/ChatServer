import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
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
			/*ipAddress.addItem("Usuario 1");
			ipAddress.addItem("Usuario 2");
			ipAddress.addItem("Usuario 3");*/

			add(ipAddress);
			
			chatField = new JTextArea(12,20);
			add(chatField);
			input1 = new JTextField(20);
			add(input1);
			sendButton = new JButton("Enviar");
			sendMessage event = new sendMessage();
			sendButton.addActionListener(event);
			add(sendButton);
			
			Thread thread = new Thread(this);
			thread.start();
		}
		
		private class sendMessage implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Socket socket = new Socket("192.168.100.88",9999);
					
					PackageSent information = new PackageSent();
					information.setIp(ipAddress.getSelectedItem().toString());
					information.setNameClient(nameClient.getText());
					information.setMessage(input1.getText());
					
					ObjectOutputStream pack = new ObjectOutputStream(socket.getOutputStream());
					pack.writeObject(information);
					
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
						chatField.append(packReceived.getNameClient() + ": " + packReceived.getMessage());
					}else {
						ArrayList<String> ipsMenu = new ArrayList<String>();
						for(String z:ipsMenu) {
							ipAddress.addItem(z);
							System.out.println(z);
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
			addWindowListener((WindowListener) new Online());
		}
	}

	public static void main(String []args) {
		UserView myView = new UserView();
		myView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
