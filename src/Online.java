import java.awt.event.WindowEvent;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Online {
	
	public void windowOpened(WindowEvent e) {
		try {
			Socket soc = new Socket("192.168.100.88",9999);
			PackageSent info = new PackageSent();
			info.setMessage(" online");
			ObjectOutputStream package_data = new ObjectOutputStream(soc.getOutputStream());
			package_data.writeObject(info);
			soc.close();
			
			
		}catch(Exception ew) {
			
		}
	}

}
