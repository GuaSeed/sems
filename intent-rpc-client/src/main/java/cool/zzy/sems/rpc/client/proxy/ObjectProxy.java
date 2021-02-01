package cool.zzy.sems.rpc.client.proxy;


import cool.zzy.sems.rpc.client.core.ConnectionManager;
import cool.zzy.sems.rpc.client.core.RpcClientHandler;
import cool.zzy.sems.rpc.common.codec.RpcRequest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * @author intent zzy.main@gmail.com
 * @date 2021/1/12 10:33
 * @since 1.0
 */
public class ObjectProxy<T> implements InvocationHandler {
    private static final String TAG = ObjectProxy.class.getSimpleName();
    private final Class<T> clazz;
    private final int version;

    public ObjectProxy(Class<T> clazz, int version) {
        this.clazz = clazz;
        this.version = version;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class == method.getDeclaringClass()) {
            String name = method.getName();
            if ("equals".equals(name)) {
                return proxy == args[0];
            } else if ("hashCode".equals(name)) {
                return System.identityHashCode(proxy);
            } else if ("toString".equals(name)) {
                return proxy.getClass().getName() + "@" +
                        Integer.toHexString(System.identityHashCode(proxy)) +
                        ", with InvocationHandler " + this;
            } else {
                throw new IllegalStateException(String.valueOf(method));
            }
        }
        RpcRequest request = new RpcRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParameters(args);
        request.setVersion(version);
//        String serviceKey = ServiceUtils.generateKey(method.getDeclaringClass().getName(), version);
        RpcClientHandler handler = ConnectionManager.getInstance().getHandler(future -> {
        });
        RpcFuture future = handler.sendRequest(request);
        if (future.get() instanceof Exception){
            throw (Exception) future.get();
        }
        return future.get();
    }
}
