package com.example.proj12_biblioteca.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.proj12_biblioteca.model.Aluno;

import java.util.ArrayList;
import java.util.List;

public class AlunoDao extends SQLiteOpenHelper implements ICRUDDao<Aluno> {
    private static final String DATABASE_NAME = "biblioteca.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Aluno";
    private static final String COLUMN_RA = "RA";
    private static final String COLUMN_NOME = "Nome";
    private static final String COLUMN_EMAIL = "Email";

    public AlunoDao(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_RA + " INTEGER PRIMARY KEY, " +
                COLUMN_NOME + " VARCHAR(100), " +
                COLUMN_EMAIL + " VARCHAR(50))";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void insert(Aluno aluno) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RA, aluno.getRA());
        values.put(COLUMN_NOME, aluno.getNome());
        values.put(COLUMN_EMAIL, aluno.getEmail());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    @Override
    public int update(Aluno aluno) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOME, aluno.getNome());
        values.put(COLUMN_EMAIL, aluno.getEmail());
        int rowsAffected = db.update(TABLE_NAME, values, COLUMN_RA + " = ?", new String[]{String.valueOf(aluno.getRA())});
        db.close();
        return rowsAffected;
    }

    @Override
    public void delete(Aluno aluno) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_RA + " = ?", new String[]{String.valueOf(aluno.getRA())});
        db.close();
    }

    @Override
    public Aluno findOne(Aluno aluno) throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_RA, COLUMN_NOME, COLUMN_EMAIL},
                COLUMN_RA + " = ?", new String[]{String.valueOf(aluno.getRA())},
                null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Aluno foundAluno = new Aluno(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
            cursor.close();
            db.close();
            return foundAluno;
        } else {
            cursor.close();
            db.close();
            return null;
        }
    }

    @Override
    public List<Aluno> findAll() throws SQLException {
        List<Aluno> alunos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                Aluno aluno = new Aluno(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
                alunos.add(aluno);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return alunos;
    }
}