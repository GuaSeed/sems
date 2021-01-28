package coo.zzy.rpc.common.codec;

import android.util.Log;
import coo.zzy.rpc.common.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author intent zzy.main@gmail.com
 * @date 2021/1/11 16:11
 * @since 1.0
 */
public class RpcDecoder extends ByteToMessageDecoder {
    private static final String TAG = RpcDecoder.class.getSimpleName();
    private final Class<?> genericClass;
    private final Serializer serializer;

    public RpcDecoder(Class<?> genericClass, Serializer serializer) {
        this.genericClass = genericClass;
        this.serializer = serializer;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4) {
            return;
        }
        in.markReaderIndex();
        int dataLength = in.readInt();
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return;
        }
        byte[] data = new byte[dataLength];
        in.readBytes(data);
        Object obj = null;
        try {
            obj = serializer.deserialize(data, genericClass);
            out.add(obj);
        } catch (Exception ex) {
            Log.e(TAG,"Decode error: " + ex.toString());
        }
    }
}
