package False_AIO;

import java.net.ServerSocket;
import java.net.Socket;

public class TimeServer {
	public static void main(String[] args) {
		int port = 8080;
		if (args != null && args.length > 0) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				// TODO: handle exception
			}
		}
		ServerSocket sc = null;
		try {
			sc = new ServerSocket(port);
			System.out.println("The time server is start at port:" + port);
			Socket socket = null;
			TimeServerHandlerPool timeServerHandlerPool = new TimeServerHandlerPool(50, 1000);
			while (true) {
				socket = sc.accept();// 阻塞
				timeServerHandlerPool.execute(new TimeServerHandler(socket));
			}

		} catch (Exception e) {
			// TODO: handle exception

		}
	}
}
