package com.sikhcentre.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.sikhcentre.entities.DaoMaster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by brinder.singh on 31/12/16.
 */

public class DbHelper extends DaoMaster.OpenHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(DbHelper.class);

    public DbHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LOGGER.info("greenDAO : Upgrading Sikh Centre DB from version {} {} {}", oldVersion, " to ", newVersion);
        if (oldVersion < newVersion) {
            try {
                db.beginTransaction();
                switch (oldVersion) {
                    case 1:
                        upgradeFromVersion1to2(db);
                }
                db.setTransactionSuccessful();
            } catch (Exception e) {
                LOGGER.error("greenDAO : Exception migrating db:", e);
            } finally {
                db.endTransaction();
            }
        }
    }

    private void upgradeFromVersion1to2(SQLiteDatabase db) {
        LOGGER.info("updating to version2 from version1");
        //write migration queries here
    }
}

