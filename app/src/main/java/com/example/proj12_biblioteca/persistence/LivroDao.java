package com.example.proj12_biblioteca.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.proj12_biblioteca.model.Livro;

import java.util.ArrayList;
import java.util.List;

public class LivroDao extends ExemplarDao implements ICRUDDao<Livro> {
    private static final String TABLE_NAME = "Livro";
    private static final String COLUMN_CODIGO = "ExemplarCodigo";
    private static final String COLUMN_ISBN = "ISBN";
    private static final String COLUMN_EDICAO = "Edicao";

    private Context context;

    public LivroDao(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        super.onCreate(db);
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_CODIGO + " INTEGER PRIMARY KEY, " +
                COLUMN_ISBN + " CHAR(13), " +
                COLUMN_EDICAO + " INTEGER)";
        db.execSQL(createTable);
    }

    @Override
    public void insert(Livro livro) throws SQLException {
        super.insertExemplar(livro);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CODIGO, livro.getCodigo());
        values.put(COLUMN_ISBN, livro.getISBN());
        values.put(COLUMN_EDICAO, livro.getEdicao());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    @Override
    public int update(Livro livro) throws SQLException {
        super.updateExemplar(livro);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ISBN, livro.getISBN());
        values.put(COLUMN_EDICAO, livro.getEdicao());
        int rowsAffected = db.update(TABLE_NAME, values, COLUMN_CODIGO + " = ?", new String[]{String.valueOf(livro.getCodigo())});
        db.close();
        return rowsAffected;
    }

    @Override
    public void delete(Livro livro) throws SQLException {
        super.deleteExemplar(livro);
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_CODIGO + " = ?", new String[]{String.valueOf(livro.getCodigo())});
        db.close();
    }

    @Override
    public Livro findOne(Livro livro) throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_CODIGO, COLUMN_ISBN, COLUMN_EDICAO},
                COLUMN_CODIGO + " = ?", new String[]{String.valueOf(livro.getCodigo())},
                null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Livro foundLivro = new Livro(cursor.getInt(0), null, 0, cursor.getString(1), cursor.getInt(2));
            cursor.close();
            db.close();
            return foundLivro;
        } else {
            cursor.close();
            db.close();
            return null;
        }
    }

    @Override
    public List<Livro> findAll() throws SQLException {
        List<Livro> livros = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                Livro livro = new Livro(cursor.getInt(0), null, 0, cursor.getString(1), cursor.getInt(2));
                livros.add(livro);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return livros;
    }

    public Context getContext() {
        return context;
    }
}