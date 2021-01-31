package cool.zzy.sems.rpc.common.codec;

import java.io.Serializable;

/**
 * @author intent zzy.main@gmail.com
 * @date 2021/1/11 16:08
 * @since 1.0
 */
public class RpcResponse implements Serializable {
    private static final long serialVersionUID = -1743045036587186823L;

    private String requestId;
    private String error;
    private Object result;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
