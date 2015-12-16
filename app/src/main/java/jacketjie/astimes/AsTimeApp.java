package jacketjie.astimes;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import jacketjie.astimes.greenDao.ATUser;
import jacketjie.astimes.greenDao.DaoMaster;
import jacketjie.astimes.greenDao.DaoSession;

/**
 * Created by Administrator on 2015/12/9.
 */
public class AsTimeApp extends Application {
    private static Context context;
    public DaoSession daoSession;
    public static ATUser curATUser;

    public static ATUser getCurATUser() {
        return curATUser;
    }

    public static void setCurATUser(ATUser curATUser) {
        AsTimeApp.curATUser = curATUser;
    }

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context =getApplicationContext();
        setupDatabase();
    }

    private void setupDatabase() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "astimes-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
