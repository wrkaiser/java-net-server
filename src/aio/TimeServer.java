package aio;

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
	
		AsynTimeServerHandler asynTimeServerHandler = new AsynTimeServerHandler(port);
		new Thread(asynTimeServerHandler,"AIO-TimeServerHandler-001").start();;
		
	}
}
