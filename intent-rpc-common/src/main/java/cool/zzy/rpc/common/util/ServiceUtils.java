package cool.zzy.rpc.common.util;

/**
 * @author intent zzy.main@gmail.com
 * @date 2021/1/11 15:03
 * @since 1.0
 */
public class ServiceUtils {
    public static final String SERVICE_CONCAT_TOKEN = "#";

    public static String generateKey(String name, int version) {
        return name + SERVICE_CONCAT_TOKEN + version;
    }
}
