package jacketjie.astimes.greenDao;

import android.content.Context;

import java.util.List;

import de.greenrobot.dao.query.Query;
import jacketjie.astimes.AsTimeApp;

/**
 * Created by Administrator on 2015/12/16.
 */
public class GreenDaoUtils {
    public static void insertOrUpdate(Context context, ATUser user) {
        getUserDao(context).insertOrReplace(user);
    }

    public static void clearUsers(Context context) {
        getUserDao(context).deleteAll();
    }

    public static void deleteUserWithId(Context context, long id) {
        getUserDao(context).delete(getUserForId(context, id));
    }

    public static ATUser getUserForId(Context context, long id) {
        return getUserDao(context).load(id);
    }

    public static ATUser getUserFromLogin(Context context,String userName,String password){
        Query<ATUser> query = getUserDao(context).queryBuilder().where(ATUserDao.Properties.UserName.eq(userName) , ATUserDao.Properties.UserPassword.eq(password)).build();
        ATUser atUser = query.unique();
        return atUser;
    }
    public static ATUser getUserForStauts(Context context){
        Query<ATUser> query = getUserDao(context).queryBuilder().where(ATUserDao.Properties.IsActiveUser.eq(true)).build();
        ATUser atUser = query.unique();
        return atUser;
    }
    public static boolean isUserHadExisted(Context context,String userName){
        Query<ATUser> query = getUserDao(context).queryBuilder().where(ATUserDao.Properties.UserName.eq(userName)).build();
        List<ATUser> list = query.list();
        if (list == null || list.size() == 0){
            return false;
        }
        return true ;
    }


    public static List<ATUser> getAllUsers(Context context) {
        return getUserDao(context).loadAll();
    }

    private static ATUserDao getUserDao(Context c) {
        return ((AsTimeApp) c.getApplicationContext()).getDaoSession().getATUserDao();
    }
}
