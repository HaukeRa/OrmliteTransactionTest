package de.haukera.ormlitetest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import de.haukera.ormlitetest.tables.ChildTable;
import de.haukera.ormlitetest.tables.ParentTable;

/**
 * Created by Hauke on 16.03.2018.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static DatabaseHelper sharedInstance;

    private DatabaseHelper(Context context) {
        super(context, "test.db", null, 1, R.raw.ormlite_config);
    }

    public static DatabaseHelper getInstance(Context context) {
        if (sharedInstance == null) {
            sharedInstance = new DatabaseHelper(context);
        }
        return sharedInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, ParentTable.class);
            TableUtils.createTable(connectionSource, ChildTable.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
    }

    public Dao<ParentTable, Long> getParentDao() {
        try {
            return DaoManager.createDao(getConnectionSource(), ParentTable.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Dao<ChildTable, Long> getChildDao() {
        try {
            return DaoManager.createDao(getConnectionSource(), ChildTable.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
