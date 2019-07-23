package bio;

import java.net.ServerSocket;
import java.net.Socket;

public class TimeServer {
	public static void main(String[] args) {
		int port = 8080;
		if(args!=null&&args.length>0){
			try{
				port =Integer.parseInt(args[0]);
			}catch (NumberFormatException e) {
				// TODO: handle exception
			}
		}
		ServerSocket  sc = null;
		try{
			sc = new ServerSocket(port);
			System.out.println("The time server is start at port:"+port);
			Socket socket = null;
			while(true){
				socket = sc.accept();//阻塞
				System.out.println("rrrr");
				new  Thread(new TimeServerHandler(socket)).start();;
			}
			
		}catch (Exception e) {
			// TODO: handle exception
			
		}
	}
}
