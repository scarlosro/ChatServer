import java.io.Serializable;
import java.util.ArrayList;

public class PackageSent implements Serializable{
	
	private String nameClient;
	private String ip;
	private String message;
	private ArrayList<String> ipList;
	
	public ArrayList<String> getIpList() {
		return ipList;
	}
	public void setIpList(ArrayList<String> ipList) {
		this.ipList = ipList;
	}
	public String getNameClient() {
		return nameClient;
	}
	public void setNameClient(String nameClient) {
		this.nameClient = nameClient;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	

}
