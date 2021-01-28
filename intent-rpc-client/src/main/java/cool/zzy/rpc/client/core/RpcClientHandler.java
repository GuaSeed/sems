package cool.zzy.rpc.client.core;

import android.util.Log;
import coo.zzy.rpc.common.codec.Beat;
import coo.zzy.rpc.common.codec.RpcRequest;
import coo.zzy.rpc.common.codec.RpcResponse;
import cool.zzy.rpc.client.proxy.RpcFuture;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author intent zzy.main@gmail.com
 * @date 2021/1/12 17:01
 * @since 1.0
 */
public class RpcClientHandler extends SimpleChannelInboundHandler<RpcResponse> {
    private static final String TAG = RpcClientHandler.class.getSimpleName();
    private ConcurrentHashMap<String, RpcFuture> pendingRPC = new ConcurrentHashMap<>();
    private volatile Channel channel;
    public SocketAddress remotePeer;

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        this.channel = ctx.channel();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.remotePeer = this.channel.remoteAddress();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse rpcResponse) throws Exception {
        String requestId = rpcResponse.getRequestId();
        Log.d(TAG, "Receive response: " + requestId);
        RpcFuture rpcFuture = pendingRPC.get(requestId);
        if (rpcFuture != null) {
            pendingRPC.remove(requestId);
            rpcFuture.done(rpcResponse);
        } else {
            Log.w(TAG, "Can not get pending response for request id: " + requestId);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Log.e(TAG, "Client caught exception: " + cause.getMessage());
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateHandler) {
            sendRequest(Beat.BEAT_PING);
            Log.d(TAG, "Client send beat-ping to " + remotePeer);
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    public void close(ChannelFutureListener channelFutureListener) {
        channel.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListeners(ChannelFutureListener.CLOSE, channelFutureListener);
    }

    public RpcFuture sendRequest(RpcRequest request) {
        RpcFuture rpcFuture = new RpcFuture(request);
        pendingRPC.put(request.getRequestId(), rpcFuture);
        try {
            ChannelFuture channelFuture = channel.writeAndFlush(request).sync();
            if (!channelFuture.isSuccess()) {
                Log.e(TAG, "Send request " + request.getRequestId() + " error");
            }
        } catch (InterruptedException e) {
            Log.e(TAG, "Send request exception " + e.getMessage());
        }
        return rpcFuture;
    }
}
