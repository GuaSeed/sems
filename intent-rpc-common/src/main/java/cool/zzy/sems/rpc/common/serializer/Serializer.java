package cool.zzy.sems.rpc.common.serializer;

import java.io.IOException;

/**
 * @author intent zzy.main@gmail.com
 * @date 2021/1/11 16:16
 * @since 1.0
 */
public interface Serializer {
    /**
     * Serialize
     *
     * @param obj
     * @param <T>
     * @return
     */
    <T> byte[] serialize(T obj) throws IOException;

    <T> Object deserialize(byte[] data, Class<T> clazz) throws IOException, ClassNotFoundException;
}
