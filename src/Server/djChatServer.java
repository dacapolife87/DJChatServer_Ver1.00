package Server;
import java.io.*;
import java.net.*;
import java.util.Date;

public class djChatServer {
	int port = 2345;
	ServerSocket sSocket;
	Socket soc;
	long time = System.currentTimeMillis();
	djChatServerThread obj_ServerThread;
	Thread tr;
	public djChatServer() {
		// TODO Auto-generated constructor stub
		try {
			sSocket = new ServerSocket(port);
			System.out.println("Server Start");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void startServer(){
		while(true){
			try {
				soc= sSocket.accept();
				System.out.println("accept : "+soc.getInetAddress());
				System.out.println("acceptTime : "+new Date(time));
				obj_ServerThread= new djChatServerThread(soc);
				tr = new Thread(obj_ServerThread);
				tr.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					soc.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}	
		}
	}
	public void stopServerThread(){
		if(tr != null){
			if (tr != Thread.currentThread())
				tr.interrupt();
			tr=null;
		}
	}
	public void stopSocket(){
		try {
			soc.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		djChatServer obj_Server = new djChatServer();
		obj_Server.startServer();
	}
}
