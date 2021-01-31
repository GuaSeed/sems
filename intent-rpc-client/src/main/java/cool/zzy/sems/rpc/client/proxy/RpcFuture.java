package cool.zzy.sems.rpc.client.proxy;

import android.util.Log;
import cool.zzy.sems.rpc.common.codec.RpcRequest;
import cool.zzy.sems.rpc.common.codec.RpcResponse;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * @author intent zzy.main@gmail.com
 * @date 2021/1/12 14:38
 * @since 1.0
 */
public class RpcFuture implements Future<Object> {
    private static final String TAG = RpcFuture.class.getSimpleName();
    private static final long RESPONSE_TIME_OUT = 5000L;

    private Sync sync;
    private final RpcRequest request;
    private RpcResponse response;

    private long startTime;

    public RpcFuture(RpcRequest request) {
        this.sync = new Sync();
        this.request = request;
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isCancelled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDone() {
        return sync.isDone();
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {
        sync.acquire(1);
        if (this.response != null) {
            return this.response.getResult();
        }
        return null;
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        boolean success = sync.tryAcquireNanos(1, unit.toNanos(timeout));
        if (success) {
            if (this.response != null) {
                return this.response.getResult();
            } else {
                return null;
            }
        } else {
            throw new RuntimeException("Timeout exception. Request id: " + this.request.getRequestId()
                    + ". Request class name: " + this.request.getClassName()
                    + ". Request method: " + this.request.getMethodName());
        }
    }

    public void done(RpcResponse response) {
        this.response = response;
        sync.release(1);
        long responseTime = System.currentTimeMillis() - startTime;
        if (responseTime > RESPONSE_TIME_OUT) {
            Log.w(TAG, "Service response time is too slow. Request id = " + response.getRequestId() + ". Response Time = " + responseTime + "ms");
        }
    }

    static class Sync extends AbstractQueuedSynchronizer {
        private static final int DONE = 1;
        private static final int PENDING = 0;

        @Override
        protected boolean tryAcquire(int arg) {
            return getState() == DONE;
        }

        @Override
        protected boolean tryRelease(int arg) {
            if (getState() == PENDING) {
                return compareAndSetState(PENDING, DONE);
            }
            return true;
        }

        protected boolean isDone() {
            return getState() == DONE;
        }
    }
}
