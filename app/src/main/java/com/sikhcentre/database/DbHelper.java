package com.sikhcentre.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;
import com.sikhcentre.entities.DaoMaster;

/**
 * Created by brinder.singh on 31/12/16.
 */

public class DbHelper extends DaoMaster.DevOpenHelper {
    private Context context;
    private static final Logger LOGGER = LoggerManager.getLogger();

    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        if (oldVersion < newVersion) {
            try {
                db.beginTransaction();
                switch (oldVersion) {
                    case 1:
                        upgradeFromVersion1to2(db);
                }
                db.setTransactionSuccessful();
            } catch (Exception e) {
                LOGGER.e("greenDAO : Exception migrating db:" + e);
            } finally {
                db.endTransaction();
            }
        }
    }

    private void upgradeFromVersion1to2(SQLiteDatabase db) {
    }
}
