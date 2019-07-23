package aio;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Date;

public class ReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {

	private AsynchronousSocketChannel channel;

	public ReadCompletionHandler(AsynchronousSocketChannel channel) {
		// TODO Auto-generated constructor stub
		if (this.channel == null)
			this.channel = channel;
	}

	@Override
	public void completed(Integer result, ByteBuffer attachment) {
		// TODO Auto-generated method stub
		attachment.flip();
		byte[] body = new byte[attachment.remaining()];
		attachment.get(body);
		try {
			String req = new String(body, "UTF-8");
			System.out.println("The time server receive order:" + req);
			String current_time = "QUERY TIME ORDER".equalsIgnoreCase(req)
					? new Date(System.currentTimeMillis()).toString() : "BAD ORDER";
			doWrite(current_time);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void doWrite(String current_time) {
		if (current_time != null && current_time.length() > 0) {
			byte[] bytes = current_time.getBytes();
			ByteBuffer bf = ByteBuffer.allocate(bytes.length);
			bf.put(bytes);
            bf.flip();
            channel.write(bf, bf, new CompletionHandler<Integer, ByteBuffer>(){

				@Override
				public void completed(Integer result, ByteBuffer attachment) {
					// TODO Auto-generated method stub
					if(attachment.hasRemaining()){
						channel.write(bf, bf,this);
					}
					
				}

				@Override
				public void failed(Throwable exc, ByteBuffer attachment) {
					// TODO Auto-generated method stub
					try{
						channel.close();
					}catch (Exception e) {
						// TODO: handle exception
					}
				}
            	
            });
		}
	}

	@Override
	public void failed(Throwable exc, ByteBuffer attachment) {
		// TODO Auto-generated method stub
		try{
			this.channel.close();
		}catch (Exception e) {
			// TODO: handle exception
		}

	}

}
