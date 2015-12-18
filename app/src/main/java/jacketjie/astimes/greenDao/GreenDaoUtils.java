package jacketjie.astimes.greenDao;

import android.content.Context;

import java.util.List;

import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;
import jacketjie.astimes.AsTimeApp;

/**
 * Created by Administrator on 2015/12/16.
 */
public class GreenDaoUtils {
    public static void insertOrUpdateUser(Context context, ATUser user) {
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


    /**
     * 增加或者更新ATIE
     * @param context
     * @param essay
     */
    public static void insertOrUpdateInformalEssay(Context context, ATInformalEssay essay) {
        getInformalEssayDao(context).insertOrReplace(essay);
    }

    /**
     * 获取所有共享的ATIE，以更新时间，降序排序
     * @param context
     * @return
     */
    public static List<ATInformalEssay> getAllSharedInformalEssayByDate(Context context) {
        List<ATInformalEssay> sharedEssay  = getInformalEssayDao(context).queryBuilder().where(ATInformalEssayDao.Properties.ATIEShared.eq(1)).orderDesc(ATInformalEssayDao.Properties.ATIEReleaseDate).list();
        return sharedEssay;
    }

    /**
     * 获取所有ATIE，更新时间降序
     * @param context
     * @return
     */
    public static List<ATInformalEssay> getAllInformalEssayByDate(Context context) {
        List<ATInformalEssay> sharedEssay = getInformalEssayDao(context).queryBuilder().where(ATInformalEssayDao.Properties.ATIEHasSubmit.eq(1)).list();
        return sharedEssay;
    }

    /**
     * 获取所有ATIE，更新时间降序
     * @param context
     * @return
     */
    public static ATInformalEssay getLastNotSubmit(Context context) {
        QueryBuilder<ATInformalEssay> qb = getInformalEssayDao(context).queryBuilder().where(ATInformalEssayDao.Properties.ATIEHasSubmit.eq(0));
        return qb.unique();
    }
    public static ATInformalEssay getLastNotByATIEId(Context context,String ATIEId) {
        QueryBuilder<ATInformalEssay> qb = getInformalEssayDao(context).queryBuilder().where(ATInformalEssayDao.Properties.ATIEId.eq(ATIEId));
        return qb.unique();
    }

    /**
     * 根据Id获取ATIE
     * @param context
     * @param id
     * @return
     */
    public static ATInformalEssay getEssaysForId(Context context, long id) {
        return getInformalEssayDao(context).load(id);
    }

    /**
     * 清空所有ATIE
     * @param context
     */
    public static void clearEssays(Context context) {
        getInformalEssayDao(context).deleteAll();
    }

    /**
     * 删除某条ATIE
     * @param context
     * @param id
     */
    public static void deleteEssayWithId(Context context, long id) {
        getInformalEssayDao(context).delete(getEssaysForId(context, id));
    }

    /**
     * 删除某条ATIE
     * @param context
     * @param essay
     */
    public static void deleteEssay(Context context, ATInformalEssay essay) {
        getInformalEssayDao(context).delete(essay);
    }

    private static ATUserDao getUserDao(Context c) {
        return ((AsTimeApp) c.getApplicationContext()).getDaoSession().getATUserDao();
    }

    private static ATInformalEssayDao getInformalEssayDao(Context c){
        return ((AsTimeApp) c.getApplicationContext()).getDaoSession().getATInformalEssayDao();
    }
}
