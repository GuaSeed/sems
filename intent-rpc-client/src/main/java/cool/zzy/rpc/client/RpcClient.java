package cool.zzy.rpc.client;

import cool.zzy.rpc.client.core.ConnectionManager;
import cool.zzy.rpc.client.proxy.ObjectProxy;

import java.lang.reflect.Proxy;

/**
 * @author intent zzy.main@gmail.com
 * @date 2021/1/12 10:35
 * @since 1.0
 */
public class RpcClient {

    public RpcClient(String address) {
        String[] array = address.split(":");
        String host = array[0];
        int port = Integer.parseInt(array[1]);
        ConnectionManager.getInstance().connectServerNode(host, port);
    }

    public <T> T createService(Class<T> interfaceClass, int version) {
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new ObjectProxy<T>(interfaceClass, version));
    }

    public void finish() {
        ConnectionManager.getInstance().close();
    }
}
