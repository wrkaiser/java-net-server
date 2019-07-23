package aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AccptionCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, AsynTimeServerHandler> {

	@Override
	public void completed(AsynchronousSocketChannel result, AsynTimeServerHandler attachment) {
		// TODO Auto-generated method stub
		attachment.asynchronousServerSocketChannel.accept(attachment, this);// 继续接受下一个连接 ，异步的
		ByteBuffer bf = ByteBuffer.allocate(1024);
		result.read(bf, bf, new ReadCompletionHandler(result));
	}

	@Override
	public void failed(Throwable exc, AsynTimeServerHandler attachment) {
		// TODO Auto-generated method stub
		exc.printStackTrace();
		attachment.latch.countDown();
	}

}
