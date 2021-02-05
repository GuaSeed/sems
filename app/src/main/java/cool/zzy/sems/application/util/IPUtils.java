package cool.zzy.sems.application.util;

import android.util.Log;
import com.google.gson.Gson;
import cool.zzy.sems.application.SemsApplication;
import cool.zzy.sems.application.model.IPApi;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

import java.net.InetSocketAddress;
import java.net.URI;

/**
 * @author intent zzy.main@gmail.com
 * @date 2021/2/5 16:58
 * @since 1.0
 */
public class IPUtils {
    private static final String TAG = IPUtils.class.getSimpleName();
    private static final String GET_IP_API_HOST = "http://ip-api.com";
    private static final String GET_IP_API_IP = "208.95.112.1";
    private static final String GET_IP_API_PATH = "/json/?lang=zh-CN";

    private interface Callback {
        void callback(String json);
    }

    public static void getMyIp() {
        try {
            connect(GET_IP_API_IP, 80, json -> {
                IPApi ipApi = new Gson().fromJson(json, IPApi.class);
                SemsApplication.instance.setIp(ipApi.getQuery());
                Log.d(TAG, "getMyIp: " + json);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void connect(String host, int port, Callback callback) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup(1);
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, false)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new HttpResponseDecoder());
                            pipeline.addLast(new HttpRequestEncoder());
                            pipeline.addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    if (msg instanceof HttpContent) {
                                        HttpContent content = (HttpContent) msg;
                                        ByteBuf buf = content.content();
                                        String json = buf.toString(io.netty.util.CharsetUtil.UTF_8);
                                        callback.callback(json);
                                        buf.release();
                                        ChannelFuture channelFuture = ctx.writeAndFlush(Unpooled.EMPTY_BUFFER);
                                        channelFuture.addListeners(ChannelFutureListener.CLOSE);
                                    }
                                }
                            });
                        }
                    });
            ChannelFuture f = bootstrap.connect(new InetSocketAddress(host, port)).sync();
            f.addListeners((ChannelFutureListener) future1 -> {
                if (future1.isSuccess()) {
                    Log.d(TAG, "connect: success.");
                } else {
                    Log.e(TAG, "connect: fail");
                }
            });
            URI uri = new URI(GET_IP_API_HOST + GET_IP_API_PATH);
            DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET,
                    uri.toASCIIString());
            // 构建http请求
            request.headers().set(HttpHeaderNames.HOST, host);
            request.headers().set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
            // 发送http请求
            f.channel().writeAndFlush(request).channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}
