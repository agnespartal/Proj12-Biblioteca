package com.example.proj12_biblioteca.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.proj12_biblioteca.model.Exemplar;

public abstract class ExemplarDao extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "biblioteca.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Exemplar";
    private static final String COLUMN_CODIGO = "Codigo";
    private static final String COLUMN_NOME = "Nome";
    private static final String COLUMN_QTDPAGINAS = "QtdPaginas";

    public ExemplarDao(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_CODIGO + " INTEGER PRIMARY KEY, " +
                COLUMN_NOME + " VARCHAR(50), " +
                COLUMN_QTDPAGINAS + " INTEGER)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertExemplar(Exemplar exemplar) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CODIGO, exemplar.getCodigo());
        values.put(COLUMN_NOME, exemplar.getNome());
        values.put(COLUMN_QTDPAGINAS, exemplar.getQtdPaginas());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public int updateExemplar(Exemplar exemplar) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOME, exemplar.getNome());
        values.put(COLUMN_QTDPAGINAS, exemplar.getQtdPaginas());
        int rowsAffected = db.update(TABLE_NAME, values, COLUMN_CODIGO + " = ?", new String[]{String.valueOf(exemplar.getCodigo())});
        db.close();
        return rowsAffected;
    }

    public void deleteExemplar(Exemplar exemplar) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_CODIGO + " = ?", new String[]{String.valueOf(exemplar.getCodigo())});
        db.close();
    }
}