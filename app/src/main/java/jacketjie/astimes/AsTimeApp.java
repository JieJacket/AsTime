package jacketjie.astimes;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2015/12/9.
 */
public class AsTimeApp extends Application {
    private static Context context;

    public static Context getContext() {
        return context;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        context =getApplicationContext();
    }
}
