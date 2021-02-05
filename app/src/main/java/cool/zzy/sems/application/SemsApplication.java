package cool.zzy.sems.application;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;
import com.google.gson.Gson;
import cool.zzy.sems.application.constant.Const;
import cool.zzy.sems.context.model.User;
import cool.zzy.sems.context.service.HelloService;
import cool.zzy.sems.context.service.UserService;
import cool.zzy.sems.rpc.client.RpcClient;


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
    private String ip;
    private User user;
    private RpcClient rpcClient;
    private HelloService helloService;
    private UserService userService;

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
        userService = RpcClient.createService(UserService.class, 1);
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

    public UserService getUserService() {
        return userService;
    }

    public boolean isInitRPC() {
        return isInitRPC;
    }

    public void putUser(User user) {
        String json = new Gson().toJson(user);
        putLocalStorage(Const.SHARED_PREFS_NAME, Const.SHARED_PREFS_USER, json);
        this.user = user;
    }

    public User getUser() {
        String data = getLocalStorage(Const.SHARED_PREFS_NAME, Const.SHARED_PREFS_USER, String.class);
        User localUser = new Gson().fromJson(data, User.class);
        if (localUser != null) {
            this.user = localUser;
        }
        return localUser;
    }

    public void removeUser() {
        removeLocalStorage(Const.SHARED_PREFS_NAME, Const.SHARED_PREFS_USER);
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
