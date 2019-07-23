package nio;

import java.net.ServerSocket;
import java.net.Socket;

import bio.TimeServerHandler;

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
	   new Thread(new MultiplexerTimeServer(port)).start();
	}
}
