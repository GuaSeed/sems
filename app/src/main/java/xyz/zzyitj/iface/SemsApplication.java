package xyz.zzyitj.iface;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;
import cool.zzy.rpc.client.RpcClient;
import cool.zzy.rpc.service.HelloService;
import xyz.zzyitj.iface.constant.Const;

/**
 * xyz.zzyitj.iface
 *
 * @author intent zzy.main@gmail.com
 * @date 2020/9/8 14:51
 * @since 1.0
 */
public class SemsApplication extends Application {
    private static final String TAG = SemsApplication.class.getSimpleName();
    public static SemsApplication instance;
    private volatile boolean isInitRPC;
    private RpcClient rpcClient;
    private HelloService helloService;

    public SemsApplication() {
        instance = this;
        Log.d(TAG, "SemsApplication: ");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        initRPC();
    }

    @SuppressLint("DefaultLocale")
    public void initRPC() {
        Log.d(TAG, "initRPC: ");
        rpcClient = new RpcClient(
                String.format("%s:%d", Const.RPC_IP, Const.RPC_PORT),
                future -> {
                    if (future.isSuccess()) {
                        initService();
                    } else {
                        Log.d(TAG, "initRPC: " + future.toString());
                    }
                    isInitRPC = future.isSuccess();
                });
    }

    private void initService() {
        Log.d(TAG, "initService: ");
        helloService = RpcClient.createService(HelloService.class, 1);
    }

    /**
     * 设置API本地存储，SharePreface方式
     *
     * @param key   key
     * @param value value
     * @param <T>   类型
     */
    public <T> void putLocalStorage(String sharedPrefsName, String key, T value) {
        Log.d(TAG, "put: " + key + ", value: " + value);
        SharedPreferences sp = this.getSharedPreferences(sharedPrefsName, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else {
            editor.putString(key, (String) value);
        }
        editor.apply();
    }

    /**
     * 获取本地存储的数据
     *
     * @param key    key
     * @param tClass 类型
     * @param <T>    类型
     * @return 数据
     */
    public <T> T getLocalStorage(String sharedPrefsName, String key, Class<T> tClass) {
        SharedPreferences sp = this.getSharedPreferences(sharedPrefsName, MODE_PRIVATE);
        if (tClass == Long.class) {
            Object o = sp.getLong(key, -1);
            return (T) o;
        }
        return (T) sp.getString(key, null);
    }

    /**
     * 根据key移除本地数据
     *
     * @param key key
     */
    public void removeLocalStorage(String sharedPrefsName, String key) {
        SharedPreferences sp = this.getSharedPreferences(sharedPrefsName, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.apply();
    }

    public RpcClient getRpcClient() {
        return rpcClient;
    }

    public HelloService getHelloService() {
        return helloService;
    }

    public boolean isInitRPC() {
        return isInitRPC;
    }

//    public void putUser(ApiUserDto apiUserDto) {
//        String json = new Gson().toJson(apiUserDto);
//        putLocalStorage(ApiConst.SHARED_PREFS_NAME, ApiConst.SHARED_PREFS_USER, json);
//        this.userDto = apiUserDto;
//    }

//    public ApiUserDto getUser() {
//        String data = getLocalStorage(ApiConst.SHARED_PREFS_NAME, ApiConst.SHARED_PREFS_USER, String.class);
//        ApiUserDto apiUserDto = new Gson().fromJson(data, ApiUserDto.class);
//        if (apiUserDto != null) {
//            this.userDto = apiUserDto;
//        }
//        return apiUserDto;
//    }

//    public void removeUser() {
//        removeLocalStorage(ApiConst.SHARED_PREFS_NAME, ApiConst.SHARED_PREFS_USER);
//    }
}
