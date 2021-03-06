package cool.zzy.sems.rpc.client.core;

import cool.zzy.sems.rpc.common.codec.*;
import cool.zzy.sems.rpc.common.serializer.JavaSerializer;
import cool.zzy.sems.rpc.common.serializer.Serializer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author intent zzy.main@gmail.com
 * @date 2021/1/12 17:18
 * @since 1.0
 */
public class RpcClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        Serializer serializer = JavaSerializer.class.newInstance();
        ChannelPipeline channelPipeline = socketChannel.pipeline();
//        channelPipeline.addLast(new LoggingHandler(LogLevel.INFO));
        channelPipeline.addLast(new IdleStateHandler(0, 0, Beat.BEAT_INTERVAL, TimeUnit.SECONDS));
        channelPipeline.addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0));
        channelPipeline.addLast(new RpcDecoder(RpcResponse.class, serializer));
        channelPipeline.addLast(new RpcEncoder(RpcRequest.class, serializer));
        channelPipeline.addLast(new RpcClientHandler());
    }
}
