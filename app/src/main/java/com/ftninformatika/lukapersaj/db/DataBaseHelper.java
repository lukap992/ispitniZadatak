package com.ftninformatika.lukapersaj.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ftninformatika.lukapersaj.db.model.Grupa;
import com.ftninformatika.lukapersaj.db.model.TodoZadatak;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DataBaseHelper extends OrmLiteSqliteOpenHelper {

    public static final String DATABASE_NAME = "ormlite";
    private static final int DATABASE_VERSION = 1;

    private Dao<Grupa, Integer> getGrupa = null;
    private Dao<TodoZadatak, Integer> getZadatak = null;

    public DataBaseHelper(Context context){
        super(context, DATABASE_NAME,  null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Grupa.class);
            TableUtils.createTable(connectionSource, TodoZadatak.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Grupa.class, true);
            TableUtils.dropTable(connectionSource, TodoZadatak.class, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public Dao<Grupa, Integer> getGrupaDao() throws SQLException{
        if(getGrupa == null){
            getGrupa = getDao(Grupa.class);
        }
        return getGrupa;
    }
    public Dao<TodoZadatak, Integer> getZadatakDao() throws SQLException{
        if(getZadatak == null){
            getZadatak = getDao(TodoZadatak.class);
        }
        return getZadatak;
    }
}
