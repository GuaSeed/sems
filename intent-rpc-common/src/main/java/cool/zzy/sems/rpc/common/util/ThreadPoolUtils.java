package cool.zzy.sems.rpc.common.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author intent zzy.main@gmail.com
 * @date 2021/1/11 15:20
 * @since 1.0
 */
public class ThreadPoolUtils {
    public static ThreadPoolExecutor makeServerThreadPool(final String serviceName, int corePoolSize, int maxPoolSize) {
        return new ThreadPoolExecutor(corePoolSize, maxPoolSize,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1000),
                (ThreadFactory) r -> {
                    return new Thread(r, "intent-rpc-" + serviceName + "-" + r.hashCode());
                },
                new ThreadPoolExecutor.AbortPolicy());
    }
}
