package com.example.proj12_biblioteca.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.proj12_biblioteca.model.Revista;

import java.util.ArrayList;
import java.util.List;

public class RevistaDao extends ExemplarDao implements ICRUDDao<Revista> {
    private static final String TABLE_NAME = "Revista";
    private static final String COLUMN_CODIGO = "ExemplarCodigo";
    private static final String COLUMN_ISSN = "ISSN";

    private Context context;

    public RevistaDao(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        super.onCreate(db);
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_CODIGO + " INTEGER PRIMARY KEY, " +
                COLUMN_ISSN + " CHAR(8))";
        db.execSQL(createTable);
    }

    @Override
    public void insert(Revista revista) throws SQLException {
        super.insertExemplar(revista);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CODIGO, revista.getCodigo());
        values.put(COLUMN_ISSN, revista.getISSN());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    @Override
    public int update(Revista revista) throws SQLException {
        super.updateExemplar(revista);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ISSN, revista.getISSN());
        int rowsAffected = db.update(TABLE_NAME, values, COLUMN_CODIGO + " = ?", new String[]{String.valueOf(revista.getCodigo())});
        db.close();
        return rowsAffected;
    }

    @Override
    public void delete(Revista revista) throws SQLException {
        super.deleteExemplar(revista);
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_CODIGO + " = ?", new String[]{String.valueOf(revista.getCodigo())});
        db.close();
    }

    @Override
    public Revista findOne(Revista revista) throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_CODIGO, COLUMN_ISSN},
                COLUMN_CODIGO + " = ?", new String[]{String.valueOf(revista.getCodigo())},
                null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Revista foundRevista = new Revista(cursor.getInt(0), null, 0, cursor.getString(1));
            cursor.close();
            db.close();
            return foundRevista;
        } else {
            cursor.close();
            db.close();
            return null;
        }
    }

    @Override
    public List<Revista> findAll() throws SQLException {
        List<Revista> revistas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                Revista revista = new Revista(cursor.getInt(0), null, 0, cursor.getString(1));
                revistas.add(revista);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return revistas;
    }

    public Context getContext() {
        return context;
    }
}