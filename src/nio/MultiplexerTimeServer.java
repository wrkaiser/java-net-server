package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

public class MultiplexerTimeServer implements Runnable {

	private Selector selector;
	private ServerSocketChannel server;
	private volatile boolean stop;

	public MultiplexerTimeServer(int port) {
		// TODO Auto-generated constructor stub
		try {
			server = ServerSocketChannel.open();
			server.configureBlocking(false);
			selector = Selector.open();
			server.socket().bind(new InetSocketAddress(port), 1024);
			server.register(selector, SelectionKey.OP_ACCEPT);
			System.out.println("The time server start in port :" + port);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.exit(1);
		}

	}

	public void stop() {
		this.stop = true;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (!stop) {
			try {
				selector.select(1000);
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				SelectionKey key = null;
				Iterator<SelectionKey> it = selectedKeys.iterator();
				while (it.hasNext()) {
					key = it.next();
					it.remove();
					try {
						handleInput(key);
					} catch (Exception e) {
						// TODO: handle exception
						if (key != null) {
							key.cancel();
							if (key.channel() != null)
								key.channel().close();
						}

					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		if (selector != null) {
			try {
				selector.close();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}

	}

	private void handleInput(SelectionKey key) throws IOException {
		if (key.isValid()) {
			if (key.isAcceptable()) {
				ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
				SocketChannel sc = ssc.accept();
				sc.configureBlocking(false);
				sc.register(selector, SelectionKey.OP_READ);
			}
			if (key.isReadable()) {
				SocketChannel sc = (SocketChannel) key.channel();
				ByteBuffer bf = ByteBuffer.allocate(1024);// 后面调用了new
															// HeapByteBuffer(capacity,
															// capacity);
				int readBytes = sc.read(bf);
				if (readBytes > 0) {
					bf.flip();
					byte[] bytes = new byte[bf.remaining()];
					bf.get(bytes);
					String body = new String(bytes, "UTF-8");
					System.out.println("The time server receive order:" + body);
					String current_time = "QUERY TIME ORDER".equalsIgnoreCase(body.trim())
							? new Date(System.currentTimeMillis()).toString() : "BAD ORDER";
					doWrite(sc, current_time);
				}
			}
		}

	}

	private void doWrite(SocketChannel sc, String response) throws IOException {
		if (response != null && response.trim().length() > 0) {
			byte[] bytes = response.getBytes();
			ByteBuffer  writerBuffer = ByteBuffer.allocate(bytes.length);
			writerBuffer.put(bytes);
			writerBuffer.flip();
			sc.write(writerBuffer);
		}

	}

}
