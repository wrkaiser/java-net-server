package bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

public class TimeServerHandler implements Runnable {

	private Socket socket;

	public TimeServerHandler(Socket socket) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		BufferedReader  in = null;
		PrintWriter  out =null;
		try{
			in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			out = new PrintWriter(this.socket.getOutputStream(),true);
			String current_time =null ,body = null;
			while(true){
				body = in.readLine();
				if(body == null)
					break;
				System.out.println("The time server receive order:"+body);
				current_time = "QUERY TIME ORDER".equalsIgnoreCase(body.trim()) ? new Date(System.currentTimeMillis()).toString() : "BAD ORDER";
				out.println(current_time);
			}
		
			
		}catch (Exception e) {
			// TODO: handle exception
		}finally {
			if(in!=null){
				try{
					in.close();
				}catch (Exception e) {
					// TODO: handle exception
				}
			}
			if(out!=null){
				out.flush();
				out.close();
				out=null;
			}
			if(socket != null){
				try {
					this.socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				this.socket = null;
			}
		}
		

	}

}
