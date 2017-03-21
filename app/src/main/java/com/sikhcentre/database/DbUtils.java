package com.sikhcentre.database;

import android.content.Context;
import android.util.Log;

import com.sikhcentre.entities.DaoMaster;
import com.sikhcentre.entities.DaoSession;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.QueryBuilder;

/**
 * Created by brinder.singh on 15/02/17.
 */

public enum DbUtils {
    INSTANCE;
    private static final String TAG = "DbUtils";
    public static final String SIKH_CS_DB_NAME = "SikhCentreDB";
    private DaoSession daoSession;

    public synchronized void init(Context context) {
        if (daoSession == null) {
            daoSession = new DaoMaster(new DbHelper(context, SIKH_CS_DB_NAME).getWritableDatabase()).newSession();
            QueryBuilder.LOG_SQL = false;
        }
    }

    public DaoSession getDaoSession() throws Exception {
        if (daoSession == null) {
            throw new Exception("DB not initialized, Please initialize it");
        }
        return daoSession;
    }

    public static void executeDML(String query, Object... params) throws Exception {
        Log.i(TAG, "Executing query " + query);
        if (params.length != 0) {
            INSTANCE.getDaoSession().getDatabase().execSQL(query, params);
        } else {
            INSTANCE.getDaoSession().getDatabase().execSQL(query);
        }
    }

    public static void endTransaction(Database database) {
        try {
            if (database != null) {
                database.endTransaction();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in SQLiteDatabase endTransaction ", e);
        }
    }
}
